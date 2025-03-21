package org.uimshowdown.bingo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.Tile;

@SpringBootTest
public class ContributionRepositoryTests {
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

    @BeforeEach
    public void setUp() {
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
        testTile = tileRepository.save(SharedTestVariables.makeTestTile());
        testContributionMethod = contributionMethodRepository.save(SharedTestVariables.makeTestContributionMethod(testTile));
        testContribution = contributionRepository.save(SharedTestVariables.makeTestContribution(testContributionMethod, testPlayer));
    }

    @AfterEach
    public void tearDown() {
        contributionRepository.delete(testContribution);
        contributionMethodRepository.delete(testContributionMethod);
        tileRepository.delete(testTile);
        playerRepository.delete(testPlayer);
        teamRepository.delete(testTeam);
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
