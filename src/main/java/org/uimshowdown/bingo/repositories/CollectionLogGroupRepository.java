package org.uimshowdown.bingo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.CollectionLogGroup;

public interface CollectionLogGroupRepository extends CrudRepository<CollectionLogGroup, Integer> {
    Optional<CollectionLogGroup> findByName(String name);
    Iterable<CollectionLogGroup> findByOrderByIdAsc();
}
