package org.uimshowdown.bingo.models;

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
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "collection_log_points")
    private int collectionLogPoints;

    @Column(name = "other_tile_contribution")
    private double otherTileContribution;

    @Column(name = "pvm_tile_contribution")
    private double pvmTileContribution;

    @Column(name = "skilling_tile_contribution")
    private double skillingTileContribution;

    @Column(name = "total_tile_contribution")
    private double totalTileContribution;

    public int getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public int getCollectionLogPoints() {
        return collectionLogPoints;
    }

    public double getOtherTileContribution() {
        return otherTileContribution;
    }

    public double getPvmTileContribution() {
        return pvmTileContribution;
    }

    public double getSkillingTileContribution() {
        return skillingTileContribution;
    }

    public double getTotalTileContribution() {
        return totalTileContribution;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setCollectionLogPoints(int points) {
        collectionLogPoints = points;
    }

    public void setOtherTileContribution(double points) {
        otherTileContribution = points;
    }

    public void setPvmTileContribution(double points) {
        pvmTileContribution = points;
    }

    public void setSkillingTitleContribution(double points) {
        skillingTileContribution = points;
    }

    public void setTotalTileContribution(double points) {
        totalTileContribution = points;
    }

    @Override
    public boolean equals(Object obj) {
    	return obj != null && obj instanceof PlayerScoreboard && ((PlayerScoreboard) obj).getId() == this.id;
    }
    
}
