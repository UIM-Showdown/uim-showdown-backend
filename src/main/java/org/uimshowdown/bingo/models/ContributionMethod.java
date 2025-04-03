package org.uimshowdown.bingo.models;

import java.util.Objects;
import java.util.Set;

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
    private Integer id;

    @OneToMany(mappedBy = "contributionMethod")
    private Set<ContributionSubmission> contributionSubmissions;

    @OneToMany(mappedBy = "contributionMethod")
    private Set<Contribution> playerContributions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tile_id")
    private Tile tile;

    @OneToMany(mappedBy = "contributionMethod")
    private Set<UnrankedStartingValueSubmission> unrankedStartingValueSubmissions;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category contributionMethodCategory;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type contributionMethodType;

    @Column(name = "eht_rate")
    private Double ehtRate;

    @Column(length = 64)
    private String name;

    public Integer getId() {
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

    public Double getEhtRate() {
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

    public void setEhtRate(Double ehtRate) {
        this.ehtRate = ehtRate;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof ContributionMethod) == false) {
            return false;
        }

        ContributionMethod otherContributionMethod = (ContributionMethod) obj;
        return Integer.compare(id, otherContributionMethod.getId()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contributionMethodCategory, contributionMethodType, ehtRate, name);
    }
}
