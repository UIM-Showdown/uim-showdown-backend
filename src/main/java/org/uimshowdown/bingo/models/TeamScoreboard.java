package org.uimshowdown.bingo.models;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "team_scoreboards")
public class TeamScoreboard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "event_points")
    private int eventPoints;

    @Column(name = "event_points_from_challenges")
    private int eventPointsFromChallenges;

    @Column(name = "event_points_from_collection_log_items")
    private int eventPointsFromCollectionLogItems;

    @Column(name = "event_points_from_groups")
    private int eventPointsFromGroups;

    @Column(name = "event_points_from_records")
    private int eventPointsFromRecords;

    @Column(name = "event_points_from_tiles")
    private int eventPointsFromTiles;
    
    @OneToMany(mappedBy = "teamScoreboard", cascade = CascadeType.ALL)
    private Set<RecordLeaderboardEntry> recordLeaderboardEntries;
    
    @OneToMany(mappedBy = "teamScoreboard", cascade = CascadeType.ALL)
    private Set<ChallengeLeaderboardEntry> challengeLeaderboardEntries;

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public int getEventPoints() {
        return eventPoints;
    }

    public int getEventPointsFromChallenges() {
        return eventPointsFromChallenges;
    }

    public int getEventPointsFromCollectionLogItems() {
        return eventPointsFromCollectionLogItems;
    }

    public int getEventPointsFromGroups() {
        return eventPointsFromGroups;
    }

    public int getEventPointsFromRecords() {
        return eventPointsFromRecords;
    }

    public int getEventPointsFromTiles() {
        return eventPointsFromTiles;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setEventPoints(int points) {
        eventPoints = points;
    }

    public void setEventPointsFromChallenges(int points) {
        eventPointsFromChallenges = points;
    }

    public void setEventPointsFromCollectionLogItems(int points) {
        eventPointsFromCollectionLogItems = points;
    }

    public void setEventPointsFromGroups(int points) {
        eventPointsFromGroups = points;
    }

    public void setEventPointsFromRecords(int points) {
        eventPointsFromRecords = points;
    }

    public void setEventPointsFromTiles(int points) {
        eventPointsFromTiles = points;
    }

    public Set<RecordLeaderboardEntry> getRecordLeaderboardEntries() {
        return recordLeaderboardEntries;
    }

    public void setRecordLeaderboardEntries(Set<RecordLeaderboardEntry> recordLeaderboardEntries) {
        this.recordLeaderboardEntries = recordLeaderboardEntries;
    }

    public Set<ChallengeLeaderboardEntry> getChallengeLeaderboardEntries() {
        return challengeLeaderboardEntries;
    }

    public void setChallengeLeaderboardEntries(Set<ChallengeLeaderboardEntry> challengeLeaderboardEntries) {
        this.challengeLeaderboardEntries = challengeLeaderboardEntries;
    }
    
    public RecordLeaderboardEntry getRecordLeaderboardEntry(Record record) {
        for(RecordLeaderboardEntry entry : recordLeaderboardEntries) {
            if(entry.getSkill() == record.getSkill()) {
                return entry;
            }
        }
        return null;
    }
    
    public ChallengeLeaderboardEntry getChallengeLeaderboardEntry(Challenge challenge) {
        for(ChallengeLeaderboardEntry entry : challengeLeaderboardEntries) {
            if(entry.getChallengeName() == challenge.getName()) {
                return entry;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof TeamScoreboard && ((TeamScoreboard) obj).getId() == this.id;
    }
    
}
