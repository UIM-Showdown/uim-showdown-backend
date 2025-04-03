package org.uimshowdown.bingo.models;

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
    private int id;

    @Column(length = 64)
    private String name;

    @Column(name = "points_per_tier")
    private int pointsPerTier;

    @OneToMany(mappedBy = "tile")
    private Set<ContributionMethod> contributionMethods;

    @OneToMany(mappedBy = "tile")
    private Set<TileProgress> progress;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPointsPerTier() {
        return pointsPerTier;
    }

    public Set<ContributionMethod> getContributionMethods() {
        return contributionMethods;
    }

    public Set<TileProgress> getProgress() {
        return progress;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setPointsPerTier(int pointsPerTier) {
        this.pointsPerTier = pointsPerTier;
    }

    @Override
    public boolean equals(Object obj) {
    	return obj != null && obj instanceof Tile && ((Tile) obj).getId() == this.id;
    }
    
}
