package org.uimshowdown.bingo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.ContributionMethod;

public interface ContributionMethodRepository extends CrudRepository<ContributionMethod, Integer> {
    Optional<ContributionMethod> findByName(String name);
    Iterable<ContributionMethod> findAllByTileId(Integer tileId);
}
