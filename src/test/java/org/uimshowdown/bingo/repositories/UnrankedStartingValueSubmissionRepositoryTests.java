package org.uimshowdown.bingo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.uimshowdown.bingo.constants.TestTag;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.Tile;
import org.uimshowdown.bingo.models.UnrankedStartingValueSubmission;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UnrankedStartingValueSubmissionRepositoryTests {
    @Autowired
    private ContributionMethodRepository contributionMethodRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TileRepository tileRepository;

    @Autowired
    private UnrankedStartingValueSubmissionRepository unrankedStartingValueSubmissionRepository;

    private ContributionMethod testContributionMethod;
    private Player testPlayer;
    private Team testTeam;
    private Tile testTile;
    private UnrankedStartingValueSubmission testUnrankedStartingValueSubmission;

    @BeforeAll
    public void setUp() {
    	teamRepository.deleteAll();
    	playerRepository.deleteAll();
    	tileRepository.deleteAll();
    	contributionMethodRepository.deleteAll();
    	unrankedStartingValueSubmissionRepository.deleteAll();
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
        testTile = tileRepository.save(SharedTestVariables.makeTestTile());
        testContributionMethod = contributionMethodRepository.save(SharedTestVariables.makeTestContributionMethod(testTile));
        testUnrankedStartingValueSubmission = unrankedStartingValueSubmissionRepository.save(SharedTestVariables.makeTestUnrankedStartingValueSubmission(testContributionMethod, testPlayer));
    }

    @AfterAll
    public void tearDown() {
        unrankedStartingValueSubmissionRepository.delete(testUnrankedStartingValueSubmission);
        contributionMethodRepository.delete(testContributionMethod);
        tileRepository.delete(testTile);
        playerRepository.delete(testPlayer);
        teamRepository.delete(testTeam);
    }

    @Test
    @Transactional
    public void Should_FindUnrankedStartingValueSubmission_When_GivenTestContributionMethodId() {
        Iterable<UnrankedStartingValueSubmission> unrankedStartingValueSubmissions = unrankedStartingValueSubmissionRepository.findAllByContributionMethodId(testContributionMethod.getId());

        assertThat(unrankedStartingValueSubmissions)
            .isNotNull()
            .isNotEmpty()
            .contains(testUnrankedStartingValueSubmission);
    }

    @Test
    @Transactional
    public void Should_NotFindUnrankedStartingValueSubmission_When_GivenWrongContributionMethodId() {
        Iterable<UnrankedStartingValueSubmission> unrankedStartingValueSubmissions = unrankedStartingValueSubmissionRepository.findAllByContributionMethodId(0);

        assertThat(unrankedStartingValueSubmissions)
            .isNotNull()
            .doesNotContain(testUnrankedStartingValueSubmission);
    }
}
