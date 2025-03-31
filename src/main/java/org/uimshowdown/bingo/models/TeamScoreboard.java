package org.uimshowdown.bingo.models;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "team_scoreboards")
public class TeamScoreboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "event_points")
    private Integer eventPoints;

    @Column(name = "event_points_from_challenges")
    private Integer eventPointsFromChallenges;

    @Column(name = "event_points_from_collection_log_items")
    private Integer eventPointsFromCollectionLogItems;

    @Column(name = "event_points_from_groups")
    private Integer eventPointsFromGroups;

    @Column(name = "event_points_from_records")
    private Integer eventPointsFromRecords;

    @Column(name = "event_points_from_tiles")
    private Integer eventPointsFromTiles;

    public Integer getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public Integer getEventPoints() {
        return eventPoints;
    }

    public Integer getEventPointsFromChallenges() {
        return eventPointsFromChallenges;
    }

    public Integer getEventPointsFromCollectionLogItems() {
        return eventPointsFromCollectionLogItems;
    }

    public Integer getEventPointsFromGroups() {
        return eventPointsFromGroups;
    }

    public Integer getEventPointsFromRecords() {
        return eventPointsFromRecords;
    }

    public Integer getEventPointsFromTiles() {
        return eventPointsFromTiles;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setEventPoints(Integer points) {
        eventPoints = points;
    }

    public void setEventPointsFromChallenge(Integer points) {
        eventPointsFromChallenges = points;
    }

    public void setEventPointsFromCollectionLogItems(Integer points) {
        eventPointsFromCollectionLogItems = points;
    }

    public void setEventPointsFromGroups(Integer points) {
        eventPointsFromGroups = points;
    }

    public void setEventPointsFromRecords(Integer points) {
        eventPointsFromRecords = points;
    }

    public void setEventPointsFromTiles(Integer points) {
        eventPointsFromTiles = points;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof TeamScoreboard) == false) {
            return false;
        }

        TeamScoreboard otherTeamScoreboard = (TeamScoreboard) obj;
        return (
            Integer.compare(id, otherTeamScoreboard.getId()) == 0
            && Integer.compare(eventPoints, otherTeamScoreboard.getEventPoints()) == 0
            && Integer.compare(eventPointsFromChallenges, otherTeamScoreboard.getEventPointsFromChallenges()) == 0
            && Integer.compare(eventPointsFromCollectionLogItems, otherTeamScoreboard.getEventPointsFromCollectionLogItems()) == 0
            && Integer.compare(eventPointsFromGroups, otherTeamScoreboard.getEventPointsFromGroups()) == 0
            && Integer.compare(eventPointsFromRecords, otherTeamScoreboard.getEventPointsFromRecords()) == 0
            && Integer.compare(eventPointsFromTiles, otherTeamScoreboard.getEventPointsFromTiles()) == 0
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            eventPoints,
            eventPointsFromChallenges,
            eventPointsFromCollectionLogItems,
            eventPointsFromGroups,
            eventPointsFromRecords,
            eventPointsFromTiles
        );
    }
}
