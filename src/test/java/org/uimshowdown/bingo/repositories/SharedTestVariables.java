package org.uimshowdown.bingo.repositories;

import org.uimshowdown.bingo.enums.ContributionMethodCategory;
import org.uimshowdown.bingo.enums.ContributionMethodType;
import org.uimshowdown.bingo.enums.SubmissionState;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Submission;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.Tile;

public class SharedTestVariables {
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
