package org.uimshowdown.bingo.repositories;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.RecordSubmission;

public interface RecordSubmissionRepository extends CrudRepository<RecordSubmission, Integer> {
    @NativeQuery("SELECT * FROM record_submissions WHERE handicap_id = ?1")
    Iterable<RecordSubmission> findAllByHandicapId(Integer handicapId);
    Iterable<RecordSubmission> findAllByRecordId(Integer recordId);
    Iterable<RecordSubmission> findAllBySubmissionId(Integer submissionId);
}
