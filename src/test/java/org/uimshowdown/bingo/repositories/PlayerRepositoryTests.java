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
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerRepositoryTests {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;
    
    private Player testPlayer;
    private Team testTeam;

    @BeforeAll
    public void setUp() {
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
    }

    @AfterAll
    public void tearDown() {
        playerRepository.delete(testPlayer);
        teamRepository.delete(testTeam);
    }

    @Test
    @Transactional
    public void Should_FindTestPlayer_When_GivenTestDiscordName() {
        Player player = playerRepository.findByDiscordName(testPlayer.getDiscordName()).orElse(null);

        assertThat(player)
            .isNotNull()
            .isEqualTo(testPlayer);
    }

    @Test
    @Transactional
    public void Should_NotFindTestPlayer_When_GivenWrongDiscordName() {
        Player player = playerRepository.findByDiscordName("not.likely.to.be.found").orElse(null);

        assertThat(player).isNull();
    }

    @Test
    @Transactional
    public void Should_FindTestPlayer_When_GivenTestRsn() {
        Player player = playerRepository.findByRsn(testPlayer.getRsn()).orElse(null);

        assertThat(player)
            .isNotNull()
            .isEqualTo(testPlayer);
    }

    @Test
    @Transactional
    public void Should_NotFindTestPlayer_When_GivenWrongRsn() {
        Player player = playerRepository.findByRsn("Not Likely To Be Found").orElse(null);

        assertThat(player).isNull();
    }
}
