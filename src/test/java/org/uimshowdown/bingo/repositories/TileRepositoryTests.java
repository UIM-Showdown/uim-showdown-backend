package org.uimshowdown.bingo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.uimshowdown.bingo.constants.TestTag;
import org.uimshowdown.bingo.models.Tile;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
public class TileRepositoryTests extends SharedTestVariables {
    @Autowired
    private TileRepository tileRepository;

    private Tile testTile;

    @BeforeEach
    public void setUp() {
        testTile = tileRepository.save(SharedTestVariables.makeTestTile());
    }

    @AfterEach
    public void tearDown() {
        tileRepository.delete(testTile);
    }

    @Test
    @Transactional
    public void Should_FindTestTile_When_GivenTestName() {
        Tile tile = tileRepository.findByName(testTile.getName()).orElse(new Tile());

        assertThat(tile)
            .isNotNull()
            .isEqualTo(testTile);
    }

    @Test
    @Transactional
    public void Should_NotFindTestTile_When_GivenWrongName() {
        Tile tile = tileRepository.findByName("Not Likely To Be Found").orElse(new Tile());

        assertThat(tile)
            .isNotNull()
            .isNotEqualTo(testTile);
    }
}
