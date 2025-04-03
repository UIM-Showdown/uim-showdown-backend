package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.RecordCompletion;

public interface RecordCompletionRepository extends CrudRepository<RecordCompletion, Integer> {
    Iterable<RecordCompletion> findAllByHandicapId(int handicapId);
    Iterable<RecordCompletion> findAllByPlayerId(int playerId);
    Iterable<RecordCompletion> findAllByRecordId(int recordId);
}
