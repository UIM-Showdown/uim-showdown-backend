package org.uimshowdown.bingo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.ItemOption;

public interface ItemOptionRepository extends CrudRepository<ItemOption, Integer> {
    public Iterable<ItemOption> findAllByItemId(int itemId);
    public Optional<ItemOption> findByName(String name);
}
