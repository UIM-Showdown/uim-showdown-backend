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
import org.uimshowdown.bingo.enums.SubmissionState;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Submission;
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubmissionRepositoryTests {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SubmissionRepostiory submissionRepostiory;

    private Team testTeam;
    private Player testPlayer;
    private Submission testSubmission;

    @BeforeAll
    public void setUp() {
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
        testSubmission = submissionRepostiory.save(SharedTestVariables.makeTestSubmission(testPlayer));
    }

    @AfterAll
    public void tearDown() {
        submissionRepostiory.delete(testSubmission);
        playerRepository.delete(testPlayer);
        teamRepository.delete(testTeam);
    }

    @Test
    @Transactional
    public void Should_FindTestSubmission_When_GivenTestPlayerId() {
        Iterable<Submission> submissions = submissionRepostiory.findAllByPlayerId(testPlayer.getId());

        assertThat(submissions)
            .isNotNull()
            .isNotEmpty()
            .contains(testSubmission);
    }

    @Test
    @Transactional
    public void Should_NotFindTestSubmission_When_GivenIncorrectPlayerId() {
        Iterable<Submission> submissions = submissionRepostiory.findAllByPlayerId(0);

        assertThat(submissions)
            .isNotNull()
            .doesNotContain(testSubmission);
    }

    @Test
    @Transactional
    public void Should_FindTestSubmission_When_GivenOpenSubmissionState() {
        Iterable<Submission> submissions = submissionRepostiory.findAllByState(SubmissionState.OPEN);

        assertThat(submissions)
            .isNotNull()
            .isNotEmpty()
            .contains(testSubmission);
    }

    @Test
    @Transactional
    public void Should_NotFindTestSubmission_When_GivenDeniedSubmissionState() {
        Iterable<Submission> submissions = submissionRepostiory.findAllByState(SubmissionState.DENIED);

        assertThat(submissions)
            .isNotNull()
            .doesNotContain(testSubmission);
    }
}
