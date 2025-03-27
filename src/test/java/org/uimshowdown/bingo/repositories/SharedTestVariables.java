package org.uimshowdown.bingo.repositories;

import java.sql.Timestamp;
import java.time.Instant;

import org.uimshowdown.bingo.enums.ChallengeType;
import org.uimshowdown.bingo.enums.ContributionMethodCategory;
import org.uimshowdown.bingo.enums.ContributionMethodType;
import org.uimshowdown.bingo.enums.SubmissionState;
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.ChallengeCompletion;
import org.uimshowdown.bingo.models.ChallengeRelayComponent;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Record;
import org.uimshowdown.bingo.models.RecordCompletion;
import org.uimshowdown.bingo.models.RecordHandicap;
import org.uimshowdown.bingo.models.RecordSubmission;
import org.uimshowdown.bingo.models.Submission;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.Tile;

public class SharedTestVariables {
    public static Challenge makeTestChallenge() {
        return new Challenge()
            .setDescription("Four player relay for each of the Desert Treasure II bosses.")
            .setName("DT2 Boss Relay")
            .setTeamSize(4)
            .setType(ChallengeType.RELAY);
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
        return new ChallengeRelayComponent()
            .setChallenge(challenge)
            .setName("Duke Sucellus");
    }

    public static Contribution makeTestContribution(ContributionMethod contributionMethod, Player player) {
        return new Contribution()
            .setContributionMethod(contributionMethod)
            .setPlayer(player)
            .setStaffAdjustment(0)
            .setUnrankedStartingValue(4);
    }

    public static ContributionMethod makeTestContributionMethod(Tile tile) {
        return new ContributionMethod()
            .setContributionMethodCategory(ContributionMethodCategory.PVM)
            .setContributionMethodType(ContributionMethodType.KC)
            .setEhtRate(25.0)
            .setName("Calvar'ion")
            .setTile(tile);
    }

    public static Record makeTestRecord() {
        return new Record()
            .setDescription("Six hour woodcutting record")
            .setSkill("Woodcutting");
    }

    public static RecordCompletion makeTestRecordCompletion(Player player, Record record, RecordHandicap recordHandicap) {
        return new RecordCompletion()
            .setHandicap(recordHandicap)
            .setPlayer(player)
            .setRawValue(600000)
            .setRecord(record);
    }

    public static RecordHandicap makeTestRecordHandicap(Record record) {
        return new RecordHandicap()
            .setMultiplier(1.25)
            .setName("Two-handed axe modifier")
            .setRecord(record);
    }

    public static RecordSubmission makeTestRecordSubmission(RecordHandicap recordHandicap, Record record, Submission submission) {
        return new RecordSubmission()
            .setHandicap(recordHandicap)
            .setRecord(record)
            .setSubmission(submission)
            .setValue(1);
    }

    public static Player makeTestPlayer(Team team) {
        return new Player()
            .setCaptainStatus(true)
            .setDiscordName("flashcards")
            .setRsn("Flashcards")
            .setTeam(team);
    }

    public static Submission makeTestSubmission(Player player) {
        return new Submission()
            .setSubmissionState(SubmissionState.OPEN)
            .setPlayer(player);
    }

    public static Team makeTestTeam() {
        return new Team()
            .setAbbreviation("ffs")
            .setColor("c97632")
            .setName("Falador Fullsends");
    }

    public static Tile makeTestTile() {
        return new Tile()
            .setName("Looting Bag Management")
            .setPointsPerTier(100000);
    }
}
