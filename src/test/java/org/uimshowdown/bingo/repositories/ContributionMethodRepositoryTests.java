package org.uimshowdown.bingo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Tile;

@SpringBootTest
public class ContributionMethodRepositoryTests {
    @Autowired
    private ContributionMethodRepository contributionMethodRepository;

    @Autowired
    private TileRepository tileRepository;

    private ContributionMethod testContributionMethod;
    private Tile testTile;

    @BeforeEach
    public void setUp() {
        testTile = tileRepository.save(SharedTestVariables.makeTestTile());
        testContributionMethod = contributionMethodRepository.save(SharedTestVariables.makeTestContributionMethod(testTile));
    }

    @AfterEach
    public void tearDown() {
        contributionMethodRepository.delete(testContributionMethod);
        tileRepository.delete(testTile);
    }

    @Test
    @Transactional
    public void Should_FindTestContributionMethod_When_GivenTestContributionName() {
        ContributionMethod contributionMethod = contributionMethodRepository.findByName(testContributionMethod.getName()).orElse(new ContributionMethod());

        assertThat(contributionMethod)
            .isNotNull()
            .isEqualTo(testContributionMethod);
    }

    @Test
    @Transactional
    public void Should_NotFindTestContributionMethod_When_GivenWrongContributionName() {
        ContributionMethod contributionMethod = contributionMethodRepository.findByName("Not Likely to be Found").orElse(new ContributionMethod());

        assertThat(contributionMethod)
            .isNotNull()
            .isNotEqualTo(testContributionMethod);
    }

    @Test
    @Transactional
    public void Should_FindTestContributionMethod_When_GivenTestTileId() {
        Iterable<ContributionMethod> contributionMethods = contributionMethodRepository.findAllByTileId(testTile.getId());

        assertThat(contributionMethods)
            .isNotNull()
            .isNotEmpty()
            .contains(testContributionMethod);
    }

    public void Should_NotFindTestContributionMethod_When_GivenWrongTileId() {
        Iterable<ContributionMethod> contributionsMethods = contributionMethodRepository.findAllByTileId(0);

        assertThat(contributionsMethods)
            .isNotNull()
            .doesNotContain(testContributionMethod);
    }
}
