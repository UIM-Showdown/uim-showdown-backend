package org.uimshowdown.bingo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.Tile;

public interface TileRepository extends CrudRepository<Tile, Integer> {
    Optional<Tile> findByName(String name);
}
