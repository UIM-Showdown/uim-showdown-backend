package org.uimshowdown.bingo.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration.ChallengeConfig;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration.ContributionMethodConfig;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration.HandicapConfig;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration.CollectionLogItemConfig;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration.RecordConfig;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration.TileConfig;
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.ChallengeCompletion;
import org.uimshowdown.bingo.models.ChallengeLeaderboardEntry;
import org.uimshowdown.bingo.models.ChallengeRelayComponent;
import org.uimshowdown.bingo.models.CollectionLogItem;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.ItemOption;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.PlayerScoreboard;
import org.uimshowdown.bingo.models.Record;
import org.uimshowdown.bingo.models.RecordHandicap;
import org.uimshowdown.bingo.models.RecordLeaderboardEntry;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.TeamScoreboard;
import org.uimshowdown.bingo.models.Tile;
import org.uimshowdown.bingo.models.TileProgress;
import org.uimshowdown.bingo.repositories.ChallengeCompletionRepository;
import org.uimshowdown.bingo.repositories.ChallengeRelayComponentRepository;
import org.uimshowdown.bingo.repositories.ChallengeRepository;
import org.uimshowdown.bingo.repositories.CollectionLogCompletionRepository;
import org.uimshowdown.bingo.repositories.CollectionLogItemRepository;
import org.uimshowdown.bingo.repositories.ContributionMethodRepository;
import org.uimshowdown.bingo.repositories.ContributionRepository;
import org.uimshowdown.bingo.repositories.PlayerChallengeCompletionRepository;
import org.uimshowdown.bingo.repositories.PlayerRepository;
import org.uimshowdown.bingo.repositories.PlayerScoreboardRepository;
import org.uimshowdown.bingo.repositories.RecordCompletionRepository;
import org.uimshowdown.bingo.repositories.RecordHandicapRepository;
import org.uimshowdown.bingo.repositories.RecordRepository;
import org.uimshowdown.bingo.repositories.SubmissionRepository;
import org.uimshowdown.bingo.repositories.SubmissionScreenshotUrlRepository;
import org.uimshowdown.bingo.repositories.TeamRepository;
import org.uimshowdown.bingo.repositories.TeamScoreboardRepository;
import org.uimshowdown.bingo.repositories.TileProgressRepository;
import org.uimshowdown.bingo.repositories.TileRepository;

@Component
public class EventDataInitializationService {
    
    @Autowired
    private CompetitionConfiguration competitionConfiguration;
    
    @Autowired ChallengeCompletionRepository challengeCompletionRepository;
    @Autowired ChallengeRelayComponentRepository challengeRelayComponentRepository;
    @Autowired ChallengeRepository challengeRepository;
    @Autowired CollectionLogCompletionRepository collectionLogCompletionRepository;
    @Autowired CollectionLogItemRepository collectionLogItemRepository;
    @Autowired ContributionMethodRepository contributionMethodRepository;
    @Autowired ContributionRepository contributionRepository;
    @Autowired PlayerChallengeCompletionRepository playerChallengeCompletionRepository;
    @Autowired PlayerRepository playerRepository;
    @Autowired PlayerScoreboardRepository playerScoreboardRepository;
    @Autowired RecordCompletionRepository recordCompletionRepository;
    @Autowired RecordHandicapRepository recordHandicapRepository;
    @Autowired RecordRepository recordRepository;
    @Autowired SubmissionRepository submissionRepository;
    @Autowired SubmissionScreenshotUrlRepository submissionScreenshotUrlRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired TeamScoreboardRepository teamScoreboardRepository;
    @Autowired TileProgressRepository tileProgressRepository;
    @Autowired TileRepository tileRepository;
    
    /**
     * Resets all DB data to the "blank" state defined by the config file. THIS DELETES EVERYTHING, so 
     * use it wisely.
     */
    public void initializeCompetition() {
        dropAllData();
        initializeTiles();
        initializeChallenges();
        initializeRecords();
        initializeCollectionLogItems();
    }
    
    /**
     * Deletes all rows from the DB across all tables.
     */
    private void dropAllData() {
        challengeCompletionRepository.deleteAll();
        collectionLogCompletionRepository.deleteAll();
        contributionRepository.deleteAll();
        playerChallengeCompletionRepository.deleteAll();
        playerRepository.deleteAll();
        playerScoreboardRepository.deleteAll();
        recordCompletionRepository.deleteAll();
        recordHandicapRepository.deleteAll();
        submissionRepository.deleteAll();
        submissionScreenshotUrlRepository.deleteAll();
        teamRepository.deleteAll();
        teamScoreboardRepository.deleteAll();
        tileProgressRepository.deleteAll();
        tileRepository.deleteAll();
        contributionMethodRepository.deleteAll();
        challengeRelayComponentRepository.deleteAll();
        challengeRepository.deleteAll();
        recordRepository.deleteAll();
        collectionLogItemRepository.deleteAll();
    }
    
    /**
     * Creates all rows in the "tiles" and "contribution_methods" tables based on the config file
     */
    private void initializeTiles() {
        for(TileConfig tileConfig : competitionConfiguration.getTiles()) {
            Tile tile = new Tile();
            tile.setName(tileConfig.getName());
            tile.setAbbreviation(tileConfig.getAbbreviation());
            Set<ContributionMethod> contributionMethods = new HashSet<ContributionMethod>();
            for(ContributionMethodConfig methodConfig : tileConfig.getContributionMethods()) {
                ContributionMethod method = new ContributionMethod();
                method.setName(methodConfig.getName());
                method.setContributionMethodType(methodConfig.getType());
                method.setContributionMethodCategory(methodConfig.getCategory());
                method.setEhtRate(methodConfig.getEhtRate());
                method.setTempleId(methodConfig.getTempleID());
                method.setTile(tile);
                method.setDiminishedThreshold(methodConfig.getDiminishedThreshold());
                method.setDiminishedMultiplier(methodConfig.getDiminishedMultiplier());
                contributionMethods.add(method);
            }
            tile.setContributionMethods(contributionMethods);
            initializeTilePoints(tile, tileConfig);
            tileRepository.save(tile);
        }
    }
    
    /**
     * Creates all rows in the "challenges" and "challenge_relay_components" tables based on the config file
     */
    private void initializeChallenges() {
        for(ChallengeConfig challengeConfig : competitionConfiguration.getChallenges()) {
            Challenge challenge = new Challenge();
            challenge.setName(challengeConfig.getName());
            challenge.setTeamSize(challengeConfig.getTeamSize());
            challenge.setDescription(challengeConfig.getDescription());
            challenge.setType(challengeConfig.getType());
            Set<ChallengeRelayComponent> relayComponents = new HashSet<ChallengeRelayComponent>();
            if(challengeConfig.getType().equals(Challenge.Type.RELAY)) {                
                for(String relayComponentName : challengeConfig.getRelayComponents()) {
                    ChallengeRelayComponent relayComponent = new ChallengeRelayComponent();
                    relayComponent.setName(relayComponentName);
                    relayComponent.setChallenge(challenge);
                    relayComponents.add(relayComponent);
                }
                challenge.setRelayComponents(relayComponents);
            }
            challengeRepository.save(challenge);
        }
    }
    
    /**
     * Creates all rows in the "records" and "record_handicaps" tables based on the config file
     */
    private void initializeRecords() {
        for(RecordConfig recordConfig : competitionConfiguration.getRecords()) {
            Record record = new Record();
            record.setSkill(recordConfig.getSkill());
            record.setDescription(recordConfig.getDescription());
            Set<RecordHandicap> recordHandicaps = new HashSet<RecordHandicap>();
            if(recordConfig.getHandicaps() != null) {                
                for(HandicapConfig handicapConfig : recordConfig.getHandicaps()) {
                    RecordHandicap recordHandicap = new RecordHandicap();
                    recordHandicap.setName(handicapConfig.getName());
                    recordHandicap.setMultiplier(handicapConfig.getMultiplier());
                    recordHandicap.setRecord(record);
                    recordHandicaps.add(recordHandicap);
                }
                record.setHandicaps(recordHandicaps);
            }
            recordRepository.save(record);
        }
    }
    
    /**
     * Creates all rows in the "collection_log_items" and "item_options" tables 
     * based on the config file
     */
    private void initializeCollectionLogItems() {
        for(CollectionLogItemConfig itemConfig : competitionConfiguration.getCollectionLogItems()) {
            CollectionLogItem item = new CollectionLogItem();
            item.setName(itemConfig.getName());
            item.setDescription(itemConfig.getDescription());
            item.setPoints(itemConfig.getPoints());
            item.setType(itemConfig.getType());
            Set<ItemOption> itemOptions = new HashSet<ItemOption>();
            if(itemConfig.getItemOptions() != null) {                    
                for(String optionName : itemConfig.getItemOptions()) {
                    ItemOption itemOption = new ItemOption();
                    itemOption.setName(optionName);
                    itemOption.setItem(item);
                    itemOptions.add(itemOption);
                }
            }
            item.setItemOptions(itemOptions);
            collectionLogItemRepository.save(item);
        }
        for(String pet : competitionConfiguration.getPets()) {
            CollectionLogItem item = new CollectionLogItem();
            item.setName(pet);
            item.setType(CollectionLogItem.Type.PET);
            collectionLogItemRepository.save(item);
        }
        for(String jar : competitionConfiguration.getJars()) {
            CollectionLogItem item = new CollectionLogItem();
            item.setName(jar);
            item.setType(CollectionLogItem.Type.JAR);
            collectionLogItemRepository.save(item);
        }
    }
    
    /**
     * Helper method to round a double to the nearest int with the given number of significant figures.
     * 
     * For example:
     * roundToSigFigs(123.456, 2) returns 120
     * roundToSigFigs(123.456, 1) returns 100
     * roundToSigFigs(789.000, 2) returns 790
     * @param valueToRound
     * @param sigFigs
     * @return
     */
    private int roundToSigFigs(double valueToRound, int sigFigs) {
        double intermediate = valueToRound / Math.pow(10, Math.floor(Math.log10(Math.abs(valueToRound))) - (sigFigs - 1));
        intermediate = Math.round(intermediate);
        int result = (int) Math.round(intermediate * Math.pow(10, Math.floor(Math.log10(Math.abs(valueToRound))) - (sigFigs-1)));
        return result;
    }
    
    /**
     * Calculates and sets the points per tier on the given tile, as well as the tile points per contribution on 
     * all included contribution methods
     * @param tile
     */
    private void initializeTilePoints(Tile tile, TileConfig tileConfig) {
        
        // Load config stuff
        int ehtPerTier = competitionConfiguration.getEhtPerTier();
        double maxRoundingErrorForContributionPoints = competitionConfiguration.getMaxRoundingErrorForContributionPoints();
        int sigFigsForTilePointsPerTier = competitionConfiguration.getSigFigsForTilePointsPerTier();
        int sigFigsForContributionPoints = competitionConfiguration.getSigFigsForContributionPoints();
        
        // Override with tile-specific values if needed
        if(tileConfig.getMaxRoundingErrorForContributionPoints() > 0) {
            maxRoundingErrorForContributionPoints = tileConfig.getMaxRoundingErrorForContributionPoints();
        }
        if(tileConfig.getSigFigsForTilePointsPerTier() > 0) {
            sigFigsForTilePointsPerTier = tileConfig.getSigFigsForTilePointsPerTier();
        }
        if(tileConfig.getSigFigsForContributionPoints() > 0) {
            sigFigsForContributionPoints = tileConfig.getSigFigsForContributionPoints();
        }
        
        // Figure out the max contribution units per tier
        double maxUnitsPerTier = 0;
        for(ContributionMethod method : tile.getContributionMethods()) {
            double unitsPerTier = method.getEhtRate() * ehtPerTier;
            if(unitsPerTier > maxUnitsPerTier) {
                maxUnitsPerTier = unitsPerTier;
            }
        }
        
        // Calculate tile point multipliers
        Map<String, Double> tilePointMultipliers = new HashMap<String, Double>();
        for(ContributionMethod method : tile.getContributionMethods()) {
            tilePointMultipliers.put(method.getName(), maxUnitsPerTier / (method.getEhtRate() * ehtPerTier));
        }
        
        // Find the lowest multiplier that keeps the rounding error under the configured maximum when rounded to an integer with the configured number of sig figs
        for(int i = 1; i < 9999; i++) {
            double maxRoundingError = 0;
            
            // Establish tile points per tier with this multiplier
            ContributionMethod firstMethod = (ContributionMethod) tile.getContributionMethods().toArray()[0]; // The following gets the same result for any contribution method
            double unitsPerTier = firstMethod.getEhtRate() * ehtPerTier;
            double tilePointsPerContribution = tilePointMultipliers.get(firstMethod.getName()) * i;
            int tilePointsPerTier = roundToSigFigs(unitsPerTier * tilePointsPerContribution, sigFigsForTilePointsPerTier);
            tile.setPointsPerTier(tilePointsPerTier);
            
            // Set all the tile contribution points and find the highest rounding error
            for(ContributionMethod method : tile.getContributionMethods()) {
                // Round and set the tile contribution points
                double truePointsPerContribution = tilePointMultipliers.get(method.getName()) * i;
                int roundedPointsPerContribution = roundToSigFigs((int) Math.round(truePointsPerContribution), sigFigsForContributionPoints);
                method.setTilePointsPerContribution(roundedPointsPerContribution);
                
                // Find the rounding error for this method on this multiplier
                double trueContributionPerTier = method.getEhtRate() * ehtPerTier;
                double roundedContributionPerTier = (double) tilePointsPerTier / roundedPointsPerContribution;
                double roundingError = Math.abs(roundedContributionPerTier - trueContributionPerTier) / trueContributionPerTier;
                if(roundingError > maxRoundingError) {
                    maxRoundingError = roundingError;
                }
            }
            if(maxRoundingError < maxRoundingErrorForContributionPoints) {
                // We found an acceptable multiplier and we're done here
                break;
            }
        }
        
    }
    
    /**
     * Creates a new player and adds it to the team with the given name
     * @param discordName
     * @param rsn
     * @param isCaptain
     * @param teamName
     */
    public void addPlayer(String discordName, String rsn, Boolean isCaptain, String teamName) throws Exception {
        Team team = teamRepository.findByName(teamName).orElse(null);
        if(team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found: " + teamName);
        }
        if(playerRepository.findByDiscordName(discordName).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player already exists with Discord name: " + discordName);
        }
        if(playerRepository.findByRsn(rsn).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player already exists with rsn: " + rsn);
        }
        Player player = new Player();
        player.setCaptainStatus(isCaptain);
        player.setDiscordName(discordName);
        player.setRsn(rsn);
        player.setTeam(team);
        if(team.getPlayers() == null) {
            team.setPlayers(new HashSet<Player>());
        }
        player.setContributions(generateEmptyContributions(player));
        PlayerScoreboard scoreboard = new PlayerScoreboard();
        scoreboard.setPlayer(player);
        player.setScoreboard(scoreboard);
        team.getPlayers().add(player);
        teamRepository.save(team);
    }
    
    /**
     * Creates a new team
     * @param name
     * @param abbreviation
     * @param color
     */
    public void addTeam(String name, String abbreviation, String color) throws Exception {
        if(teamRepository.findByName(name).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team already exists: " + name);
        }
        Team team = new Team();
        team.setName(name);
        team.setAbbreviation(abbreviation);
        team.setColor(color);
        team.setChallengeCompletions(generateEmptyChallengeCompletions(team));
        team.setTileProgresses(generateEmptyTileProgresses(team));
        TeamScoreboard scoreboard = new TeamScoreboard();
        scoreboard.setTeam(team);
        if(!team.getName().equals(competitionConfiguration.getWaitlistTeamName())) { // Waitlist team doesn't get leaderboard entries       
            scoreboard.setRecordLeaderboardEntries(generateEmptyRecordLeaderboardEntries(scoreboard));
            scoreboard.setChallengeLeaderboardEntries(generateEmptyChallengeLeaderboardEntries(scoreboard));
        }
        team.setScoreboard(scoreboard);
        teamRepository.save(team);
    }
    
    /**
     * Returns a set of "empty" (start/end values are 0) contributions for a player, for all contribution methods
     * @param player
     * @return
     */
    private Set<Contribution> generateEmptyContributions(Player player) {
        Set<Contribution> contributions = new HashSet<Contribution>();
        for(ContributionMethod contributionMethod : contributionMethodRepository.findAll()) {
            contributions.add(new Contribution(player, contributionMethod, 0, 0, true));
        }
        return contributions;
    }
    
    private Set<ChallengeCompletion> generateEmptyChallengeCompletions(Team team) {
        Set<ChallengeCompletion> completions = new HashSet<ChallengeCompletion>();
        for(Challenge challenge : challengeRepository.findAll()) {
            ChallengeCompletion completion = new ChallengeCompletion();
            completion.setChallenge(challenge);
            completion.setTeam(team);
            completions.add(completion);
        }
        return completions;
    }
    
    private Set<TileProgress> generateEmptyTileProgresses(Team team) {
        Set<TileProgress> progresses = new HashSet<TileProgress>();
        for(Tile tile : tileRepository.findAll()) {
            TileProgress progress = new TileProgress();
            progress.setTile(tile);
            progress.setTeam(team);
            progresses.add(progress);
        }
        return progresses;
    }
    
    private Set<RecordLeaderboardEntry> generateEmptyRecordLeaderboardEntries(TeamScoreboard scoreboard) {
        Set<RecordLeaderboardEntry> entries = new HashSet<RecordLeaderboardEntry>();
        for(Record record : recordRepository.findAll()) {
            RecordLeaderboardEntry entry = new RecordLeaderboardEntry();
            entry.setPlace(-1);
            entry.setPlayerName(null);
            entry.setPoints(-1);
            entry.setSkill(record.getSkill());
            entry.setTeamScoreboard(scoreboard);
            entry.setValue(-1);
            entries.add(entry);
        }
        return entries;
    }
    
    private Set<ChallengeLeaderboardEntry> generateEmptyChallengeLeaderboardEntries(TeamScoreboard scoreboard) {
        Set<ChallengeLeaderboardEntry> entries = new HashSet<ChallengeLeaderboardEntry>();
        for(Challenge challenge: challengeRepository.findAll()) {
            ChallengeLeaderboardEntry entry = new ChallengeLeaderboardEntry();
            entry.setChallengeName(challenge.getName());
            entry.setPlace(-1);
            entry.setPlayerNames(null);
            entry.setPoints(-1);
            entry.setSeconds(-1.0);
            entry.setTeamScoreboard(scoreboard);
            entries.add(entry);
        }
        return entries;
    }
    
}
