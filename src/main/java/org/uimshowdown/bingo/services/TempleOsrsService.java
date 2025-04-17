package org.uimshowdown.bingo.services;

import java.util.List;
import java.util.Map;
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

        // We produce a map of temple ID/Skilling XP contribution methods so that we can perform constant time searches against all fields in the `detailed_gains` object
        Map<String, ContributionMethod> skillingXpContributionMethods = StreamSupport
            .stream(contributionMethods.spliterator(), false)
            .filter(
                contributionMethod ->
                    contributionMethod.getContributionMethodCategory() == ContributionMethod.Category.SKILLING
                    && contributionMethod.getContributionMethodType() == ContributionMethod.Type.XP
            )
            .collect(Collectors.toMap(
                ContributionMethod::getTempleId,
                Function.identity()
            ));

        try {
            JsonNode competitionSkillGains = restClient
                .get()
                .uri("/api/competition_info_v2.php?id={competition_id}&details=1", competitionConfiguration.getTempleCompetitionID())
                .retrieve()
                .body(JsonNode.class);

            if (competitionSkillGains.get("data").get("participants").isArray() == false) {
                throw new IllegalStateException(String.format("Expected `data.participants` to be a JSON array! Instead received: `%s`", competitionSkillGains.asText()));
            }

            for (JsonNode participant : competitionSkillGains.get("data").get("participants")) {
                Player player = players.get(participant.get("username").asText());
                if (player == null) {
                    continue;
                }

                JsonNode detailedSkillGains = participant.get("detailed_gains");
                if (detailedSkillGains == null) {
                    continue;
                }

                List<Contribution> relevantSkillGainContributions = StreamSupport
                    .stream(
                        Spliterators.spliteratorUnknownSize(detailedSkillGains.fields(), Spliterator.ORDERED),
                        false
                    )
                    .filter(
                        skillGain ->
                            skillingXpContributionMethods.containsKey(skillGain.getKey())
                            && skillGain.getValue().get("end_xp").asInt() > skillGain.getValue().get("start_xp").asInt()
                    )
                    .map(
                        skillGain -> new Contribution(
                            player,
                            skillingXpContributionMethods.get(skillGain.getKey()),
                            skillGain.getValue().get("start_xp").asInt(),
                            skillGain.getValue().get("end_xp").asInt()
                        )
                    )
                    .toList();

                player.getContributions().addAll(relevantSkillGainContributions);
                playerRepository.save(player);
            }
        } catch (Throwable e) {
            /** @todo Broadcast to a Discord `#errors` channel that pulling the competition skill gains failed */
            return;
        }

        /** @todo Repeat the steps above but for PVM KC contribution methods */
    }
}
