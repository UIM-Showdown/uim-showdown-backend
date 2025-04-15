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
import org.uimshowdown.bingo.models.Tile;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TileRepositoryTests extends SharedTestVariables {
    
    @Autowired
    private TestUtils testUtils;
    
    @Autowired
    private TileRepository tileRepository;

    private Tile testTile;

    @BeforeAll
    public void setUp() {
        testUtils.resetDB();
        testTile = tileRepository.save(SharedTestVariables.makeTestTile());
    }

    @AfterAll
    public void tearDown() {
        testUtils.resetDB();
    }

    @Test
    @Transactional
    public void Should_FindTestTile_When_GivenTestName() {
        Tile tile = tileRepository.findByName(testTile.getName()).orElse(null);

        assertThat(tile)
            .isNotNull()
            .isEqualTo(testTile);
    }

    @Test
    @Transactional
    public void Should_NotFindTestTile_When_GivenWrongName() {
        Tile tile = tileRepository.findByName("Not Likely To Be Found").orElse(null);

        assertThat(tile).isNull();
    }
    
}
