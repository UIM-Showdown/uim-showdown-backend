package org.uimshowdown.bingo.models;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "contribution_methods")
public class ContributionMethod {
	
	public enum Category { PVM, SKILLING, OTHER }
	
	public enum Type { KC, SUBMISSION, XP }
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private int id;

    @OneToMany(mappedBy = "contributionMethod")
    @JsonIgnore
    private Set<ContributionSubmission> contributionSubmissions;

    @OneToMany(mappedBy = "contributionMethod")
    @JsonIgnore
    private Set<Contribution> playerContributions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tile_id")
    @JsonIgnore
    private Tile tile;

    @OneToMany(mappedBy = "contributionMethod")
    @JsonIgnore
    private Set<UnrankedStartingValueSubmission> unrankedStartingValueSubmissions;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    @JsonProperty
    private Category contributionMethodCategory;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @JsonProperty
    private Type contributionMethodType;

    @Column(name = "eht_rate")
    @JsonProperty
    private double ehtRate;

    @Column(length = 64)
    @JsonProperty
    private String name;
    
    @Column(length = 64)
    @JsonProperty
    private String templeId;

    public int getId() {
        return id;
    }

    public Set<ContributionSubmission> getContributionSubmissions() {
        return contributionSubmissions;
    }

    public Set<Contribution> getPlayerContributions() {
        return playerContributions;
    }

    public Tile getTile() {
        return tile;
    }

    public Set<UnrankedStartingValueSubmission> getUnrankedStartingValueSubmissions() {
        return unrankedStartingValueSubmissions;
    }

    public Category getContributionMethodCategory() {
        return contributionMethodCategory;
    }

    public Type getContributionMethodType() {
        return contributionMethodType;
    }

    public double getEhtRate() {
        return ehtRate;
    }

    public String getName() {
        return name;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setContributionMethodCategory(Category category) {
        contributionMethodCategory = category;
    }

    public void setContributionMethodType(Type type) {
        contributionMethodType = type;
    }

    public void setEhtRate(double ehtRate) {
        this.ehtRate = ehtRate;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("tilePointsPerContribution")
    public int getTilePointsPerContribution() {
    	int ehtPerTier = 30; // TODO get from config
    	return (int) (this.tile.getPointsPerTier() / ehtPerTier / this.ehtRate);
    }

    @Override
    public boolean equals(Object obj) {
    	return obj != null && obj instanceof ContributionMethod && ((ContributionMethod) obj).getId() == this.id;
    }

}
