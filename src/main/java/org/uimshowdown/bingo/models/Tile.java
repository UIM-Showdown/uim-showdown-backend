package org.uimshowdown.bingo.models;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tiles")
public class Tile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 64)
    private String name;

    @Column(name = "points_per_tier")
    private Integer pointsPerTier;

    @OneToMany(mappedBy = "tile")
    private Set<ContributionMethod> contributionMethods;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPointsPerTier() {
        return pointsPerTier;
    }

    public Set<ContributionMethod> getContributionMethods() {
        return contributionMethods;
    }
    
    public Tile setName(String name) {
        this.name = name;
        return this;
    }

    public Tile setPointsPerTier(Integer pointsPerTier) {
        this.pointsPerTier = pointsPerTier;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof Tile) == false) {
            return false;
        }

        Tile otherTile = (Tile) obj;
        return (
            getId() == otherTile.getId()
            && getName() == otherTile.getName()
            && getPointsPerTier() == otherTile.getPointsPerTier()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pointsPerTier);
    }
}
