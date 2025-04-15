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
import org.uimshowdown.bingo.TestUtils;
import org.uimshowdown.bingo.constants.TestTag;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.Tile;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContributionRepositoryTests {
    
    @Autowired
    private TestUtils testUtils;
    
    @Autowired
    private ContributionRepository contributionRepository;

    @Autowired
    private ContributionMethodRepository contributionMethodRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TileRepository tileRepository;

    private Contribution testContribution;
    private ContributionMethod testContributionMethod;
    private Player testPlayer;
    private Team testTeam;
    private Tile testTile;

    @BeforeAll
    public void setUp() {
        testUtils.resetDB();
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
        testTile = tileRepository.save(SharedTestVariables.makeTestTile());
        testContributionMethod = contributionMethodRepository.save(SharedTestVariables.makeTestContributionMethod(testTile));
        testContribution = contributionRepository.save(SharedTestVariables.makeTestContribution(testContributionMethod, testPlayer));
    }

    @AfterAll
    public void tearDown() {
        testUtils.resetDB();
    }

    @Test
    @Transactional
    public void Should_FindTestContribution_When_GivenTestPlayerId() {
        Iterable<Contribution> contributions = contributionRepository.findAllByPlayerId(testPlayer.getId());

        assertThat(contributions)
            .isNotNull()
            .isNotEmpty()
            .contains(testContribution);
    }

    @Test
    @Transactional
    public void Should_NotFindTestContribution_When_GivenWrongPlayerId() {
        Iterable<Contribution> contributions = contributionRepository.findAllByPlayerId(0);

        assertThat(contributions)
            .isNotNull()
            .doesNotContain(testContribution);
    }
    
}
