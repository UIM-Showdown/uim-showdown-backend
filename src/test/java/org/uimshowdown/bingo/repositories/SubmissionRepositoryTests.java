package org.uimshowdown.bingo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.uimshowdown.bingo.TestUtils;
import org.uimshowdown.bingo.constants.TestTag;
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.ChallengeSubmission;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Submission;
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class SubmissionRepositoryTests {
    
    @Autowired
    private TestUtils testUtils;
    
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SubmissionRepository submissionRepository;
    
    @Autowired
    private ChallengeRepository challengeRepository;

    private Team testTeam;
    private Player testPlayer;
    private ChallengeSubmission testSubmission;
    private Challenge testChallenge;

    @BeforeAll
    public void setUp() {
        testUtils.resetDB();
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
        testChallenge = challengeRepository.save(SharedTestVariables.makeTestChallenge());
        testSubmission = submissionRepository.save(SharedTestVariables.makeTestSubmission(testPlayer, testChallenge));
    }

    @AfterAll
    public void tearDown() {
        testUtils.resetDB();
    }

    @Test
    @Transactional
    public void Should_FindTestSubmission_When_GivenTestPlayerId() {
        Iterable<Submission> submissions = submissionRepository.findAllByPlayerId(testPlayer.getId());

        assertThat(submissions)
            .isNotNull()
            .isNotEmpty()
            .contains(testSubmission);
    }

    @Test
    @Transactional
    public void Should_NotFindTestSubmission_When_GivenIncorrectPlayerId() {
        Iterable<Submission> submissions = submissionRepository.findAllByPlayerId(0);

        assertThat(submissions)
            .isNotNull()
            .doesNotContain(testSubmission);
    }

    @Test
    @Transactional
    public void Should_FindTestSubmission_When_GivenOpenSubmissionState() {
        Iterable<Submission> submissions = submissionRepository.findAllByState(Submission.State.OPEN);

        assertThat(submissions)
            .isNotNull()
            .isNotEmpty()
            .contains(testSubmission);
    }

    @Test
    @Transactional
    public void Should_NotFindTestSubmission_When_GivenDeniedSubmissionState() {
        Iterable<Submission> submissions = submissionRepository.findAllByState(Submission.State.DENIED);

        assertThat(submissions)
            .isNotNull()
            .doesNotContain(testSubmission);
    }
    
}
