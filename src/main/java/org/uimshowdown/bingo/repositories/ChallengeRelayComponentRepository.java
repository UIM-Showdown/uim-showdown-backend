package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.ChallengeRelayComponent;

public interface ChallengeRelayComponentRepository extends CrudRepository<ChallengeRelayComponent, Integer> {
    public Iterable<ChallengeRelayComponent> findAllByChallengeId(int challengeId);
}
