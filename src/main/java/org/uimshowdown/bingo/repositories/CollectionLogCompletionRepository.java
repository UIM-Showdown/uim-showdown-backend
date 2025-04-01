package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.CollectionLogCompletion;

public interface CollectionLogCompletionRepository extends CrudRepository<CollectionLogCompletion, Integer> {
    Iterable<CollectionLogCompletion> findAllByItemId(Integer collectionLogItemId);
    Iterable<CollectionLogCompletion> findAllByPlayerId(Integer playerId);
}
