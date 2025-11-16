package org.uimshowdown.bingo.configuration;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.CollectionLogItem;
import org.uimshowdown.bingo.models.ContributionMethod;

@Configuration
@ConfigurationProperties("competition")
public class CompetitionConfiguration {
    
    private String eventName;
    
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private Date startDatetime;
    
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private Date endDatetime;
    
    private int templeCompetitionID;
    
    private String waitlistTeamName;
    
    private int ehtPerTier;
    
    private int[] eventPointsPerTier;
    
    private int tierCap;
    
    private boolean blackoutBonusEnabled;
    
    private int topNumberOfRecordsToUse;
    
    private int recordPlacePoints;
    
    private int recordPlacePointsFalloff;
    
    private int recordDistancePoints;
    
    private double recordDistanceCutoffPercentage;
    
    private int topNumberOfChallengesToUse;
    
    private int challengePlacePoints;
    
    private int challengePlacePointsFalloff;
    
    private int challengeDistancePoints;
    
    private double challengeDistanceCutoffPercentage;
    
    private List<TeamConfig> teams;
    
    private List<TileConfig> tiles;
    
    private List<TileGroupConfig> tileGroups;
    
    private List<ChallengeConfig> challenges;
    
    private List<RecordConfig> records;
    
    private List<CollectionLogItemConfig> collectionLogItems;
    
    private List<String> pets;
    
    private List<String> jars;
    
    private double maxRoundingErrorForContributionPoints;
    
    private int sigFigsForTilePointsPerTier;
    
    private int sigFigsForContributionPoints;
    
    private Map<String, Integer> slayerXPPenalties;
    
    private int[] petPointValues;
    
    private int[] jarPointValues;
    
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public int getTempleCompetitionID() {
        return templeCompetitionID;
    }

    public void setTempleCompetitionID(int templeCompetitionID) {
        this.templeCompetitionID = templeCompetitionID;
    }

    public String getWaitlistTeamName() {
        return waitlistTeamName;
    }

    public void setWaitlistTeamName(String waitlistTeamName) {
        this.waitlistTeamName = waitlistTeamName;
    }

    public int getEhtPerTier() {
        return ehtPerTier;
    }

    public void setEhtPerTier(int ehtPerTier) {
        this.ehtPerTier = ehtPerTier;
    }

    public int[] getEventPointsPerTier() {
        return eventPointsPerTier;
    }

    public void setEventPointsPerTier(int[] eventPointsPerTier) {
        this.eventPointsPerTier = eventPointsPerTier;
    }

    public int getTierCap() {
        return tierCap;
    }

    public void setTierCap(int tierCap) {
        this.tierCap = tierCap;
    }

    public boolean isBlackoutBonusEnabled() {
        return blackoutBonusEnabled;
    }

    public void setBlackoutBonusEnabled(boolean blackoutBonusEnabled) {
        this.blackoutBonusEnabled = blackoutBonusEnabled;
    }

    public int getTopNumberOfRecordsToUse() {
        return topNumberOfRecordsToUse;
    }

    public void setTopNumberOfRecordsToUse(int topNumberOfRecordsToUse) {
        this.topNumberOfRecordsToUse = topNumberOfRecordsToUse;
    }

    public int getRecordPlacePoints() {
        return recordPlacePoints;
    }

    public void setRecordPlacePoints(int recordPlacePoints) {
        this.recordPlacePoints = recordPlacePoints;
    }

    public int getRecordPlacePointsFalloff() {
        return recordPlacePointsFalloff;
    }

    public void setRecordPlacePointsFalloff(int recordPlacePointsFalloff) {
        this.recordPlacePointsFalloff = recordPlacePointsFalloff;
    }

    public int getRecordDistancePoints() {
        return recordDistancePoints;
    }

    public void setRecordDistancePoints(int recordDistancePoints) {
        this.recordDistancePoints = recordDistancePoints;
    }

    public double getRecordDistanceCutoffPercentage() {
        return recordDistanceCutoffPercentage;
    }

    public void setRecordDistanceCutoffPercentage(double recordDistanceCutoffPercentage) {
        this.recordDistanceCutoffPercentage = recordDistanceCutoffPercentage;
    }

    public int getTopNumberOfChallengesToUse() {
        return topNumberOfChallengesToUse;
    }

    public void setTopNumberOfChallengesToUse(int topNumberOfChallengesToUse) {
        this.topNumberOfChallengesToUse = topNumberOfChallengesToUse;
    }

    public int getChallengePlacePoints() {
        return challengePlacePoints;
    }

    public void setChallengePlacePoints(int challengePlacePoints) {
        this.challengePlacePoints = challengePlacePoints;
    }

    public int getChallengePlacePointsFalloff() {
        return challengePlacePointsFalloff;
    }

    public void setChallengePlacePointsFalloff(int challengePlacePointsFalloff) {
        this.challengePlacePointsFalloff = challengePlacePointsFalloff;
    }

    public int getChallengeDistancePoints() {
        return challengeDistancePoints;
    }

    public void setChallengeDistancePoints(int challengeDistancePoints) {
        this.challengeDistancePoints = challengeDistancePoints;
    }

    public double getChallengeDistanceCutoffPercentage() {
        return challengeDistanceCutoffPercentage;
    }

    public void setChallengeDistanceCutoffPercentage(double challengeDistanceCutoffPercentage) {
        this.challengeDistanceCutoffPercentage = challengeDistanceCutoffPercentage;
    }
    
    public List<TeamConfig> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamConfig> teams) {
        this.teams = teams;
    }

    public List<TileConfig> getTiles() {
        return tiles;
    }

    public void setTiles(List<TileConfig> tiles) {
        this.tiles = tiles;
    }

    public List<TileGroupConfig> getTileGroups() {
        return tileGroups;
    }

    public void setTileGroups(List<TileGroupConfig> tileGroups) {
        this.tileGroups = tileGroups;
    }

    public List<ChallengeConfig> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<ChallengeConfig> challenges) {
        this.challenges = challenges;
    }

    public List<RecordConfig> getRecords() {
        return records;
    }

    public void setRecords(List<RecordConfig> records) {
        this.records = records;
    }

    public List<CollectionLogItemConfig> getCollectionLogItems() {
        return collectionLogItems;
    }

    public void setCollectionLogItems(List<CollectionLogItemConfig> collectionLogItems) {
        this.collectionLogItems = collectionLogItems;
    }
    
    public double getMaxRoundingErrorForContributionPoints() {
        return maxRoundingErrorForContributionPoints;
    }

    public void setMaxRoundingErrorForContributionPoints(double maxRoundingErrorForContributionPoints) {
        this.maxRoundingErrorForContributionPoints = maxRoundingErrorForContributionPoints;
    }

    public int getSigFigsForTilePointsPerTier() {
        return sigFigsForTilePointsPerTier;
    }

    public void setSigFigsForTilePointsPerTier(int sigFigsForTilePointsPerTier) {
        this.sigFigsForTilePointsPerTier = sigFigsForTilePointsPerTier;
    }

    public int getSigFigsForContributionPoints() {
        return sigFigsForContributionPoints;
    }

    public void setSigFigsForContributionPoints(int sigFigsForContributionPoints) {
        this.sigFigsForContributionPoints = sigFigsForContributionPoints;
    }

    public Map<String, Integer> getSlayerXPPenalties() {
        return slayerXPPenalties;
    }

    public void setSlayerXPPenalties(Map<String, Integer> slayerXPPenalties) {
        this.slayerXPPenalties = slayerXPPenalties;
    }

    public int[] getPetPointValues() {
        return petPointValues;
    }

    public void setPetPointValues(int[] petPointValues) {
        this.petPointValues = petPointValues;
    }

    public int[] getJarPointValues() {
        return jarPointValues;
    }

    public void setJarPointValues(int[] jarPointValues) {
        this.jarPointValues = jarPointValues;
    }

    public List<String> getPets() {
        return pets;
    }

    public void setPets(List<String> pets) {
        this.pets = pets;
    }

    public List<String> getJars() {
        return jars;
    }

    public void setJars(List<String> jars) {
        this.jars = jars;
    }
    
    public static class TeamConfig {
        private String name;
        private String abbreviation;
        private String color;
        private List<String> captains;
        
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getAbbreviation() {
            return abbreviation;
        }
        public void setAbbreviation(String abbreviation) {
            this.abbreviation = abbreviation;
        }
        public String getColor() {
            return color;
        }
        public void setColor(String color) {
            this.color = color;
        }
        public List<String> getCaptains() {
            return captains;
        }
        public void setCaptains(List<String> captains) {
            this.captains = captains;
        }
    }

    public static class TileConfig {
        private String name;
        private String abbreviation;
        private double maxRoundingErrorForContributionPoints = -1;
        private int sigFigsForTilePointsPerTier = -1;
        private int sigFigsForContributionPoints = -1;
        private List<ContributionMethodConfig> contributionMethods;
        
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getAbbreviation() {
            return abbreviation;
        }
        public void setAbbreviation(String abbreviation) {
            this.abbreviation = abbreviation;
        }
        public double getMaxRoundingErrorForContributionPoints() {
            return maxRoundingErrorForContributionPoints;
        }
        public void setMaxRoundingErrorForContributionPoints(double maxRoundingErrorForContributionPoints) {
            this.maxRoundingErrorForContributionPoints = maxRoundingErrorForContributionPoints;
        }
        public int getSigFigsForTilePointsPerTier() {
            return sigFigsForTilePointsPerTier;
        }
        public void setSigFigsForTilePointsPerTier(int sigFigsForTilePointsPerTier) {
            this.sigFigsForTilePointsPerTier = sigFigsForTilePointsPerTier;
        }
        public int getSigFigsForContributionPoints() {
            return sigFigsForContributionPoints;
        }
        public void setSigFigsForContributionPoints(int sigFigsForContributionPoints) {
            this.sigFigsForContributionPoints = sigFigsForContributionPoints;
        }
        public List<ContributionMethodConfig> getContributionMethods() {
            return contributionMethods;
        }
        public void setContributionMethods(List<ContributionMethodConfig> contributionMethods) {
            this.contributionMethods = contributionMethods;
        }
    }
    
    public static class ContributionMethodConfig {
        private String name;
        private ContributionMethod.Type type;
        private ContributionMethod.Category category;
        private double ehtRate;
        private String templeID;
        private int diminishedThreshold = -1;
        private double diminishedMultiplier = -1.0;
        private int rankingThreshold = 5;
        private List<ContributionPurchaseItemConfig> purchaseItems;
        
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public ContributionMethod.Type getType() {
            return type;
        }
        public void setType(ContributionMethod.Type type) {
            this.type = type;
        }
        public ContributionMethod.Category getCategory() {
            return category;
        }
        public void setCategory(ContributionMethod.Category category) {
            this.category = category;
        }
        public double getEhtRate() {
            return ehtRate;
        }
        public void setEhtRate(double ehtRate) {
            this.ehtRate = ehtRate;
        }
        public String getTempleID() {
            return templeID;
        }
        public void setTempleID(String templeID) {
            this.templeID = templeID;
        }
        public int getDiminishedThreshold() {
            return diminishedThreshold;
        }
        public void setDiminishedThreshold(int diminishedThreshold) {
            this.diminishedThreshold = diminishedThreshold;
        }
        public double getDiminishedMultiplier() {
            return diminishedMultiplier;
        }
        public void setDiminishedMultiplier(double diminishedMultiplier) {
            this.diminishedMultiplier = diminishedMultiplier;
        }
        public int getRankingThreshold() {
            return rankingThreshold;
        }
        public void setRankingThreshold(int rankingThreshold) {
            this.rankingThreshold = rankingThreshold;
        }
        public List<ContributionPurchaseItemConfig> getPurchaseItems() {
            return purchaseItems;
        }
        public void setPurchaseItems(List<ContributionPurchaseItemConfig> purchaseItems) {
            this.purchaseItems = purchaseItems;
        }
    }
    
    public static class ContributionPurchaseItemConfig {
        private String name;
        private String cost;
        
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getCost() {
            return cost;
        }
        public void setCost(String cost) {
            this.cost = cost;
        }
    }
    
    public static class TileGroupConfig {
        private String name;
        private List<String> tileNames;
        
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public List<String> getTileNames() {
            return tileNames;
        }
        public void setTileNames(List<String> tileNames) {
            this.tileNames = tileNames;
        }
    }
    
    public static class ChallengeConfig {
        private String name;
        private int teamSize;
        private String description;
        private Challenge.Type type;
        private List<String> relayComponents;
        
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getTeamSize() {
            return teamSize;
        }
        public void setTeamSize(int teamSize) {
            this.teamSize = teamSize;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public Challenge.Type getType() {
            return type;
        }
        public void setType(Challenge.Type type) {
            this.type = type;
        }
        public List<String> getRelayComponents() {
            return relayComponents;
        }
        public void setRelayComponents(List<String> relayComponents) {
            this.relayComponents = relayComponents;
        }
    }
    
    public static class RecordConfig {
        private String name;
        private String description;
        private List<HandicapConfig> handicaps;
        
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public List<HandicapConfig> getHandicaps() {
            return handicaps;
        }
        public void setHandicaps(List<HandicapConfig> handicaps) {
            this.handicaps = handicaps;
        }
    }
    
    public static class HandicapConfig {
        private String name;
        private double multiplier;
        
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public double getMultiplier() {
            return multiplier;
        }
        public void setMultiplier(double multiplier) {
            this.multiplier = multiplier;
        }
    }
    
    public static class CollectionLogItemConfig {
        private String name;
        private String description;
        private CollectionLogItem.Type type = CollectionLogItem.Type.NORMAL;
        private int points = 0;
        private List<String> itemOptions;
        
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getPoints() {
            return points;
        }
        public void setPoints(int points) {
            this.points = points;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public List<String> getItemOptions() {
            return itemOptions;
        }
        public void setItemOptions(List<String> itemOptions) {
            this.itemOptions = itemOptions;
        }
        public CollectionLogItem.Type getType() {
            return type;
        }
        public void setType(CollectionLogItem.Type type) {
            this.type = type;
        }
    }
    
}
