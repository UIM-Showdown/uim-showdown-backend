package org.uimshowdown.bingo.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.PointsChallengeLeaderboardEntry;

public interface PointsChallengeLeaderboardEntryRepository extends CrudRepository<PointsChallengeLeaderboardEntry, Integer> {
    
    @Query(value = "SELECT * FROM points_challenge_leaderboard_entries WHERE challenge_name = ?1 AND points > 0 ORDER BY place ASC", nativeQuery = true)
    Iterable<PointsChallengeLeaderboardEntry> findValidEntriesByChallengeNameOrderByPlaceAsc(String challengeName);
    
}
