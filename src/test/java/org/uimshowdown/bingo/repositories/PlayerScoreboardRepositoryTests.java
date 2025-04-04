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
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.PlayerScoreboard;
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerScoreboardRepositoryTests {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerScoreboardRepository playerScoreboardRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Player testPlayer;
    private PlayerScoreboard testPlayerScoreboard;
    private Team testTeam;

    @BeforeAll
    public void setUp() {
    	playerScoreboardRepository.deleteAll();
    	playerRepository.deleteAll();
    	teamRepository.deleteAll();
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
        testPlayerScoreboard = playerScoreboardRepository.save(SharedTestVariables.makeTestPlayerScoreboard(testPlayer));
    }

    @AfterAll
    public void tearDown() {
        playerScoreboardRepository.delete(testPlayerScoreboard);
        playerRepository.delete(testPlayer);
        teamRepository.delete(testTeam);
    }

    @Test
    @Transactional
    public void Should_FindTestPlayerScoreboard_When_GivenTestPlayerId() {
        PlayerScoreboard playerScoreboard = playerScoreboardRepository.findByPlayerId(testPlayer.getId()).orElse(null);

        assertThat(playerScoreboard)
            .isNotNull()
            .isEqualTo(testPlayerScoreboard);
    }

    @Test
    @Transactional
    public void Should_NotFindTestPlayerScoreboard_When_GivenWrongPlayerId() {
        PlayerScoreboard playerScoreboard = playerScoreboardRepository.findByPlayerId(0).orElse(null);

        assertThat(playerScoreboard)
            .isNull();
    }
}
