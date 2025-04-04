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
import org.uimshowdown.bingo.models.Record;
import org.uimshowdown.bingo.models.RecordHandicap;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecordHandicapRepositoryTests {
    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RecordHandicapRepository recordHandicapRepository;

    private Record testRecord;
    private RecordHandicap testRecordHandicap;

    @BeforeAll
    public void setUp() {
    	recordRepository.deleteAll();
    	recordHandicapRepository.deleteAll();
        testRecord = recordRepository.save(SharedTestVariables.makeTestRecord());
        testRecordHandicap = recordHandicapRepository.save(SharedTestVariables.makeTestRecordHandicap(testRecord));
    }

    @AfterAll
    public void tearDown() {
        recordHandicapRepository.delete(testRecordHandicap);
        recordRepository.delete(testRecord);
    }

    @Test
    @Transactional
    public void Should_FindTestRecordHandicap_When_GivenTestRecordId() {
        Iterable<RecordHandicap> recordHandicaps = recordHandicapRepository.findAllByRecordId(testRecord.getId());

        assertThat(recordHandicaps)
            .isNotNull()
            .isNotEmpty()
            .contains(testRecordHandicap);
    }

    @Test
    @Transactional
    public void Should_NotFindTestRecordHandicap_When_GivenWrongRecordId() {
        Iterable<RecordHandicap> recordHandicaps = recordHandicapRepository.findAllByRecordId(0);

        assertThat(recordHandicaps)
            .isNotNull()
            .doesNotContain(testRecordHandicap);
    }
}
