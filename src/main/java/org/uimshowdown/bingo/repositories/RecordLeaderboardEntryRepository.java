package org.uimshowdown.bingo.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.RecordLeaderboardEntry;

public interface RecordLeaderboardEntryRepository extends CrudRepository<RecordLeaderboardEntry, Integer> {
    
    @Query(value = "SELECT * FROM record_leaderboard_entries WHERE skill = ?1 AND value > 0 ORDER BY place ASC", nativeQuery = true)
    Iterable<RecordLeaderboardEntry> findValidEntriesBySkillNameOrderByPlaceAsc(String skillName);
    
}
