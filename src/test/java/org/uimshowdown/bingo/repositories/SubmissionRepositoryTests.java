package org.uimshowdown.bingo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.uimshowdown.bingo.enums.SubmissionState;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Submission;
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
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

    @BeforeEach
    public void setUp() {
        testTeam = new Team()
            .setAbbreviation("ffs")
            .setColor("c97632")
            .setName("Falador Fullsends");
        testTeam = teamRepository.save(testTeam);

        testPlayer = new Player()
            .setCaptainStatus(true)
            .setDiscordName("flashcards")
            .setRsn("Flashcards")
            .setTeam(testTeam);
        testPlayer = playerRepository.save(testPlayer);

        testSubmission = new Submission()
            .setPlayer(testPlayer)
            .setSubmissionState(SubmissionState.OPEN);
        testSubmission = submissionRepostiory.save(testSubmission);
    }

    @AfterEach
    public void tearDown() {
        teamRepository.delete(testTeam);
        playerRepository.delete(testPlayer);
        submissionRepostiory.delete(testSubmission);
    }

    @Test
    @Transactional
    public void Should_FindTestSubmission_When_GivenTestPlayer() {
        Iterable<Submission> submissions = submissionRepostiory.findAllByPlayerId(testPlayer.getId());

        assertThat(submissions)
            .isNotNull()
            .isNotEmpty()
            .contains(testSubmission);
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
