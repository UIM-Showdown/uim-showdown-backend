package org.uimshowdown.bingo.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration.TileGroupConfig;
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.ChallengeCompletion;
import org.uimshowdown.bingo.models.ChallengeLeaderboardEntry;
import org.uimshowdown.bingo.models.CollectionLogCompletion;
import org.uimshowdown.bingo.models.CollectionLogItem;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.PlayerScoreboard;
import org.uimshowdown.bingo.models.Record;
import org.uimshowdown.bingo.models.RecordCompletion;
import org.uimshowdown.bingo.models.RecordLeaderboardEntry;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.TeamScoreboard;
import org.uimshowdown.bingo.models.Tile;
import org.uimshowdown.bingo.models.TileProgress;
import org.uimshowdown.bingo.repositories.ChallengeRepository;
import org.uimshowdown.bingo.repositories.CollectionLogItemRepository;
import org.uimshowdown.bingo.repositories.PlayerRepository;
import org.uimshowdown.bingo.repositories.PlayerScoreboardRepository;
import org.uimshowdown.bingo.repositories.RecordRepository;
import org.uimshowdown.bingo.repositories.TeamRepository;
import org.uimshowdown.bingo.repositories.TeamScoreboardRepository;
import org.uimshowdown.bingo.repositories.TileProgressRepository;
import org.uimshowdown.bingo.repositories.TileRepository;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

@Component
public class ScoreboardCalculationService {
    
    @Autowired PlayerScoreboardRepository playerScoreboardRepository;
    @Autowired TeamScoreboardRepository teamScoreboardRepository;
    @Autowired CompetitionConfiguration competitionConfiguration;
    @Autowired PlayerRepository playerRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired TileProgressRepository tileProgressRepository;
    @Autowired CollectionLogItemRepository collectionLogItemRepository;
    @Autowired RecordRepository recordRepository;
    @Autowired ChallengeRepository challengeRepository;
    @Autowired TileRepository tileRepository;
    @Autowired JDA discordClient;
    
    @Value("${discord.guildId}")
    long guildId;
    
    @Transactional
    public void calculate() {
        List<Team> teams = new ArrayList<Team>();
        for(Team team : teamRepository.findAll()) {
            teams.add(team);
            for(Player player : team.getPlayers()) {
                calculatePlayerScoreboard(player);
            }
            calculateTeamScoreboard(team);
        }
        
        // Because challenge/record points are dependent on other teams' points too, we have to do them all at once
        calculateAllRecords();
        calculateAllChallenges();
        calculateAllTotalPoints();
        for(Team team : teams) {
            teamRepository.save(team);
        }
    }
    
    /**
     * Updates the event points from tiles/groups/clog items on a team's scoreboard.
     * 
     * Does not update the event points from records/challenges or the total event points, because those depend 
     * on other teams' points too.
     * @param team
     */
    private void calculateTeamScoreboard(Team team) {
        TeamScoreboard scoreboard = team.getScoreboard();
        Map<String, Integer> initialTiers = getTiers(team);
        
        // Update tile progresses
        for(TileProgress progress : team.getTileProgresses()) {
            updateTileProgress(progress);
        }
        
        // Create a list of lists of tile progresses to represent all the tile groups
        List<List<TileProgress>> groupsOfProgresses = new ArrayList<List<TileProgress>>();
        for(TileGroupConfig groupConfig : competitionConfiguration.getTileGroups()) {
            List<TileProgress> progresses = new ArrayList<TileProgress>();
            for(String tileName : groupConfig.getTileNames()) {
                for(TileProgress progress : team.getTileProgresses()) {
                    if(progress.getTile().getName().equals(tileName)) {
                        progresses.add(progress);
                        break;
                    }
                }
            }
            groupsOfProgresses.add(progresses);
        }
        if(competitionConfiguration.isBlackoutBonusEnabled()) {
            List<TileProgress> blackoutGroup = new ArrayList<TileProgress>();
            blackoutGroup.addAll(team.getTileProgresses());
            groupsOfProgresses.add(blackoutGroup);
        }
        
        // Sum up points from tile completions
        int pointsFromTiles = 0;
        int[] pointsPerTier = competitionConfiguration.getEventPointsPerTier();
        for(TileProgress progress : team.getTileProgresses()) {
            if(progress.getTier() == 0) {
                continue;
            }
            int indexForPoints = progress.getTier() - 1;
            if(indexForPoints > pointsPerTier.length - 1) {
                indexForPoints = pointsPerTier.length - 1;
            }
            for(int i = 0; i <= indexForPoints; i++) {
                pointsFromTiles += pointsPerTier[i];
            }
        }
        scoreboard.setEventPointsFromTiles(pointsFromTiles);
        
        // Sum up points from group completions
        int pointsFromGroups = 0;
        for(List<TileProgress> groupOfProgresses : groupsOfProgresses) {
            // The tier of the group is the tier of the lowest tile in the group
            int lowestTier = groupOfProgresses.get(0).getTier();
            for(TileProgress progress: groupOfProgresses) {
                if(progress.getTier() < lowestTier) {
                    lowestTier = progress.getTier();
                }
            }
            if(lowestTier == 0) {
                continue;
            }
            int indexForPoints = lowestTier - 1;
            if(indexForPoints > pointsPerTier.length - 1) {
                indexForPoints = pointsPerTier.length - 1;
            }
            for(int i = 0; i <= indexForPoints; i++) {
                pointsFromGroups += pointsPerTier[i];
            }
        }
        scoreboard.setEventPointsFromGroups(pointsFromGroups);
        
        // Sum up points from normal collection log items - This is for UNIQUE items only
        Set<CollectionLogItem> uniqueItemsCompleted = new HashSet<CollectionLogItem>(); // Automatically removes duplicates
        for(Player player : team.getPlayers()) {
            for(CollectionLogCompletion completion : player.getCollectionLogCompletions()) {
                if(completion.getItem().getType() == CollectionLogItem.Type.NORMAL) {                    
                    uniqueItemsCompleted.add(completion.getItem());
                }
            }
        }
        int pointsFromClog = 0;
        for(CollectionLogItem item : uniqueItemsCompleted) {
            pointsFromClog += item.getPoints();
        }
        
        // Sum up points from pets - Dupes count here
        int pointsFromPets = 0;
        int petPointIndex = 0;
        for(Player player : team.getPlayers()) {
            for(CollectionLogCompletion completion : player.getCollectionLogCompletions()) {
                if(completion.getItem().getType() == CollectionLogItem.Type.PET) {
                    pointsFromPets += competitionConfiguration.getPetPointValues()[petPointIndex];
                    if(petPointIndex + 1 < competitionConfiguration.getPetPointValues().length) {
                        petPointIndex++;
                    }
                }
            }
        }
        
        // Sum up points from jars - Dupes count here
        int pointsFromJars = 0;
        int jarPointIndex = 0;
        for(Player player : team.getPlayers()) {
            for(CollectionLogCompletion completion : player.getCollectionLogCompletions()) {
                if(completion.getItem().getType() == CollectionLogItem.Type.JAR) {
                    pointsFromJars += competitionConfiguration.getJarPointValues()[jarPointIndex];
                    if(jarPointIndex + 1 < competitionConfiguration.getJarPointValues().length) {
                        jarPointIndex++;
                    }
                }
            }
        }
        
        scoreboard.setEventPointsFromCollectionLogItems(pointsFromClog + pointsFromPets + pointsFromJars);
        Map<String, Integer> finalTiers = getTiers(team);
        sendDiscordAnnouncements(team, initialTiers, finalTiers);
    }
    
    /**
     * Updates the points, tier, and percentage to next tier on a tile progress
     * @param tileProgress
     */
    private void updateTileProgress(TileProgress tileProgress) {
        Team team = tileProgress.getTeam();
        Tile tile = tileProgress.getTile();
        Set<ContributionMethod> methods = tile.getContributionMethods();
        int tilePointsContributed = 0;
        for(ContributionMethod method : methods) {
            for(Player player : team.getPlayers()) {
                for(Contribution contribution : player.getContributions()) {
                    if(contribution.getContributionMethod().equals(method)) {
                        if(method.getDiminishedThreshold() != -1 && contribution.getUnitsContributed() > method.getDiminishedThreshold()) {
                            int pointsUntilThreshold = method.getDiminishedThreshold() * method.getTilePointsPerContribution();
                            int pointsAfterThreshold = (int) ((contribution.getUnitsContributed() - method.getDiminishedThreshold()) * method.getTilePointsPerContribution() * method.getDiminishedMultiplier());
                            tilePointsContributed += pointsUntilThreshold + pointsAfterThreshold;
                        } else {                            
                            tilePointsContributed += contribution.getUnitsContributed() * method.getTilePointsPerContribution();
                        }
                        break;
                    }
                }
            }
        }
        tileProgress.setPoints(tilePointsContributed);
        int tier = tilePointsContributed / tile.getPointsPerTier();
        int tierCap = competitionConfiguration.getTierCap();
        if(tierCap != -1 && tier > tierCap) {
            tier = tierCap;
        }
        int pointsTowardsNextTier = tilePointsContributed % tile.getPointsPerTier();
        if(tierCap != -1 && tier == tierCap) {
            pointsTowardsNextTier = 0;
        }
        tileProgress.setTier(tier);
        tileProgress.setPercentageToNextTier((double) pointsTowardsNextTier / (double) tile.getPointsPerTier());
    }
    
    /**
     * Sets clog points, pvm points, skilling points, other points, and total points on player scoreboard
     * @param player
     */
    private void calculatePlayerScoreboard(Player player) {
        PlayerScoreboard scoreboard = playerScoreboardRepository.findByPlayerId(player.getId()).get();
        
        // Add up all clog item points
        int clogPoints = 0;
        for(CollectionLogCompletion completion : player.getCollectionLogCompletions()) {
            clogPoints += completion.getItem().getPoints();
        }
        scoreboard.setCollectionLogPoints(clogPoints);
        
        // Add up all event points gained from PVM contribution methods
        double pvmPoints = 0.0;
        for(Contribution contribution : player.getContributions()) {
            if(contribution.getContributionMethod().getContributionMethodCategory() == ContributionMethod.Category.PVM) {
                int unitsContributed = contribution.getUnitsContributed();
                int tilePoints = unitsContributed * contribution.getContributionMethod().getTilePointsPerContribution();
                double tilePercent = (double) tilePoints / (double) contribution.getContributionMethod().getTile().getPointsPerTier();
                double eventPoints = (double) competitionConfiguration.getEventPointsPerTier()[0] * tilePercent; // Assume event points from t1 completion
                pvmPoints += eventPoints;
            }
        }
        scoreboard.setPvmTileContribution(pvmPoints);
        
        double skillingPoints = 0.0;
        for(Contribution contribution : player.getContributions()) {
            if(contribution.getContributionMethod().getContributionMethodCategory() == ContributionMethod.Category.SKILLING) {
                int unitsContributed = contribution.getUnitsContributed();
                int tilePoints = unitsContributed * contribution.getContributionMethod().getTilePointsPerContribution();
                double tilePercent = (double) tilePoints / (double) contribution.getContributionMethod().getTile().getPointsPerTier();                
                double eventPoints = (double) competitionConfiguration.getEventPointsPerTier()[0] * tilePercent; // Assume event points from t1 completion
                skillingPoints += eventPoints;
            }
        }
        scoreboard.setSkillingTitleContribution(skillingPoints);
        
        double otherPoints = 0.0;
        for(Contribution contribution : player.getContributions()) {
            if(contribution.getContributionMethod().getContributionMethodCategory() == ContributionMethod.Category.OTHER) {
                int unitsContributed = contribution.getUnitsContributed();
                int tilePoints = unitsContributed * contribution.getContributionMethod().getTilePointsPerContribution();
                double tilePercent = (double) tilePoints / (double) contribution.getContributionMethod().getTile().getPointsPerTier();
                double eventPoints = (double) competitionConfiguration.getEventPointsPerTier()[0] * tilePercent; // Assume event points from t1 completion
                otherPoints += eventPoints;
            }
        }
        scoreboard.setOtherTileContribution(otherPoints);
        
        scoreboard.setTotalTileContribution(pvmPoints + skillingPoints + otherPoints);
    }
    
    /**
     * Updates event points from records and record leaderboard entries for all team scoreboards
     */
    private void calculateAllRecords() {
        // Assemble a leaderboard for each record
        Map<Record, List<RecordCompletion>> completionLeaderboards = new HashMap<Record, List<RecordCompletion>>();
        for(Record record : recordRepository.findAll()) {            
            List<RecordCompletion> leaderboard = new ArrayList<RecordCompletion>();
            for(Team team : teamRepository.findAll()) {
                if(team.getName().equals(competitionConfiguration.getWaitlistTeamName())) {
                    continue; // Waitlist team isn't on the leaderboard
                }
                RecordCompletion bestRecord = team.getBestRecordCompletion(record);
                if(bestRecord != null) {
                    leaderboard.add(bestRecord);
                }
            }
            leaderboard.sort((RecordCompletion c1, RecordCompletion c2) -> c2.getValue() - c1.getValue()); // Descending sort
            completionLeaderboards.put(record, leaderboard);
        }
        
        for(Team team : teamRepository.findAll()) {
            if(team.getName().equals(competitionConfiguration.getWaitlistTeamName())) {
                continue; // Waitlist team isn't on the leaderboard
            }
            // Assemble a list of point values for each record
            List<Integer> recordPoints = new ArrayList<Integer>();
            for(Record record : recordRepository.findAll()) {
                RecordCompletion teamCompletion = team.getBestRecordCompletion(record);
                if(teamCompletion == null) { // Need to reset the leaderboard entry in case their last record completion was deleted
                    RecordLeaderboardEntry leaderboardEntry = team.getScoreboard().getRecordLeaderboardEntry(record);
                    leaderboardEntry.setPlace(-1);
                    leaderboardEntry.setPlayerName(null);
                    leaderboardEntry.setPoints(-1);
                    leaderboardEntry.setValue(-1);
                    continue;
                }
                List<RecordCompletion> leaderboard = completionLeaderboards.get(record);
                RecordCompletion firstPlaceCompletion = leaderboard.get(0);
                if(teamCompletion.getValue() < firstPlaceCompletion.getValue() * competitionConfiguration.getRecordDistanceCutoffPercentage()) {
                    continue;
                }
                int place = leaderboard.indexOf(teamCompletion) + 1;
                int pointsFromPlace = competitionConfiguration.getRecordPlacePoints() - ((place - 1) * competitionConfiguration.getRecordPlacePointsFalloff());
                int distanceFromFirstPlace = firstPlaceCompletion.getValue() - teamCompletion.getValue();
                int pointsFromDistance = (int) ((double) competitionConfiguration.getRecordDistancePoints() * (1.0 - ((double)distanceFromFirstPlace / ((double) firstPlaceCompletion.getValue() * competitionConfiguration.getRecordDistanceCutoffPercentage()))));
                int totalPoints = pointsFromPlace + pointsFromDistance;
                recordPoints.add(totalPoints);
                RecordLeaderboardEntry leaderboardEntry = team.getScoreboard().getRecordLeaderboardEntry(record);
                leaderboardEntry.setPlace(place);
                leaderboardEntry.setPlayerName(teamCompletion.getPlayer().getRsn());
                leaderboardEntry.setPoints(totalPoints);
                leaderboardEntry.setValue(teamCompletion.getValue());
            }
            
            // Take the top X point values based on configuration
            Collections.sort(recordPoints, Collections.reverseOrder()); // Descending sort
            int valuesUsed = 0;
            int pointsFromRecords = 0;
            for(Integer points : recordPoints) {
                if(valuesUsed == competitionConfiguration.getTopNumberOfRecordsToUse()) {
                    break;
                }
                pointsFromRecords += points;
                valuesUsed++;
            }
            team.getScoreboard().setEventPointsFromRecords(pointsFromRecords);
        }
    }
        
    /**
     * Updates event points from challenges for all team scoreboards
     */
    private void calculateAllChallenges() {
        // Assemble a leaderboard for each record
        Map<Challenge, List<ChallengeCompletion>> completionLeaderboards = new HashMap<Challenge, List<ChallengeCompletion>>();
        for(Challenge challenge : challengeRepository.findAll()) {
            List<ChallengeCompletion> leaderboard = new ArrayList<ChallengeCompletion>();
            for(Team team : teamRepository.findAll()) {
                if(team.getName().equals(competitionConfiguration.getWaitlistTeamName())) {
                    continue; // Waitlist team isn't on the leaderboard
                }
                ChallengeCompletion completion = team.getChallengeCompletion(challenge);
                if(completion != null && completion.isComplete()) {
                    leaderboard.add(completion);
                }
            }
            leaderboard.sort((ChallengeCompletion c1, ChallengeCompletion c2) -> c1.getSeconds() - c2.getSeconds() < 0 ? -1 : 1); // Ascending sort
            completionLeaderboards.put(challenge, leaderboard);
        }
        
        for(Team team : teamRepository.findAll()) {
            if(team.getName().equals(competitionConfiguration.getWaitlistTeamName())) {
                continue; // Waitlist team isn't on the leaderboard
            }
            // Assemble a list of point values for each record
            List<Integer> challengePoints = new ArrayList<Integer>();
            for(Challenge challenge : challengeRepository.findAll()) {
                ChallengeCompletion teamCompletion = team.getChallengeCompletion(challenge);
                if(teamCompletion == null || !teamCompletion.isComplete()) {
                    ChallengeLeaderboardEntry leaderboardEntry = team.getScoreboard().getChallengeLeaderboardEntry(challenge);
                    leaderboardEntry.setPlace(-1);
                    leaderboardEntry.setPlayerNames(null);
                    leaderboardEntry.setPoints(-1);
                    leaderboardEntry.setSeconds(-1.0);
                    continue;
                }
                List<ChallengeCompletion> leaderboard = completionLeaderboards.get(challenge);
                ChallengeCompletion firstPlaceCompletion = leaderboard.get(0);
                if(teamCompletion.getSeconds() > firstPlaceCompletion.getSeconds() * competitionConfiguration.getChallengeDistanceCutoffPercentage()) {
                    continue;
                }
                int place = leaderboard.indexOf(teamCompletion) + 1;
                int pointsFromPlace = competitionConfiguration.getChallengePlacePoints() - ((place - 1) * competitionConfiguration.getChallengePlacePointsFalloff());
                double distanceFromFirstPlace = teamCompletion.getSeconds() - firstPlaceCompletion.getSeconds();
                int pointsFromDistance = (int) (competitionConfiguration.getRecordDistancePoints() * (1 - (distanceFromFirstPlace / ((firstPlaceCompletion.getSeconds() * competitionConfiguration.getChallengeDistanceCutoffPercentage()) - firstPlaceCompletion.getSeconds()))));
                int totalPoints = pointsFromPlace + pointsFromDistance;
                challengePoints.add(totalPoints);
                List<String> playerNames = new ArrayList<String>();
                for(Player player : teamCompletion.getPlayers()) {
                    playerNames.add(player.getRsn());
                }
                ChallengeLeaderboardEntry leaderboardEntry = team.getScoreboard().getChallengeLeaderboardEntry(challenge);
                leaderboardEntry.setPlayerNames(String.join(", ", playerNames));
                leaderboardEntry.setPoints(totalPoints);
                leaderboardEntry.setSeconds(teamCompletion.getSeconds());
                leaderboardEntry.setPlace(place);
            }
            
            // Take the top X point values based on configuration
            Collections.sort(challengePoints, Collections.reverseOrder()); // Descending sort
            int valuesUsed = 0;
            int pointsFromChallenges = 0;
            for(Integer points : challengePoints) {
                if(valuesUsed == competitionConfiguration.getTopNumberOfChallengesToUse()) {
                    break;
                }
                pointsFromChallenges += points;
            }
            team.getScoreboard().setEventPointsFromChallenges(pointsFromChallenges);
        }
    }
    
    /**
     * Updates total event points for all team scoreboards
     */
    private void calculateAllTotalPoints() {
        for(Team team : teamRepository.findAll()) {
            TeamScoreboard scoreboard = team.getScoreboard();
            int points = 0;
            points += scoreboard.getEventPointsFromTiles();
            points += scoreboard.getEventPointsFromGroups();
            points += scoreboard.getEventPointsFromCollectionLogItems();
            points += scoreboard.getEventPointsFromRecords();
            points += scoreboard.getEventPointsFromChallenges();
            scoreboard.setEventPoints(points);
        }
    }
    
    /**
     * Returns a map of tier/group names and their associated tiers. Used to find tier-ups to send Discord notifications.
     * @param scoreboard
     * @return
     */
    private Map<String, Integer> getTiers(Team team) {
        Map<String, Integer> tiers = new HashMap<String, Integer>();
        
        // Individual tiles
        for(TileProgress tileProgress : team.getTileProgresses()) {
            tiers.put(tileProgress.getTile().getName(), tileProgress.getTier());
        }
        
        // Groups
        for(TileGroupConfig groupConfig : competitionConfiguration.getTileGroups()) {
            List<TileProgress> progresses = new ArrayList<TileProgress>();
            for(String tileName : groupConfig.getTileNames()) {
                for(TileProgress progress : team.getTileProgresses()) {
                    if(progress.getTile().getName().equals(tileName)) {
                        progresses.add(progress);
                        break;
                    }
                }
            }
            // The tier of the group is the tier of the lowest tile in the group
            int lowestTier = progresses.get(0).getTier();
            for(TileProgress progress: progresses) {
                if(progress.getTier() < lowestTier) {
                    lowestTier = progress.getTier();
                }
            }
            tiers.put(groupConfig.getName(), lowestTier);
        }
        
        // Blackout
        if(competitionConfiguration.isBlackoutBonusEnabled()) {
            List<TileProgress> progresses = new ArrayList<TileProgress>();
            progresses.addAll(team.getTileProgresses());
            // The tier of the group is the tier of the lowest tile in the group
            int lowestTier = progresses.get(0).getTier();
            for(TileProgress progress: progresses) {
                if(progress.getTier() < lowestTier) {
                    lowestTier = progress.getTier();
                }
            }
            tiers.put("Blackout", lowestTier);
        }
        
        return tiers;
    }
    
    /**
     * Sends a message to the team's tier-ups channel for each tier-up
     * @param team
     * @param initialTiers
     * @param finalTiers
     */
    private void sendDiscordAnnouncements(Team team, Map<String, Integer> initialTiers, Map<String, Integer> finalTiers) {
        Guild guild = discordClient.getGuildById(guildId);
        String tierUpsTextChannelName = team.getAbbreviation().toLowerCase() + "-tier-ups";
        if(guild.getTextChannelsByName(tierUpsTextChannelName, false).isEmpty()) { // Discord server has not been set up
            return;
        }
        TextChannel tierUpsTextChannel = guild.getTextChannelsByName(tierUpsTextChannelName, false).get(0);
        
        // Individual tiles
        List<String> tileNames = new ArrayList<String>();
        for(Tile tile : tileRepository.findByOrderByIdAsc()) {
            tileNames.add(tile.getName());
        }
        for(String tileName : tileNames) {
            if(finalTiers.get(tileName) > initialTiers.get(tileName)) {
                tierUpsTextChannel.sendMessage("Congratuluations! Your team has reached tier " + finalTiers.get(tileName) + " in " + tileName + "!").complete();
            }
        }
        
        // Tile groups
        List<String> groupNames = new ArrayList<String>();
        for(TileGroupConfig tileGroupConfig : competitionConfiguration.getTileGroups()) {
            groupNames.add(tileGroupConfig.getName());
        }
        for(String groupName : groupNames) {
            if(finalTiers.get(groupName) > initialTiers.get(groupName)) {
                tierUpsTextChannel.sendMessage("Congratuluations! Your team has reached tier " + finalTiers.get(groupName) + " in " + groupName + "!").complete();
            }
        }
        
        // Blackout
        if(finalTiers.get("Blackout") > initialTiers.get("Blackout")) {
            tierUpsTextChannel.sendMessage("Congratuluations! Your team has reached a tier " + finalTiers.get("Blackout") + " blackout!").complete();
        }
    }

}
