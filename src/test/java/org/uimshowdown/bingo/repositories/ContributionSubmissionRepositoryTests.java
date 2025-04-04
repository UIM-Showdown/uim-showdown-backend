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
import org.uimshowdown.bingo.models.ContributionSubmission;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.Tile;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContributionSubmissionRepositoryTests {
    @Autowired
    private ContributionMethodRepository contributionMethodRepository;

    @Autowired
    private ContributionSubmissionRepository contributionSubmissionRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TileRepository tileRepository;

    private ContributionMethod testContributionMethod;
    private ContributionSubmission testContributionSubmission;
    private Player testPlayer;
    private Team testTeam;
    private Tile testTile;

    @BeforeAll
    public void setUp() {
    	contributionSubmissionRepository.deleteAll();
        teamRepository.deleteAll();
        playerRepository.deleteAll();
        contributionMethodRepository.deleteAll();
        tileRepository.deleteAll();
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
        testTile = tileRepository.save(SharedTestVariables.makeTestTile());
        testContributionMethod = contributionMethodRepository.save(SharedTestVariables.makeTestContributionMethod(testTile));
        testContributionSubmission = contributionSubmissionRepository.save(SharedTestVariables.makeTestContributionSubmission(testContributionMethod, testPlayer));
    }

    @AfterAll
    public void tearDown() {
        contributionSubmissionRepository.delete(testContributionSubmission);
        contributionMethodRepository.delete(testContributionMethod);
        tileRepository.delete(testTile);
        playerRepository.delete(testPlayer);
        teamRepository.delete(testTeam);
    }

    @Test
    @Transactional
    public void Should_FindContributionSubmission_When_GivenTestContributionMethodId() {
        Iterable<ContributionSubmission> contributionSubmissions = contributionSubmissionRepository.findAllByContributionMethodId(testContributionMethod.getId());

        assertThat(contributionSubmissions)
            .isNotNull()
            .isNotEmpty()
            .contains(testContributionSubmission);
    }

    @Test
    @Transactional
    public void Should_NotFindContributionSubmission_When_GivenWrongContributionMethodId() {
        Iterable<ContributionSubmission> contributionSubmissions = contributionSubmissionRepository.findAllByContributionMethodId(0);

        assertThat(contributionSubmissions)
            .isNotNull()
            .doesNotContain(testContributionSubmission);
    }
}
