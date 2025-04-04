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
import org.uimshowdown.bingo.models.ChallengeRelayComponent;
import org.uimshowdown.bingo.models.ChallengeSubmission;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChallengeSubmissionRepositoryTests {
    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeRelayComponentRepository challengeRelayComponentRepository;

    @Autowired
    private ChallengeSubmissionRepository challengeSubmissionRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Challenge testChallenge;
    private ChallengeRelayComponent testChallengeRelayComponent;
    private ChallengeSubmission testChallengeSubmission;
    private Player testPlayer;
    private Team testTeam;

    @BeforeAll
    public void setUp() {
    	teamRepository.deleteAll();
    	playerRepository.deleteAll();
    	challengeRepository.deleteAll();
    	challengeRelayComponentRepository.deleteAll();
    	challengeSubmissionRepository.deleteAll();
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
        testChallenge = challengeRepository.save(SharedTestVariables.makeTestChallenge());
        testChallengeRelayComponent = challengeRelayComponentRepository.save(SharedTestVariables.makeTestChallengeRelayComponent(testChallenge));
        testChallengeSubmission = challengeSubmissionRepository.save(SharedTestVariables.makeTestChallengeSubmission(testChallenge, testChallengeRelayComponent, testPlayer));
    }

    @AfterAll
    public void tearDown() {
        challengeSubmissionRepository.delete(testChallengeSubmission);
        challengeRelayComponentRepository.delete(testChallengeRelayComponent);
        challengeRepository.delete(testChallenge);
        playerRepository.delete(testPlayer);
        teamRepository.delete(testTeam);
    }

    @Test
    @Transactional
    public void Should_FindTestChallengeSubmission_When_GivenTestChallengeId() {
        Iterable<ChallengeSubmission> challengeSubmissions = challengeSubmissionRepository.findAllByChallengeId(testChallenge.getId());

        assertThat(challengeSubmissions)
            .isNotNull()
            .isNotEmpty()
            .contains(testChallengeSubmission);
    }

    @Test
    @Transactional
    public void Should_NotFindTestChallengeSubmission_When_GivenWrongChallengeId() {
        Iterable<ChallengeSubmission> challengeSubmissions = challengeSubmissionRepository.findAllByChallengeId(0);

        assertThat(challengeSubmissions)
            .isNotNull()
            .doesNotContain(testChallengeSubmission);
    }

    @Test
    @Transactional
    public void Should_FindTestChallengeSubmission_When_GivenTestChallengeRelayComponentId() {
        Iterable<ChallengeSubmission> challengeSubmissions = challengeSubmissionRepository.findAllByRelayComponentId(testChallengeRelayComponent.getId());

        assertThat(challengeSubmissions)
            .isNotNull()
            .isNotEmpty()
            .contains(testChallengeSubmission);
    }

    @Test
    @Transactional
    public void Should_NotFindTestChallengeSubmission_When_GivenWrongChallengeRelayComponentId() {
        Iterable<ChallengeSubmission> challengeSubmissions = challengeSubmissionRepository.findAllByRelayComponentId(0);

        assertThat(challengeSubmissions)
            .isNotNull()
            .doesNotContain(testChallengeSubmission);
    }
}
