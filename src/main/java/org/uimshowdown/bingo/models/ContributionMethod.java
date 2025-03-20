package org.uimshowdown.bingo.models;

import java.util.Set;

import org.uimshowdown.bingo.enums.ContributionMethodCategory;
import org.uimshowdown.bingo.enums.ContributionMethodType;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "contributionMethod")
    private Set<Contribution> playerContributions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tile_id")
    private Tile tile;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private ContributionMethodCategory contributionMethodCategory;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ContributionMethodType contributionMethodType;

    @Column(name = "eht_rate")
    private Float ehtRate;

    @Column(length = 64)
    private String name;

    public Integer getId() {
        return id;
    }

    public Set<Contribution> getPlayerContributions() {
        return playerContributions;
    }

    public Tile getTile() {
        return tile;
    }

    public ContributionMethodCategory getContributionMethodCategory() {
        return contributionMethodCategory;
    }

    public ContributionMethodType getContributionMethodType() {
        return contributionMethodType;
    }

    public Float getEhtRate() {
        return ehtRate;
    }

    public String getName() {
        return name;
    }

    public ContributionMethod setTile(Tile tile) {
        this.tile = tile;
        return this;
    }

    public ContributionMethod setContributionMethodCategory(ContributionMethodCategory category) {
        contributionMethodCategory = category;
        return this;
    }

    public ContributionMethod setContributionMethodType(ContributionMethodType type) {
        contributionMethodType = type;
        return this;
    }

    public ContributionMethod setEhtRate(Float ehtRate) {
        this.ehtRate = ehtRate;
        return this;
    }

    public ContributionMethod setName(String name) {
        this.name = name;
        return this;
    }
}
