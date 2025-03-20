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
@Table(name = "contributions")
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribution_method_id")
    private ContributionMethod contributionMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "initial_value", nullable = true)
    private Integer initialValue;

    @Column(name = "initial_value_screenshot_url", nullable = true, length = 512)
    private String initialValueScreenshotUrl;

    @Column(name = "final_value", nullable = true)
    private Integer finalValue;

    @Column(name = "final_value_screenshot_url", nullable = true, length = 512)
    private String finalValueScreenshotUrl;

    @Column(name = "staff_adjustment")
    private Integer staffAdjustment;

    @Column(name = "unranked_starting_value")
    private Integer unrankedStartingValue;

    public Integer getId() {
        return id;
    }

    public ContributionMethod getContributionMethod() {
        return contributionMethod;
    }

    public Player getPlayer() {
        return player;
    }

    public Integer getInitialValue() {
        return initialValue;
    }

    public String getInitialValueScreenshotUrl() {
        return initialValueScreenshotUrl;
    }

    public Integer getFinalValue() {
        return finalValue;
    }

    public String getFinalValueScreenshotUrl() {
        return finalValueScreenshotUrl;
    }

    public Integer getStaffAdjustment() {
        return staffAdjustment;
    }

    public Integer getUnrankedStartingValue() {
        return unrankedStartingValue;
    }

    public Contribution setContributionMethod(ContributionMethod contributionMethod) {
        this.contributionMethod = contributionMethod;
        return this;
    }

    public Contribution setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public Contribution setInitialValue(Integer initialValue) {
        this.initialValue = initialValue;
        return this;
    }

    public Contribution setInitialValueScreenshotUrl(String initialValueScreenshotUrl) {
        this.initialValueScreenshotUrl = initialValueScreenshotUrl;
        return this;
    }

    public Contribution setFinalValue(Integer finalValue) {
        this.finalValue = finalValue;
        return this;
    }

    public Contribution setFinalValueScreenshotUrl(String finalValueScreenshotUrl) {
        this.finalValueScreenshotUrl = finalValueScreenshotUrl;
        return this;
    }

    public Contribution setStaffAdjustment(Integer staffAdjustment) {
        this.staffAdjustment = staffAdjustment;
        return this;
    }

    public Contribution setUnrankedStartingValue(Integer unrankedStartingValue) {
        this.unrankedStartingValue = unrankedStartingValue;
        return this;
    }
}
