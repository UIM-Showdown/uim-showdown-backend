package org.uimshowdown.bingo.configuration;

import java.util.Date;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.ContributionMethod;

@Configuration
@ConfigurationProperties("competition")
public class CompetitionConfiguration {
	
	private String eventName;
	
	@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date startDatetime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date endDatetime;
	
	private int templeCompetitionID;
	
	private String waitlistTeamName;
	
	private int ehtPerTier;
	
	private int[] eventPointsPerTier;
	
	private int tierCap;
	
	private boolean blackoutBonusEnabled;
	
	private List<TileConfig> tiles;
	
	private List<TileGroupConfig> tileGroups;
	
	private List<ChallengeConfig> challenges;
	
	private List<RecordConfig> records;
	
	private List<CollectionLogGroupConfig> collectionLogGroups;
	
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

	public List<CollectionLogGroupConfig> getCollectionLogGroups() {
		return collectionLogGroups;
	}

	public void setCollectionLogGroups(List<CollectionLogGroupConfig> collectionLogGroups) {
		this.collectionLogGroups = collectionLogGroups;
	}
	
	public static class TileConfig {
		private String name;
		private String abbreviation;
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
		public List<ContributionMethodConfig> getContributionMethods() {
			return contributionMethods;
		}
		public void setContributionMethods(List<ContributionMethodConfig> contributionMethods) {
			this.contributionMethods = contributionMethods;
		}
	}
	
	public static class ContributionMethodConfig {
		private String name;
		private String type;
		private ContributionMethod.Category category;
		private double ehtRate;
		private String templeID;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
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
		private String skill;
		private String description;
		private List<HandicapConfig> handicaps;
		
		public String getSkill() {
			return skill;
		}
		public void setSkill(String skill) {
			this.skill = skill;
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
	
	public static class CollectionLogGroupConfig {
		private String name;
		private String type;
		private int[] bonusPointThresholds;
		private int[] counterPointValues;
		private String description;
		private List<ItemConfig> items;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public int[] getBonusPointThresholds() {
			return bonusPointThresholds;
		}
		public void setBonusPointThresholds(int[] bonusPointThresholds) {
			this.bonusPointThresholds = bonusPointThresholds;
		}
		public int[] getCounterPointValues() {
			return counterPointValues;
		}
		public void setCounterPointValues(int[] counterPointValues) {
			this.counterPointValues = counterPointValues;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public List<ItemConfig> getItems() {
			return items;
		}
		public void setItems(List<ItemConfig> items) {
			this.items = items;
		}
	}

	public static class ItemConfig {
		private String name;
		private int points = -1;
		
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
	}
	
}
