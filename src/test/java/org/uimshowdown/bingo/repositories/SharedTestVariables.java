package org.uimshowdown.bingo.repositories;

import java.sql.Timestamp;
import java.time.Instant;

import org.uimshowdown.bingo.constants.PlayerSkill;
import org.uimshowdown.bingo.enums.ChallengeType;
import org.uimshowdown.bingo.enums.CollectionLogGroupType;
import org.uimshowdown.bingo.enums.ContributionMethodCategory;
import org.uimshowdown.bingo.enums.ContributionMethodType;
import org.uimshowdown.bingo.enums.SubmissionState;
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.ChallengeCompletion;
import org.uimshowdown.bingo.models.ChallengeRelayComponent;
import org.uimshowdown.bingo.models.ChallengeSubmission;
import org.uimshowdown.bingo.models.CollectionLogGroup;
import org.uimshowdown.bingo.models.CollectionLogItem;
import org.uimshowdown.bingo.models.CollectionLogSubmission;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.PlayerChallengeCompletion;
import org.uimshowdown.bingo.models.Record;
import org.uimshowdown.bingo.models.RecordCompletion;
import org.uimshowdown.bingo.models.RecordHandicap;
import org.uimshowdown.bingo.models.RecordSubmission;
import org.uimshowdown.bingo.models.Submission;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.TeamScoreboard;
import org.uimshowdown.bingo.models.Tile;
import org.uimshowdown.bingo.models.TileProgress;

public class SharedTestVariables {
    public static CollectionLogGroup makeTestCollectionLogGroup() {
        CollectionLogGroup testCollectionTableGroup = new CollectionLogGroup();
        testCollectionTableGroup.setDescription("Clue scroll rewards");
        testCollectionTableGroup.setName("For the Love of the Gamba");
        testCollectionTableGroup.setType(CollectionLogGroupType.CHECKLIST);
        return testCollectionTableGroup;
    }

    public static CollectionLogItem makeTestCollectionLogItem(CollectionLogGroup collectionLogGroup) {
        CollectionLogItem testCollectionLogItem = new CollectionLogItem();
        testCollectionLogItem.setDescription("Boots we chase, yet rarely use.");
        testCollectionLogItem.setGroup(collectionLogGroup);
        testCollectionLogItem.setName("Ranger Boots");
        return testCollectionLogItem;
    }

    public static CollectionLogSubmission makeTestCollectionLogSubmission(CollectionLogItem collectionLogItem, Submission submission) {
        CollectionLogSubmission testCollectionLogSubmission = new CollectionLogSubmission();
        testCollectionLogSubmission.setItem(collectionLogItem);
        testCollectionLogSubmission.setSubmission(submission);
        return testCollectionLogSubmission;
    }

    public static Challenge makeTestChallenge() {
        Challenge testChallenge = new Challenge();
        testChallenge.setDescription("Four player relay for each of the Desert Treasure II bosses.");
        testChallenge.setName("DT2 Boss Relay");
        testChallenge.setTeamSize(4);
        testChallenge.setType(ChallengeType.RELAY);
        return testChallenge;
    }

    public static ChallengeCompletion makeTestChallengeCompletion(Challenge challenge, Team team) {
        ChallengeCompletion challengeCompletion =  new ChallengeCompletion();
        challengeCompletion.setChallenge(challenge);
        challengeCompletion.setCompletedAt(new Timestamp(Instant.now().toEpochMilli()));
        challengeCompletion.setSeconds(75.0);
        challengeCompletion.setTeam(team);
        return challengeCompletion;
    }

    public static ChallengeRelayComponent makeTestChallengeRelayComponent(Challenge challenge) {
        ChallengeRelayComponent testChallengeRelayComponent = new ChallengeRelayComponent();
        testChallengeRelayComponent.setChallenge(challenge);
        testChallengeRelayComponent.setName("Duke Sucellus");
        return testChallengeRelayComponent;
    }

    public static ChallengeSubmission makeTestChallengeSubmission(Challenge challenge, ChallengeRelayComponent challengeRelayComponent, Submission submission) {
        ChallengeSubmission testChallengeSubmission = new ChallengeSubmission();
        testChallengeSubmission.setChallenge(challenge);
        testChallengeSubmission.setRelayComponent(challengeRelayComponent);
        testChallengeSubmission.setSeconds(75.0);
        testChallengeSubmission.setSubmission(submission);
        return testChallengeSubmission;
    }

    public static Contribution makeTestContribution(ContributionMethod contributionMethod, Player player) {
        Contribution testContribution = new Contribution();
        testContribution.setContributionMethod(contributionMethod);
        testContribution.setPlayer(player);
        testContribution.setStaffAdjustment(0);
        testContribution.setUnrankedStartingValue(4);
        return testContribution;
    }

    public static ContributionMethod makeTestContributionMethod(Tile tile) {
        ContributionMethod testContributionMethod = new ContributionMethod();
        testContributionMethod.setContributionMethodCategory(ContributionMethodCategory.PVM);
        testContributionMethod.setContributionMethodType(ContributionMethodType.KC);
        testContributionMethod.setEhtRate(25.0);
        testContributionMethod.setName("Calvar'ion");
        testContributionMethod.setTile(tile);
        return testContributionMethod;
    }

    public static Record makeTestRecord() {
        Record testRecord = new Record();
        testRecord.setDescription("Six hour woodcutting record");
        testRecord.setSkill(PlayerSkill.WOODCUTTING);
        return testRecord;
    }

    public static RecordCompletion makeTestRecordCompletion(Player player, Record record, RecordHandicap recordHandicap) {
        RecordCompletion testRecordCompletion = new RecordCompletion();
        testRecordCompletion.setHandicap(recordHandicap);
        testRecordCompletion.setPlayer(player);
        testRecordCompletion.setRawValue(600000);
        testRecordCompletion.setRecord(record);
        return testRecordCompletion;
    }

    public static RecordHandicap makeTestRecordHandicap(Record record) {
        RecordHandicap testRecordHandicap = new RecordHandicap();
        testRecordHandicap.setMultiplier(1.25);
        testRecordHandicap.setName("Two-handed axe modifier");
        testRecordHandicap.setRecord(record);
        return testRecordHandicap;
    }

    public static RecordSubmission makeTestRecordSubmission(RecordHandicap recordHandicap, Record record, Submission submission) {
        RecordSubmission testRecordSubmission = new RecordSubmission();
        testRecordSubmission.setHandicap(recordHandicap);
        testRecordSubmission.setRecord(record);
        testRecordSubmission.setSubmission(submission);
        testRecordSubmission.setValue(1);
        return testRecordSubmission;
    }

    public static Player makeTestPlayer(Team team) {
        Player testPlayer = new Player();
        testPlayer.setCaptainStatus(true);
        testPlayer.setDiscordName("flashcards");
        testPlayer.setRsn("Flashcards");
        testPlayer.setTeam(team);
        return testPlayer;
    }

    public static PlayerChallengeCompletion makeTestPlayerChallengeCompletion(ChallengeCompletion challengeCompletion, ChallengeRelayComponent challengeRelayComponent, Player player) {
        PlayerChallengeCompletion testPlayerChallengeCompletion = new PlayerChallengeCompletion();
        testPlayerChallengeCompletion.setChallengeCompletion(challengeCompletion);
        testPlayerChallengeCompletion.setPlayer(player);
        testPlayerChallengeCompletion.setRelayComponent(challengeRelayComponent);
        testPlayerChallengeCompletion.setScreenshotUrl("https://www.reddit.com/media?url=https%3A%2F%2Fi.redd.it%2Fs61jozhbbxk61.png");
        testPlayerChallengeCompletion.setSeconds(75.0);
        return testPlayerChallengeCompletion;
    }

    public static Submission makeTestSubmission(Player player) {
        Submission testSubmission = new Submission();
        testSubmission.setSubmissionState(SubmissionState.OPEN);
        testSubmission.setPlayer(player);
        return testSubmission;
    }

    public static Team makeTestTeam() {
        Team testTeam = new Team();
        testTeam.setAbbreviation("ffs");
        testTeam.setColor("c97632");
        testTeam.setName("Falador Fullsends");
        return testTeam;
    }

    public static TeamScoreboard makeTestTeamScoreboard(Team team) {
        TeamScoreboard testTeamScoreboard = new TeamScoreboard();
        testTeamScoreboard.setEventPoints(1);
        testTeamScoreboard.setEventPointsFromChallenge(0);
        testTeamScoreboard.setEventPointsFromCollectionLogItems(1);
        testTeamScoreboard.setEventPointsFromGroups(0);
        testTeamScoreboard.setEventPointsFromRecords(0);
        testTeamScoreboard.setEventPointsFromTiles(0);
        testTeamScoreboard.setTeam(team);
        return testTeamScoreboard;
    }

    public static Tile makeTestTile() {
        Tile testTile = new Tile();
        testTile.setName("Looting Bag Management");
        testTile.setPointsPerTier(100000);
        return testTile;
    }

    public static TileProgress makeTestTileProgress(Tile tile, Team team) {
        TileProgress testTileProgress = new TileProgress();
        testTileProgress.setPercentageToNextTier(0.0);
        testTileProgress.setPoints(0);
        testTileProgress.setTeam(team);
        testTileProgress.setTier(1);
        testTileProgress.setTile(tile);
        return testTileProgress;
    }
}
