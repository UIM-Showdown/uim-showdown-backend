package org.uimshowdown.bingo.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    public void synchronizeRosters() {
        // Establish maps for:
        // RSN in "true" format, according to our DB -> Team name
        // RSN in "Temple-style" format, according to our DB -> Team name
        // RSN in "Temple-style" format, according to Temple -> Team name
        Map<String, String> trueRSNFromDBToTeamName = new HashMap<String, String>();
        Map<String, String> templeStyleRSNFromDBToTeamName = new HashMap<String, String>();
        for(Player player : playerRepository.findAll()) {
            String teamName = player.getTeam().getName();
            if(!teamName.equals(competitionConfiguration.getWaitlistTeamName())) { // Waitlisted players are not "in the competition" for this
                trueRSNFromDBToTeamName.put(player.getRsn(), teamName);
                templeStyleRSNFromDBToTeamName.put(player.getRsn().toLowerCase().replace("_", " "), teamName);
            }
        }
        Map<String, String> templeStyleRSNFromTempleToTeamName = new HashMap<String, String>();
        JsonNode competitionGains = this.getCompetitionGains("/api/competition_info_v2.php?id={competition_id}&details=1");
        for(JsonNode participant : competitionGains.get("data").get("participants")) {
            String templeRSN = null;
            try { // Sometimes player_name_with_capitalization is null for some reason
                templeRSN = participant.get("player_name_with_capitalization").asText().toLowerCase();
            } catch(Exception e) {}
            if(templeRSN == null || templeRSN.equals("") || templeRSN.equals("null")) {
                templeRSN = participant.get("username").asText().toLowerCase();
            }
            templeStyleRSNFromTempleToTeamName.put(templeRSN, participant.get("team_name").asText());
        }
        
        // Handle removals for players whose Temple record has an incorrect team or is no longer in the competition
        List<String> removals = new ArrayList<String>();
        for(String templeStyleRSN : templeStyleRSNFromTempleToTeamName.keySet()) {
            String templeTeamName = templeStyleRSNFromTempleToTeamName.get(templeStyleRSN);
            String dbTeamName = templeStyleRSNFromDBToTeamName.get(templeStyleRSN);
            if(dbTeamName == null || !dbTeamName.equals(templeTeamName)) {
                removals.add(templeStyleRSN);
            }
        }
        if(!removals.isEmpty()) {            
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("id", competitionConfiguration.getTempleCompetitionID());
            body.put("key", competitionConfiguration.getTempleCompetitionEditKey());
            String removalCSV = String.join(",", removals);
            body.put("players", removalCSV);
            for(int attempt = 1; attempt <= 3; attempt++) {            
                try {
                    restClient
                        .post()
                        .uri("/api/competition_remove_participant.php")
                        .body(body)
                        .retrieve()
                        .body(JsonNode.class);
                    break;
                } catch(Exception e) {
                    if(attempt == 3) {
                        throw e;
                    }
                }
            }
        }
        
        // Handle additions for players whose Temple record is nonexistent or has the wrong team
        List<String> additions = new ArrayList<String>();
        for(String trueRSN : trueRSNFromDBToTeamName.keySet()) {
            String templeStyleRSN = trueRSN.toLowerCase().replace("_", " ");
            String dbTeamName = trueRSNFromDBToTeamName.get(trueRSN);
            String templeTeamName = templeStyleRSNFromTempleToTeamName.get(templeStyleRSN);
            if(templeTeamName == null || !dbTeamName.equals(templeTeamName)) {
                additions.add(trueRSN);
            }
        }
        if(!additions.isEmpty()) {
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("id", competitionConfiguration.getTempleCompetitionID());
            body.put("key", competitionConfiguration.getTempleCompetitionEditKey());
            Map<String, String> additionsObject = new HashMap<String, String>();
            String additionsCSV = String.join(",", additions);
            for(String trueRSN : additions) {
                String teamName = trueRSNFromDBToTeamName.get(trueRSN);
                additionsObject.put(trueRSN, teamName);
            }
            body.put("players", additionsCSV);
            body.put("teams", additionsObject);
            for(int attempt = 1; attempt <= 3; attempt++) {            
                try {
                    restClient
                        .post()
                        .uri("/api/competition_add_participant.php")
                        .body(body)
                        .retrieve()
                        .body(JsonNode.class);
                    break;
                } catch(Exception e) {
                    if(attempt == 3) {
                        throw e;
                    }
                }
            }
        }
    }

    /**
     * @implNote Filtering out contribution methods that have null temple IDs is done to prevent a `IllegalStateException` due to duplicate keys during the map creation process.
     */
    public void updateCompetition() {
        // Producing a map of RSN/player key value pairs so that we can do constant time searches against the participants list
        Map<String, Player> players = StreamSupport
            .stream(playerRepository.findAll().spliterator(), false)
            .collect(Collectors.toMap(
                player -> player.getRsn().toLowerCase().replace("_", " "), // TempleOSRS uses OSRS chat capitalization logic (i.e. only one capital per word), also does weird char replacements
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

        updatePlayerContributions(players, contributionMethods, "/api/competition_info_v2.php?id={competition_id}&details=1&skill=obor");
        updatePlayerContributions(players, contributionMethods, "/api/competition_info_v2.php?id={competition_id}&details=1");
        handleSlayerXPPenalties(players); // If slayer XP is not on the board, then does nothing
        
        for(Player player : players.values()) {         
            playerRepository.save(player);
        }
    }

    private void updatePlayerContributions(Map<String, Player> players, Map<String, ContributionMethod> contributionMethods, String uri) throws IllegalArgumentException {
        JsonNode competitionGains = this.getCompetitionGains(uri);
        for (JsonNode participant : competitionGains.get("data").get("participants")) {
            String templeRSN = null;
            try { // Sometimes player_name_with_capitalization is null for some reason
                templeRSN = participant.get("player_name_with_capitalization").asText().toLowerCase();
            } catch(Exception e) {}
            if(templeRSN == null || templeRSN.equals("") || templeRSN.equals("null")) {
                templeRSN = participant.get("username").asText().toLowerCase();
            }
            Player player = players.get(templeRSN);
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
     * Applies a penalty to the final value of each player's Slayer contribution, which is required to not double-count slayer bosses and Jad/Zuk
     * @param players
     */
    private void handleSlayerXPPenalties(Map<String, Player> players) {
        ContributionMethod slayerXPMethod = contributionMethodRepository.findByName("Slayer").orElse(null);
        if(slayerXPMethod == null) { // Slayer XP is not on the board
            return;
        }
        Map<String, Integer> slayerXPPenalties = competitionConfiguration.getSlayerXPPenalties();
        Map<String, ContributionMethod> slayerBossMethods = new HashMap<String, ContributionMethod>();
        for(String methodName : slayerXPPenalties.keySet()) {
            ContributionMethod bossMethod = contributionMethodRepository.findByName(methodName).orElse(null);
            slayerBossMethods.put(methodName, bossMethod);
        }
        for(Player player : players.values()) {            
            for(String methodName : slayerXPPenalties.keySet()) {
                ContributionMethod bossMethod = slayerBossMethods.get(methodName);
                if(bossMethod == null) { // The boss is not on the board
                    continue;
                }
                Contribution bossContribution = player.getContribution(bossMethod);
                int kcWithPenalty = bossContribution.getUnitsContributed();
                if(methodName.equals("TzKal-Zuk") && bossContribution.getInitialValue() == 0 && bossContribution.getFinalValue() > 0) { // Special case - first Zuk KC cannot be on task
                    kcWithPenalty--;
                }
                Contribution slayerContribution = player.getContribution(slayerXPMethod);
                int value = slayerContribution.getFinalValue() - (kcWithPenalty * slayerXPPenalties.get(methodName));
                if(value < slayerContribution.getInitialValue()) {
                    value = slayerContribution.getInitialValue();
                }
                slayerContribution.setFinalValue(value);
            }
        }
    }
    
    private JsonNode getCompetitionGains(String uri) throws IllegalStateException {
        JsonNode competitionGains = null;
        for(int attempt = 1; attempt <= 3; attempt++) {            
            try {
                competitionGains = restClient
                        .get()
                        .uri(uri, competitionConfiguration.getTempleCompetitionID())
                        .retrieve()
                        .body(JsonNode.class);
                break;
            } catch(Exception e) {
                if(attempt == 3) {
                    throw e;
                }
            }
        }
        
        if (competitionGains == null) {
            throw new IllegalStateException("Expected a response body, but received nothing!");
        }

        if (competitionGains.get("data").get("participants").isArray() == false) {
            throw new IllegalStateException(String.format("Expected `data.participants` to be a JSON array! Instead received: `%s`", competitionGains.asText()));
        }
        return competitionGains;
    }
}
