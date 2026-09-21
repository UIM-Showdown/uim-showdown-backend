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
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Record;
import org.uimshowdown.bingo.models.RecordCompletion;
import org.uimshowdown.bingo.models.RecordHandicap;
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class RecordCompletionRepositoryTests {
    
    @Autowired
    private TestUtils testUtils;
    
    @Autowired
    private PlayerRepository playerRepository;
    
    @Autowired
    private RecordRepository recordRepository;
    
    @Autowired
    private RecordCompletionRepository recordCompletionRepository;

    @Autowired
    private RecordHandicapRepository recordHandicapRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Player testPlayer;
    private Record testRecord;
    private RecordCompletion testRecordCompletion;
    private RecordHandicap testRecordHandicap;
    private Team testTeam;

    @BeforeAll
    public void setUp() {
        testUtils.resetDB();
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
        testRecord = recordRepository.save(SharedTestVariables.makeTestRecord());
        testRecordHandicap = recordHandicapRepository.save(SharedTestVariables.makeTestRecordHandicap(testRecord));
        testRecordCompletion = recordCompletionRepository.save(SharedTestVariables.makeTestRecordCompletion(testPlayer, testRecord, testRecordHandicap));
    }

    @AfterAll
    public void tearDown() {
        testUtils.resetDB();
    }

    @Test
    @Transactional
    public void Should_FindTestRecordCompletion_When_GivenTestHandicapId() {
        Iterable<RecordCompletion> recordCompletions = recordCompletionRepository.findAllByHandicapId(testRecordHandicap.getId());

        assertThat(recordCompletions)
            .isNotNull()
            .isNotEmpty()
            .contains(testRecordCompletion);
    }

    @Test
    @Transactional
    public void Should_NotFindTestRecordCompletion_When_GivenWrongHandicapId() {
        Iterable<RecordCompletion> recordCompletions = recordCompletionRepository.findAllByHandicapId(0);

        assertThat(recordCompletions)
            .isNotNull()
            .doesNotContain(testRecordCompletion);
    }

    @Test
    @Transactional
    public void Should_FindTestRecordCompletion_When_GivenTestPlayerId() {
        Iterable<RecordCompletion> recordCompletions = recordCompletionRepository.findAllByPlayerId(testPlayer.getId());

        assertThat(recordCompletions)
            .isNotNull()
            .isNotEmpty()
            .contains(testRecordCompletion);
    }

    @Test
    @Transactional
    public void Should_NotFindTestRecordCompletion_When_GivenWrongPlayerId() {
        Iterable<RecordCompletion> recordCompletions = recordCompletionRepository.findAllByPlayerId(0);

        assertThat(recordCompletions)
            .isNotNull()
            .doesNotContain(testRecordCompletion);
    }

    @Test
    @Transactional
    public void Should_FindTestRecordCompletion_When_GivenTestRecordId() {
        Iterable<RecordCompletion> recordCompletions = recordCompletionRepository.findAllByRecordId(testRecord.getId());

        assertThat(recordCompletions)
            .isNotNull()
            .isNotEmpty()
            .contains(testRecordCompletion);
    }

    @Test
    @Transactional
    public void Should_NotFindTestRecordCompletion_When_GivenWrongRecordId() {
        Iterable<RecordCompletion> recordCompletions = recordCompletionRepository.findAllByRecordId(0);

        assertThat(recordCompletions)
            .isNotNull()
            .doesNotContain(testRecordCompletion);
    }
    
}
