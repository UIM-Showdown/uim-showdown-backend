package org.uimshowdown.bingo.repositories;

import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.ChallengeCompletion;
import org.uimshowdown.bingo.models.ChallengeRelayComponent;
import org.uimshowdown.bingo.models.ChallengeSubmission;
import org.uimshowdown.bingo.models.CollectionLogCompletion;
import org.uimshowdown.bingo.models.CollectionLogItem;
import org.uimshowdown.bingo.models.CollectionLogSubmission;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.ContributionSubmission;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.PlayerChallengeCompletion;
import org.uimshowdown.bingo.models.PlayerScoreboard;
import org.uimshowdown.bingo.models.Record;
import org.uimshowdown.bingo.models.RecordCompletion;
import org.uimshowdown.bingo.models.RecordHandicap;
import org.uimshowdown.bingo.models.RecordSubmission;
import org.uimshowdown.bingo.models.Submission;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.TeamScoreboard;
import org.uimshowdown.bingo.models.Tile;
import org.uimshowdown.bingo.models.TileProgress;
import org.uimshowdown.bingo.models.UnrankedStartingValueSubmission;

public class SharedTestVariables {
    public static CollectionLogCompletion makeTestCollectionLogCompletion(CollectionLogItem collectionLogItem, Player player) {
        CollectionLogCompletion testCollectionLogCompletion = new CollectionLogCompletion();
        testCollectionLogCompletion.setItem(collectionLogItem);
        testCollectionLogCompletion.setPlayer(player);
        testCollectionLogCompletion.setScreenshotUrl("https://www.reddit.com/media?url=https%3A%2F%2Fi.redd.it%2Fs61jozhbbxk61.png");
        return testCollectionLogCompletion;
    }

    public static CollectionLogItem makeTestCollectionLogItem() {
        CollectionLogItem testCollectionLogItem = new CollectionLogItem();
        testCollectionLogItem.setDescription("Boots we chase, yet rarely use.");
        testCollectionLogItem.setName("Ranger Boots");
        return testCollectionLogItem;
    }

    public static CollectionLogSubmission makeTestCollectionLogSubmission(CollectionLogItem collectionLogItem, Player player) {
        CollectionLogSubmission testCollectionLogSubmission = new CollectionLogSubmission();
        testCollectionLogSubmission.setItem(collectionLogItem);
        testCollectionLogSubmission.setPlayer(player);
        testCollectionLogSubmission.setSubmissionState(Submission.State.OPEN);
        return testCollectionLogSubmission;
    }

    public static Challenge makeTestChallenge() {
        Challenge testChallenge = new Challenge();
        testChallenge.setDescription("Four player relay for each of the Desert Treasure II bosses.");
        testChallenge.setName("DT2 Boss Relay");
        testChallenge.setTeamSize(4);
        testChallenge.setType(Challenge.Type.RELAY);
        return testChallenge;
    }

    public static ChallengeCompletion makeTestChallengeCompletion(Challenge challenge, Team team) {
        ChallengeCompletion challengeCompletion =  new ChallengeCompletion();
        challengeCompletion.setChallenge(challenge);
        challengeCompletion.setTeam(team);
        return challengeCompletion;
    }

    public static ChallengeRelayComponent makeTestChallengeRelayComponent(Challenge challenge) {
        ChallengeRelayComponent testChallengeRelayComponent = new ChallengeRelayComponent();
        testChallengeRelayComponent.setChallenge(challenge);
        testChallengeRelayComponent.setName("Duke Sucellus");
        return testChallengeRelayComponent;
    }

    public static ChallengeSubmission makeTestChallengeSubmission(Challenge challenge, ChallengeRelayComponent challengeRelayComponent, Player player) {
        ChallengeSubmission testChallengeSubmission = new ChallengeSubmission();
        testChallengeSubmission.setChallenge(challenge);
        testChallengeSubmission.setPlayer(player);
        testChallengeSubmission.setRelayComponent(challengeRelayComponent);
        testChallengeSubmission.setSeconds(75.0);
        testChallengeSubmission.setSubmissionState(Submission.State.OPEN);
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
        testContributionMethod.setContributionMethodCategory(ContributionMethod.Category.PVM);
        testContributionMethod.setContributionMethodType(ContributionMethod.Type.TEMPLE_KC);
        testContributionMethod.setEhtRate(25.0);
        testContributionMethod.setName("Calvar'ion");
        testContributionMethod.setTile(tile);
        return testContributionMethod;
    }

    public static ContributionSubmission makeTestContributionSubmission(ContributionMethod contributionMethod, Player player) {
        ContributionSubmission testContributionSubmission = new ContributionSubmission();
        testContributionSubmission.setContributionMethod(contributionMethod);
        testContributionSubmission.setPlayer(player);
        testContributionSubmission.setSubmissionState(Submission.State.OPEN);
        testContributionSubmission.setValue(1);
        return testContributionSubmission;
    }

    public static Record makeTestRecord() {
        Record testRecord = new Record();
        testRecord.setDescription("Six hour woodcutting record");
        testRecord.setSkill(Player.Skill.WOODCUTTING);
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

    public static RecordSubmission makeTestRecordSubmission(RecordHandicap recordHandicap, Player player, Record record) {
        RecordSubmission testRecordSubmission = new RecordSubmission();
        testRecordSubmission.setHandicap(recordHandicap);
        testRecordSubmission.setPlayer(player);
        testRecordSubmission.setRecord(record);
        testRecordSubmission.setSubmissionState(Submission.State.OPEN);
        testRecordSubmission.setRawValue(1);
        return testRecordSubmission;
    }

    public static Player makeTestPlayer(Team team) {
        Player testPlayer = new Player();
        testPlayer.setDiscordName("flashcards");
        testPlayer.setRsn("Flashcards");
        testPlayer.setTeam(team);
        return testPlayer;
    }
    
    public static Player makeTestCaptain(Team team) {
        Player testPlayer = new Player();
        testPlayer.setDiscordName("derek_mk");
        testPlayer.setRsn("DerekMK");
        testPlayer.setTeam(team);
        return testPlayer;
    }

    public static PlayerChallengeCompletion makeTestPlayerChallengeCompletion(ChallengeRelayComponent challengeRelayComponent, Player player) {
        PlayerChallengeCompletion testPlayerChallengeCompletion = new PlayerChallengeCompletion();
        testPlayerChallengeCompletion.setPlayer(player);
        testPlayerChallengeCompletion.setRelayComponent(challengeRelayComponent);
        testPlayerChallengeCompletion.setChallenge(challengeRelayComponent.getChallenge());
        testPlayerChallengeCompletion.setScreenshotUrl("https://www.reddit.com/media?url=https%3A%2F%2Fi.redd.it%2Fs61jozhbbxk61.png");
        testPlayerChallengeCompletion.setSeconds(75.0);
        return testPlayerChallengeCompletion;
    }

    public static PlayerScoreboard makeTestPlayerScoreboard(Player player) {
        PlayerScoreboard testPlayerScoreboard = new PlayerScoreboard();
        testPlayerScoreboard.setCollectionLogPoints(1);
        testPlayerScoreboard.setOtherTileContribution(0.0);
        testPlayerScoreboard.setPlayer(player);
        testPlayerScoreboard.setPvmTileContribution(0.0);
        testPlayerScoreboard.setSkillingTitleContribution(0.0);
        testPlayerScoreboard.setTotalTileContribution(1.0);
        return testPlayerScoreboard;
    }

    public static ChallengeSubmission makeTestSubmission(Player player, Challenge challenge) {
        ChallengeSubmission testSubmission = new ChallengeSubmission();
        testSubmission.setSubmissionState(Submission.State.OPEN);
        testSubmission.setPlayer(player);
        testSubmission.setChallenge(challenge);
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
        testTeamScoreboard.setEventPointsFromChallenges(0);
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
        testTile.setAbbreviation("LBM");
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

    public static UnrankedStartingValueSubmission makeTestUnrankedStartingValueSubmission(ContributionMethod contributionMethod, Player player) {
        UnrankedStartingValueSubmission testUnrankedStartingValueSubmission = new UnrankedStartingValueSubmission();
        testUnrankedStartingValueSubmission.setContributionMethod(contributionMethod);
        testUnrankedStartingValueSubmission.setPlayer(player);
        testUnrankedStartingValueSubmission.setSubmissionState(Submission.State.OPEN);
        testUnrankedStartingValueSubmission.setValue(1);
        return testUnrankedStartingValueSubmission;
    }
}
