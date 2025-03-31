package org.uimshowdown.bingo.models;

import java.util.Objects;

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
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tile_id")
    private Tile tile;

    @Column(name = "percentage_to_next_tier")
    private Double percentageToNextTier;

    @Column
    private Integer points;

    @Column
    private Integer tier;

    public Integer getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public Tile getTile() {
        return tile;
    }

    public Double getPercentageToNextTier() {
        return percentageToNextTier;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getTier() {
        return tier;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setPercentageToNextTier(Double percentageToNextTier) {
        this.percentageToNextTier = percentageToNextTier;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof TileProgress) == false) {
            return false;
        }

        TileProgress otherTileProgress = (TileProgress) obj;
        return (
            getId() instanceof Integer ? getId().equals(otherTileProgress.getId()) : getId() == otherTileProgress.getId()
            && Double.compare(getPercentageToNextTier(), otherTileProgress.getPercentageToNextTier()) == 0
            && getPoints() instanceof Integer ? getPoints().equals(otherTileProgress.getPoints()) : getPoints() == otherTileProgress.getPoints()
            && getTier() instanceof Integer ? getTier().equals(otherTileProgress.getTier()) : getTier() == otherTileProgress.getTier()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, percentageToNextTier, points, tier);
    }
}
