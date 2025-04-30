package org.uimshowdown.bingo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.CollectionLogItem;

public interface CollectionLogItemRepository extends CrudRepository<CollectionLogItem, Integer> {
    Iterable<CollectionLogItem> findByOrderByIdAsc();
    Optional<CollectionLogItem> findByName(String name);
    
    @Query(value = "SELECT collection_log_items.* FROM collection_log_items LEFT JOIN item_options ON collection_log_items.id = item_options.item_id WHERE collection_log_items.name = ?1 OR item_options.name = ?1 LIMIT 1", nativeQuery = true)
    Optional<CollectionLogItem> findByNameOrOption(String nameOrOption);
}
