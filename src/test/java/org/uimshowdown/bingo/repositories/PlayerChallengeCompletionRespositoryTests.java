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
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.ChallengeCompletion;
import org.uimshowdown.bingo.models.ChallengeRelayComponent;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.PlayerChallengeCompletion;
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerChallengeCompletionRespositoryTests {
    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeCompletionRepository challengeCompletionRepository;

    @Autowired
    private ChallengeRelayComponentRepository challengeRelayComponentRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerChallengeCompletionRepository playerChallengeCompletionRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Challenge testChallenge;
    private ChallengeCompletion testChallengeCompletion;
    private ChallengeRelayComponent testChallengeRelayComponent;
    private Player testPlayer;
    private PlayerChallengeCompletion testPlayerChallengeCompletion;
    private Team testTeam;

    @BeforeAll
    public void setUp() {
        teamRepository.deleteAll();
        playerRepository.deleteAll();
        challengeRepository.deleteAll();
        challengeCompletionRepository.deleteAll();
        challengeRelayComponentRepository.deleteAll();
        playerChallengeCompletionRepository.deleteAll();
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
        testChallenge = challengeRepository.save(SharedTestVariables.makeTestChallenge());
        testChallengeCompletion = challengeCompletionRepository.save(SharedTestVariables.makeTestChallengeCompletion(testChallenge, testTeam));
        testChallengeRelayComponent = challengeRelayComponentRepository.save(SharedTestVariables.makeTestChallengeRelayComponent(testChallenge));
        testPlayerChallengeCompletion = playerChallengeCompletionRepository.save(SharedTestVariables.makeTestPlayerChallengeCompletion(testChallengeCompletion, testChallengeRelayComponent, testPlayer));
    }

    @AfterAll
    public void tearDown() {
        playerChallengeCompletionRepository.delete(testPlayerChallengeCompletion);
        challengeRelayComponentRepository.delete(testChallengeRelayComponent);
        challengeCompletionRepository.delete(testChallengeCompletion);
        challengeRepository.delete(testChallenge);
        playerRepository.delete(testPlayer);
        teamRepository.delete(testTeam);
    }

    @Test
    @Transactional
    public void Should_FindTestPlayerChallengeCompletion_When_GivenTestChallengeCompletionId() {
        Iterable<PlayerChallengeCompletion> playerChallengeCompletions = playerChallengeCompletionRepository.findAllByChallengeCompletionId(testChallengeCompletion.getId());

        assertThat(playerChallengeCompletions)
            .isNotNull()
            .isNotEmpty()
            .contains(testPlayerChallengeCompletion);
    }

    @Test
    @Transactional
    public void Should_NotFindTestPlayerChallengeCompletion_When_GivenWrongChallengeCompletionId() {
        Iterable<PlayerChallengeCompletion> playerChallengeCompletions = playerChallengeCompletionRepository.findAllByChallengeCompletionId(0);

        assertThat(playerChallengeCompletions)
            .isNotNull()
            .doesNotContain(testPlayerChallengeCompletion);
    }

    @Test
    @Transactional
    public void Should_FindTestPlayerChallengeCompletion_When_GivenTestPlayerId() {
        Iterable<PlayerChallengeCompletion> playerChallengeCompletions = playerChallengeCompletionRepository.findAllByPlayerId(testPlayer.getId());

        assertThat(playerChallengeCompletions)
            .isNotNull()
            .isNotEmpty()
            .contains(testPlayerChallengeCompletion);
    }

    @Test
    @Transactional
    public void Should_NotFindTestPlayerChallengeCompletion_When_GivenWrongPlayerId() {
        Iterable<PlayerChallengeCompletion> playerChallengeCompletions = playerChallengeCompletionRepository.findAllByPlayerId(0);

        assertThat(playerChallengeCompletions)
            .isNotNull()
            .doesNotContain(testPlayerChallengeCompletion);
    }

    @Test
    @Transactional
    public void Should_FindTestPlayerChallengeCompletion_When_GivenTestRelayComponentId() {
        Iterable<PlayerChallengeCompletion> playerChallengeCompletions = playerChallengeCompletionRepository.findAllByRelayComponentId(testChallengeRelayComponent.getId());

        assertThat(playerChallengeCompletions)
            .isNotNull()
            .isNotEmpty()
            .contains(testPlayerChallengeCompletion);
    }

    @Test
    @Transactional
    public void Should_NotFindTestPlayerChallengeCompletion_When_GivenWrongRelayComponentId() {
        Iterable<PlayerChallengeCompletion> playerChallengeCompletions = playerChallengeCompletionRepository.findAllByRelayComponentId(0);

        assertThat(playerChallengeCompletions)
            .isNotNull()
            .doesNotContain(testPlayerChallengeCompletion);
    }
}
