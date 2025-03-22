package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.RecordCompletion;

public interface RecordCompletionRepository extends CrudRepository<RecordCompletion, Integer> {
    Iterable<RecordCompletion> findAllByHandicapId(Integer handicapId);
    Iterable<RecordCompletion> findAllByPlayerId(Integer playerId);
    Iterable<RecordCompletion> findAllByRecordId(Integer recordId);
}
