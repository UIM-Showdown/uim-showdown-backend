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
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Record;
import org.uimshowdown.bingo.models.RecordHandicap;
import org.uimshowdown.bingo.models.RecordSubmission;
import org.uimshowdown.bingo.models.Submission;
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
public class RecordSubmissionRepositoryTests {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RecordHandicapRepository recordHandicapRepository;

    @Autowired
    private RecordSubmissionRepository recordSubmissionRepository;

    @Autowired
    private SubmissionRepostiory submissionRepostiory;

    @Autowired
    private TeamRepository teamRepository;

    private Player testPlayer;
    private Record testRecord;
    private RecordHandicap testRecordHandicap;
    private RecordSubmission testRecordSubmission;
    private Submission testSubmission;
    private Team testTeam;

    @BeforeEach
    public void setUp() {
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
        testRecord = recordRepository.save(SharedTestVariables.makeTestRecord());
        testRecordHandicap = recordHandicapRepository.save(SharedTestVariables.makeTestRecordHandicap(testRecord));
        testSubmission = submissionRepostiory.save(SharedTestVariables.makeTestSubmission(testPlayer));
        testRecordSubmission = recordSubmissionRepository.save(SharedTestVariables.makeTestRecordSubmission(testRecordHandicap, testRecord, testSubmission));
    }

    @AfterEach
    public void tearDown() {
        recordSubmissionRepository.delete(testRecordSubmission);
        submissionRepostiory.delete(testSubmission);
        recordHandicapRepository.delete(testRecordHandicap);
        recordRepository.delete(testRecord);
        playerRepository.delete(testPlayer);
        teamRepository.delete(testTeam);
    }

    @Test
    @Transactional
    public void Should_FindTestRecordSubmission_When_GivenTestRecordId() {
        Iterable<RecordSubmission> recordSubmissions = recordSubmissionRepository.findAllByRecordId(testRecord.getId());

        assertThat(recordSubmissions)
            .isNotNull()
            .isNotEmpty()
            .contains(testRecordSubmission);
    }

    @Test
    @Transactional
    public void Should_NotFindTestRecordSubmission_When_GivenWrongRecordId() {
        Iterable<RecordSubmission> recordSubmissions = recordSubmissionRepository.findAllByRecordId(0);

        assertThat(recordSubmissions)
            .isNotNull()
            .doesNotContain(testRecordSubmission);
    }

    @Test
    @Transactional
    public void Should_FindTestRecordSubmission_When_GivenTestRecordHandicapId() {
        Iterable<RecordSubmission> recordSubmissions = recordSubmissionRepository.findAllByHandicapId(testRecordHandicap.getId());

        assertThat(recordSubmissions)
            .isNotNull()
            .isNotEmpty()
            .contains(testRecordSubmission);
    }

    @Test
    @Transactional
    public void Should_NotFindTestRecordSubmission_When_GivenWrongRecordHandicapId() {
        Iterable<RecordSubmission> recordSubmissions = recordSubmissionRepository.findAllByHandicapId(0);

        assertThat(recordSubmissions)
            .isNotNull()
            .doesNotContain(testRecordSubmission);
    }

    @Test
    @Transactional
    public void Should_FindTestRecordSubmission_When_GivenTestSubmissionId() {
        Iterable<RecordSubmission> recordSubmissions = recordSubmissionRepository.findAllBySubmissionId(testSubmission.getId());

        assertThat(recordSubmissions)
            .isNotNull()
            .isNotEmpty()
            .contains(testRecordSubmission);
    }

    @Test
    @Transactional
    public void Should_NotFindTestRecordSubmission_When_GivenWrongSubmissionId() {
        Iterable<RecordSubmission> recordSubmissions = recordSubmissionRepository.findAllBySubmissionId(0);

        assertThat(recordSubmissions)
            .isNotNull()
            .doesNotContain(testRecordSubmission);
    }
}
