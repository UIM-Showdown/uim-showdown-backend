package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.ChallengeRelayComponent;

public interface ChallangeRelayComponentRepository extends CrudRepository<ChallengeRelayComponent, Integer> {
    public Iterable<ChallengeRelayComponent> findAllByChallengeId(Integer challengeId);
}
