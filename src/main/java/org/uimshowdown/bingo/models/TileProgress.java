package org.uimshowdown.bingo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tile_progress")
public class TileProgress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tile_id")
    private Tile tile;

    @Column(name = "percentage_to_next_tier")
    private double percentageToNextTier;

    @Column
    private int points;

    @Column
    private int tier;

    public int getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public Tile getTile() {
        return tile;
    }

    public double getPercentageToNextTier() {
        return percentageToNextTier;
    }

    public int getPoints() {
        return points;
    }

    public int getTier() {
        return tier;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setPercentageToNextTier(double percentageToNextTier) {
        this.percentageToNextTier = percentageToNextTier;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof TileProgress && ((TileProgress) obj).getId() == this.id;
    }
    
}
