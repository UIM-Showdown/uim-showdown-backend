package org.uimshowdown.bingo.services;

import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.repositories.ContributionMethodRepository;
import org.uimshowdown.bingo.repositories.PlayerRepository;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class TempleOsrsService {

    public enum Api { CLUES_PVM, SKILLING }

    @Autowired
    private CompetitionConfiguration competitionConfiguration;
    
    @Autowired
    @Qualifier("templeOsrsClient")
    private RestClient restClient;

    @Autowired
    private ContributionMethodRepository contributionMethodRepository;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * @implNote Filtering out contribution methods that have null temple IDs is done to prevent a `IllegalStateException` due to duplicate keys during the map creation process.
     */
    public void updateCompetition() {
        // Producing a map of RSN/player key value pairs so that we can do constant time searches against the participants list
        Map<String, Player> players = StreamSupport
            .stream(playerRepository.findAll().spliterator(), false)
            .collect(Collectors.toMap(
                player -> player.getRsn().toLowerCase(), // TempleOSRS uses OSRS chat capitalization logic (i.e. only one capital per word)
                Function.identity()
            ));

        // Producing a map of temple ID/contribution methods so that we can perform constant time searches against all fields in the `detailed_gains` object
        Map<String, ContributionMethod> contributionMethods = StreamSupport
            .stream(contributionMethodRepository.findAll().spliterator(), false)
            .filter(contributionMethod -> contributionMethod.getTempleId() != null)
            .collect(Collectors.toMap(
                ContributionMethod::getTempleId,
                Function.identity()
            ));

        updatePlayerContributions(players, contributionMethods, Api.CLUES_PVM);
        updatePlayerContributions(players, contributionMethods, Api.SKILLING);
    }

    private void updatePlayerContributions(Map<String, Player> players, Map<String, ContributionMethod> contributionMethods, TempleOsrsService.Api api) throws IllegalArgumentException {
        String apiPath;

        switch (api) {
            case CLUES_PVM:
                apiPath = "/api/competition_info_v2.php?id={competition_id}&details=1&skill=obor";
                break;
            case SKILLING:
                apiPath = "/api/competition_info_v2.php?id={competition_id}&details=1";
                break;
            default:
                throw new IllegalArgumentException("The given API is not supported!");
        }

        try {
            JsonNode competitionGains = restClient
                .get()
                .uri(apiPath, competitionConfiguration.getTempleCompetitionID())
                .retrieve()
                .body(JsonNode.class);

            if (competitionGains == null) {
                throw new IllegalStateException("Expected a response body, but received nothing!");
            }

            if (competitionGains.get("data").get("participants").isArray() == false) {
                throw new IllegalStateException(String.format("Expected `data.participants` to be a JSON array! Instead received: `%s`", competitionGains.asText()));
            }

            for (JsonNode participant : competitionGains.get("data").get("participants")) {
                Player player = players.get(participant.get("username").asText().toLowerCase());
                if (player == null) {
                    continue;
                }

                JsonNode detailedPlayerGains = participant.get("detailed_gains");
                if (detailedPlayerGains == null) {
                    continue;
                }

                // Map contributions to contribution methods for constant time searches
                Map<ContributionMethod, Contribution> currentPlayerContributions = player
                    .getContributions()
                    .stream()
                    .collect(Collectors.toMap(
                        Contribution::getContributionMethod,
                        Function.identity()
                    ));

                Set<Contribution> updatedContributions = StreamSupport
                    .stream(
                        Spliterators.spliteratorUnknownSize(detailedPlayerGains.fields(), Spliterator.ORDERED),
                        false
                    )
                    .filter(
                        playerGain ->
                            contributionMethods.containsKey(playerGain.getKey()) // is this player gain relevant to the comp?
                    )
                    .map(
                        playerGain -> {
                            ContributionMethod contributionMethod = contributionMethods.get(playerGain.getKey());
                            Contribution currentPlayerContribution = currentPlayerContributions.get(contributionMethod);
                            if (currentPlayerContribution == null) {
                                return new Contribution(
                                    player,
                                    contributionMethod,
                                    playerGain.getValue().get("start_xp").asInt(),
                                    playerGain.getValue().get("end_xp").asInt()
                                );
                            }
                            
                            currentPlayerContribution.setInitialValue(playerGain.getValue().get("start_xp").asInt());
                            currentPlayerContribution.setFinalValue(playerGain.getValue().get("end_xp").asInt());
                            return currentPlayerContribution;
                        }
                    )
                    .collect(Collectors.toSet());

                // This approach is taken since `player.getContributions().addAll(updatedContributions)` will prioritize existing contributions over updated ones
                updatedContributions.addAll(currentPlayerContributions.values());
                player.setContributions(updatedContributions);
                playerRepository.save(player);
            }
        } catch (Throwable e) {
            /** @todo Broadcast to a Discord `#errors` channel that updating player contributions via Temple's API failed */
            return;
        }
    }
}
