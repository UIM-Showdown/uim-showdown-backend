package org.uimshowdown.bingo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.Submission;

public interface SubmissionRepository extends CrudRepository<Submission, Integer> {
    Iterable<Submission> findAllByPlayerId(int playerId);
    Iterable<Submission> findAllByState(Submission.State state);
    
    @Query(value = "SELECT * FROM submissions WHERE player_id = ?1 AND contribution_method_id = ?2 AND id != ?3 AND state = 'APPROVED' AND type = 'CONTRIBUTION' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<Submission> getPreviousContributionSubmission(int playerId, int methodId, int currentSubmissionId);
    
    @Query(value = "SELECT * FROM submissions WHERE player_id = ?1 AND contribution_method_id = ?2 AND id != ?3 AND state = 'APPROVED' AND type = 'UNRANKED_STARTING_VALUE' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<Submission> getPreviousUnrankedStartingValueSubmission(int playerId, int methodId, int currentSubmissionId);
    
    @Query(value = "SELECT * FROM submissions WHERE player_id = ?1 AND challenge_id = ?2 AND id != ?3 AND state = 'APPROVED' AND type = 'CHALLENGE' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<Submission> getPreviousSpeedrunChallengeSubmission(int playerId, int challengeId, int currentSubmissionId);
    
    @Query(value = "SELECT * FROM submissions WHERE player_id = ?1 AND challenge_id = ?2 AND challenge_relay_component_id = ?3 AND id != ?4 AND state = 'APPROVED' AND type = 'CHALLENGE' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<Submission> getPreviousRelayChallengeSubmission(int playerId, int challengeId, int relayComponentId, int currentSubmissionId);
    
    @Query(value = "SELECT * FROM submissions WHERE player_id = ?1 AND record_id = ?2 AND id != ?3 AND state = 'APPROVED' AND type = 'RECORD' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<Submission> getPreviousRecordSubmission(int playerId, int recordId, int currentSubmissionId);
}
