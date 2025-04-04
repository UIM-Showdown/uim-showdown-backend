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
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.Tile;
import org.uimshowdown.bingo.models.TileProgress;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TileProgressRepositoryTests {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TileRepository tileRepository;

    @Autowired
    private TileProgressRepository tileProgressRepository;

    private Team testTeam;
    private Tile testTile;
    private TileProgress testTileProgress;

    @BeforeAll
    public void setUp() {
        teamRepository.deleteAll();
        tileRepository.deleteAll();
        tileProgressRepository.deleteAll();
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testTile = tileRepository.save(SharedTestVariables.makeTestTile());
        testTileProgress = tileProgressRepository.save(SharedTestVariables.makeTestTileProgress(testTile, testTeam));
    }

    @AfterAll
    public void tearDown() {
        tileProgressRepository.delete(testTileProgress);
        tileRepository.delete(testTile);
        teamRepository.delete(testTeam);
    }

    @Test
    @Transactional
    public void Should_FindTestTileProgress_When_GivenTestTeamId() {
        Iterable<TileProgress> tileProgress = tileProgressRepository.findAllByTeamId(testTeam.getId());

        assertThat(tileProgress)
            .isNotNull()
            .isNotEmpty()
            .contains(testTileProgress);
    }

    @Test
    @Transactional
    public void Should_NotFindTestTileProgress_When_GivenWrongTeamId() {
        Iterable<TileProgress> tileProgress = tileProgressRepository.findAllByTeamId(0);

        assertThat(tileProgress)
            .isNotNull()
            .doesNotContain(testTileProgress);
    }

    @Test
    @Transactional
    public void Should_FindTestTileProgress_When_GivenTestTileId() {
        Iterable<TileProgress> tileProgress = tileProgressRepository.findAllByTileId(testTile.getId());

        assertThat(tileProgress)
            .isNotNull()
            .isNotEmpty()
            .contains(testTileProgress);
    }

    @Test
    @Transactional
    public void Should_NotFindTestTileProgress_When_GivenWrongTileId() {
        Iterable<TileProgress> tileProgress = tileProgressRepository.findAllByTileId(0);

        assertThat(tileProgress)
            .isNotNull()
            .doesNotContain(testTileProgress);
    }
}
