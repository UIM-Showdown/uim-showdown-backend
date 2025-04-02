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
@Table(name = "player_scoreboards")
public class PlayerScoreboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "collection_log_points")
    private Integer collectionLogPoints;

    @Column(name = "other_tile_contribution")
    private Double otherTileContribution;

    @Column(name = "pvm_tile_contribution")
    private Double pvmTileContribution;

    @Column(name = "skilling_tile_contribution")
    private Double skillingTileContribution;

    @Column(name = "total_tile_contribution")
    private Double totalTileContribution;

    public Integer getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public Integer getCollectionLogPoints() {
        return collectionLogPoints;
    }

    public Double getOtherTileContribution() {
        return otherTileContribution;
    }

    public Double getPvmTileContribution() {
        return pvmTileContribution;
    }

    public Double getSkillingTileContribution() {
        return skillingTileContribution;
    }

    public Double getTotalTileContribution() {
        return totalTileContribution;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setCollectionLogPoints(Integer points) {
        collectionLogPoints = points;
    }

    public void setOtherTileContribution(Double points) {
        otherTileContribution = points;
    }

    public void setPvmTileContribution(Double points) {
        pvmTileContribution = points;
    }

    public void setSkillingTitleContribution(Double points) {
        skillingTileContribution = points;
    }

    public void setTotalTileContribution(Double points) {
        totalTileContribution = points;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof PlayerScoreboard) == false) {
            return false;
        }

        PlayerScoreboard otherPlayerScoreboard = (PlayerScoreboard) obj;
        return Integer.compare(id, otherPlayerScoreboard.getId()) == 0;
    }
    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            collectionLogPoints,
            otherTileContribution,
            pvmTileContribution,
            skillingTileContribution,
            totalTileContribution
        );
    }
}
