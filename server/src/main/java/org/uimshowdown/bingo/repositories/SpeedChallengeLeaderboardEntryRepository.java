package org.uimshowdown.bingo.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.SpeedChallengeLeaderboardEntry;

public interface SpeedChallengeLeaderboardEntryRepository extends CrudRepository<SpeedChallengeLeaderboardEntry, Integer> {
    
    @Query(value = "SELECT * FROM speed_challenge_leaderboard_entries WHERE challenge_name = ?1 AND seconds > 0 ORDER BY place ASC", nativeQuery = true)
    Iterable<SpeedChallengeLeaderboardEntry> findValidEntriesByChallengeNameOrderByPlaceAsc(String challengeName);
    
}
