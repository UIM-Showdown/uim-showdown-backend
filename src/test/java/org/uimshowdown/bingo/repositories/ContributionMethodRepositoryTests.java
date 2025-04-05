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
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Tile;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContributionMethodRepositoryTests {
	
	@Autowired
	private TestUtils testUtils;
	
    @Autowired
    private ContributionMethodRepository contributionMethodRepository;

    @Autowired
    private TileRepository tileRepository;

    private ContributionMethod testContributionMethod;
    private Tile testTile;

    @BeforeAll
    public void setUp() {
    	testUtils.resetDB();
        testTile = tileRepository.save(SharedTestVariables.makeTestTile());
        testContributionMethod = contributionMethodRepository.save(SharedTestVariables.makeTestContributionMethod(testTile));
    }

    @AfterAll
    public void tearDown() {
    	testUtils.resetDB();
    }

    @Test
    @Transactional
    public void Should_FindTestContributionMethod_When_GivenTestContributionName() {
        ContributionMethod contributionMethod = contributionMethodRepository.findByName(testContributionMethod.getName()).orElse(null);

        assertThat(contributionMethod)
            .isNotNull()
            .isEqualTo(testContributionMethod);
    }

    @Test
    @Transactional
    public void Should_NotFindTestContributionMethod_When_GivenWrongContributionName() {
        ContributionMethod contributionMethod = contributionMethodRepository.findByName("Not Likely to be Found").orElse(null);

        assertThat(contributionMethod).isNull();
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
