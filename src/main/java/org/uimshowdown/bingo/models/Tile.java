package org.uimshowdown.bingo.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
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
    
    @Column(length = 8)
    private String abbreviation;

    @Column(name = "points_per_tier")
    private int pointsPerTier;

    @OneToMany(mappedBy = "tile", cascade = CascadeType.ALL)
    private Set<ContributionMethod> contributionMethods = new HashSet<ContributionMethod>();

    @OneToMany(mappedBy = "tile", cascade = CascadeType.ALL)
    private Set<TileProgress> progress = new HashSet<TileProgress>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public String getAbbreviation() {
        return abbreviation;
    }

    public int getPointsPerTier() {
        return pointsPerTier;
    }

    public Set<ContributionMethod> getContributionMethods() {
        return contributionMethods;
    }

    public void setContributionMethods(Set<ContributionMethod> contributionMethods) {
        this.contributionMethods = contributionMethods;
    }

    public Set<TileProgress> getProgress() {
        return progress;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setPointsPerTier(int pointsPerTier) {
        this.pointsPerTier = pointsPerTier;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Tile && ((Tile) obj).getId() == this.id;
    }
    
}
