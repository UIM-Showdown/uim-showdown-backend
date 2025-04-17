package org.uimshowdown.bingo.services;

import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Predicate;
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

    public enum Api { PVM, SKILLING }

    @Autowired
    private CompetitionConfiguration competitionConfiguration;
    
    @Autowired
    @Qualifier("templeOsrsClient")
    private RestClient restClient;

    @Autowired
    private ContributionMethodRepository contributionMethodRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public void updateCompetition() {
        // We produce a map of RSN/Player key value pairs so that we can do constant time searches against the participants list
        Map<String, Player> players = StreamSupport
            .stream(playerRepository.findAll().spliterator(), false)
            .collect(Collectors.toMap(
                Player::getRsn,
                Function.identity()
            ));

        Iterable<ContributionMethod> contributionMethods = contributionMethodRepository.findAll();

        updatePlayerContributions(players, contributionMethods, Api.PVM);
        updatePlayerContributions(players, contributionMethods, Api.SKILLING);
    }

    private void updatePlayerContributions(Map<String, Player> players, Iterable<ContributionMethod> contributionMethods, TempleOsrsService.Api api) throws IllegalArgumentException {
        String apiPath;
        Predicate<ContributionMethod> filterBy;

        switch (api) {
            case PVM:
                apiPath = "/api/competition_info_v2.php?id={competition_id}&details=1&skill=Obor";
                filterBy = contributionMethod ->
                    contributionMethod.getContributionMethodCategory() == ContributionMethod.Category.PVM
                    && contributionMethod.getContributionMethodType() == ContributionMethod.Type.KC;
                break;
            case SKILLING:
                apiPath = "/api/competition_info_v2.php?id={competition_id}&details=1";
                filterBy = contributionMethod ->
                    contributionMethod.getContributionMethodCategory() == ContributionMethod.Category.SKILLING
                    && contributionMethod.getContributionMethodType() == ContributionMethod.Type.XP;
                break;
            default:
                throw new IllegalArgumentException("The given API is not supported!");
        }

        // We produce a map of temple ID/Skilling XP contribution methods so that we can perform constant time searches against all fields in the `detailed_gains` object
        Map<String, ContributionMethod> relevantContributionMethods = StreamSupport
            .stream(contributionMethods.spliterator(), false)
            .filter(filterBy)
            .collect(Collectors.toMap(
                ContributionMethod::getTempleId,
                Function.identity()
            ));

        try {
            JsonNode competitionGains = restClient
                .get()
                .uri(apiPath, competitionConfiguration.getTempleCompetitionID())
                .retrieve()
                .body(JsonNode.class);

            if (competitionGains.get("data").get("participants").isArray() == false) {
                throw new IllegalStateException(String.format("Expected `data.participants` to be a JSON array! Instead received: `%s`", competitionGains.asText()));
            }

            for (JsonNode participant : competitionGains.get("data").get("participants")) {
                Player player = players.get(participant.get("username").asText());
                if (player == null) {
                    continue;
                }

                JsonNode detailedGains = participant.get("detailed_gains");
                if (detailedGains == null) {
                    continue;
                }

                List<Contribution> relevantGainContributions = StreamSupport
                    .stream(
                        Spliterators.spliteratorUnknownSize(detailedGains.fields(), Spliterator.ORDERED),
                        false
                    )
                    .filter(
                        skillGain ->
                            relevantContributionMethods.containsKey(skillGain.getKey())
                            && skillGain.getValue().get("end_xp").asInt() > skillGain.getValue().get("start_xp").asInt()
                    )
                    .map(
                        skillGain -> new Contribution(
                            player,
                            relevantContributionMethods.get(skillGain.getKey()),
                            skillGain.getValue().get("start_xp").asInt(),
                            skillGain.getValue().get("end_xp").asInt()
                        )
                    )
                    .toList();

                player.getContributions().addAll(relevantGainContributions);
                playerRepository.save(player);
            }
        } catch (Throwable e) {
            /** @todo Broadcast to a Discord `#errors` channel that pulling the competition skill gains failed */
            return;
        }
    }
}
