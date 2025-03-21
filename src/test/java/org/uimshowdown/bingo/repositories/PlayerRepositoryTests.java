package org.uimshowdown.bingo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
public class PlayerRepositoryTests {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;
    
    private Player testPlayer;
    private Team testTeam;

    @BeforeEach
    public void setUp() {
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
    }

    @AfterEach
    public void tearDown() {
        playerRepository.delete(testPlayer);
        teamRepository.delete(testTeam);
    }

    @Test
    @Transactional
    public void Should_FindTestPlayer_When_GivenTestDiscordName() {
        Player player = playerRepository.findByDiscordName(testPlayer.getDiscordName()).orElse(new Player());

        assertThat(player)
            .isNotNull()
            .isEqualTo(testPlayer);
    }

    @Test
    @Transactional
    public void Should_NotFindTestPlayer_When_GivenWrongDiscordName() {
        Player player = playerRepository.findByDiscordName("not.likely.to.be.found").orElse(new Player());

        assertThat(player)
            .isNotNull()
            .isNotEqualTo(testPlayer);
    }

    @Test
    @Transactional
    public void Should_FindTestPlayer_When_GivenTestRsn() {
        Player player = playerRepository.findByRsn(testPlayer.getRsn()).orElse(new Player());

        assertThat(player)
            .isNotNull()
            .isEqualTo(testPlayer);
    }

    @Test
    @Transactional
    public void Should_NotFindTestPlayer_When_GivenWrongRsn() {
        Player player = playerRepository.findByRsn("Not Likely To Be Found").orElse(new Player());

        assertThat(player)
            .isNotNull()
            .isNotEqualTo(testPlayer);
    }
}
