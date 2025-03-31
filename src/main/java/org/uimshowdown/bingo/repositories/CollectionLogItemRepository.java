package org.uimshowdown.bingo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.CollectionLogItem;

public interface CollectionLogItemRepository extends CrudRepository<CollectionLogItem, Integer> {
    Iterable<CollectionLogItem> findAllByGroupId(Integer collectionLogGroupId);
    Optional<CollectionLogItem> findByName(String name);
}
