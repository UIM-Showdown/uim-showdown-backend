package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.TileProgress;

public interface TileProgressRepository extends CrudRepository<TileProgress, Integer> {
    Iterable<TileProgress> findAllByTeamId(int teamId);
    Iterable<TileProgress> findAllByTileId(int tileId);
}
