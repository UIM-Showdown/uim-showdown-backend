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
import org.springframework.transaction.annotation.Transactional;
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

        updatePlayerContributions(players, contributionMethods, "/api/competition_info_v2.php?id={competition_id}&details=1&skill=obor&altunranked=1");
        updatePlayerContributions(players, contributionMethods, "/api/competition_info_v2.php?id={competition_id}&details=1");
        for(Player player : players.values()) {
            handleSlayerXPPenalties(player);
            playerRepository.save(player);
        }
    }

    private void updatePlayerContributions(Map<String, Player> players, Map<String, ContributionMethod> contributionMethods, String uri) throws IllegalArgumentException {
        JsonNode competitionGains = null;
        int attempt = 1;
        try {
            competitionGains = restClient
                .get()
                .uri(uri, competitionConfiguration.getTempleCompetitionID())
                .retrieve()
                .body(JsonNode.class);
        } catch(Exception e) {
            if(attempt == 3) {
                throw e;
            }
            attempt++;
        }
        
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
                                playerGain.getValue().get("end_xp").asInt(),
                                false
                            );
                        }
                        
                        currentPlayerContribution.setIsEmpty(false);
                        currentPlayerContribution.setInitialValue(playerGain.getValue().get("start_xp").asInt());
                        currentPlayerContribution.setFinalValue(playerGain.getValue().get("end_xp").asInt());
                        return currentPlayerContribution;
                    }
                )
                .collect(Collectors.toSet());

            // This approach is taken since `player.getContributions().addAll(updatedContributions)` will prioritize existing contributions over updated ones
            updatedContributions.addAll(currentPlayerContributions.values());
            player.setContributions(updatedContributions);
        }
    }
    
    /**
     * Applies a penalty to the final value of the player's Slayer contribution, which is required to not double-count slayer bosses and Jad/Zuk
     * @param player
     */
    private void handleSlayerXPPenalties(Player player) {
        ContributionMethod slayerMethod = contributionMethodRepository.findByName("Slayer").orElse(null);
        if(slayerMethod == null) { // Slayer XP is not on the board
            return;
        }
        Map<String, Integer> slayerXPPenalties = competitionConfiguration.getSlayerXPPenalties();
        for(String methodName : slayerXPPenalties.keySet()) {
            ContributionMethod bossMethod = contributionMethodRepository.findByName(methodName).orElse(null);
            if(bossMethod == null) { // The boss is not on the board
                continue;
            }
            Contribution bossContribution = player.getContribution(bossMethod);
            int kcWithPenalty = bossContribution.getUnitsContributed();
            if(methodName.equals("TzKal-Zuk") && bossContribution.getUnrankedStartingValue() == 0 && bossContribution.getFinalValue() > 0) { // Special case - first Zuk KC cannot be on task
                kcWithPenalty--;
            }
            Contribution slayerContribution = player.getContribution(slayerMethod);
            int value = slayerContribution.getFinalValue() - (kcWithPenalty * slayerXPPenalties.get(methodName));
            if(value < 0) {
                value = 0;
            }
            slayerContribution.setFinalValue(value);
        }
    }
}
