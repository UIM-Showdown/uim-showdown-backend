package org.uimshowdown.bingo.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.ChallengeLeaderboardEntry;

public interface ChallengeLeaderboardEntryRepository extends CrudRepository<ChallengeLeaderboardEntry, Integer> {
    
    @Query(value = "SELECT * FROM challenge_leaderboard_entries WHERE challenge_name = ?1 AND seconds > 0 ORDER BY place ASC", nativeQuery = true)
    Iterable<ChallengeLeaderboardEntry> findValidEntriesByChallengeNameOrderByPlaceAsc(String challengeName);
    
}
