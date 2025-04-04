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
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribution_method_id")
    private ContributionMethod contributionMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "initial_value", nullable = true)
    private int initialValue;

    @Column(name = "initial_value_screenshot_url", nullable = true, length = 512)
    private String initialValueScreenshotUrl;

    @Column(name = "final_value", nullable = true)
    private int finalValue;

    @Column(name = "final_value_screenshot_url", nullable = true, length = 512)
    private String finalValueScreenshotUrl;

    @Column(name = "staff_adjustment")
    private int staffAdjustment;

    @Column(name = "unranked_starting_value")
    private int unrankedStartingValue;

    public int getId() {
        return id;
    }

    public ContributionMethod getContributionMethod() {
        return contributionMethod;
    }

    public Player getPlayer() {
        return player;
    }

    public int getInitialValue() {
        return initialValue;
    }

    public String getInitialValueScreenshotUrl() {
        return initialValueScreenshotUrl;
    }

    public int getFinalValue() {
        return finalValue;
    }

    public String getFinalValueScreenshotUrl() {
        return finalValueScreenshotUrl;
    }

    public int getStaffAdjustment() {
        return staffAdjustment;
    }

    public int getUnrankedStartingValue() {
        return unrankedStartingValue;
    }

    public void setContributionMethod(ContributionMethod contributionMethod) {
        this.contributionMethod = contributionMethod;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setInitialValue(int initialValue) {
        this.initialValue = initialValue;
    }

    public void setInitialValueScreenshotUrl(String initialValueScreenshotUrl) {
        this.initialValueScreenshotUrl = initialValueScreenshotUrl;
    }

    public void setFinalValue(int finalValue) {
        this.finalValue = finalValue;
    }

    public void setFinalValueScreenshotUrl(String finalValueScreenshotUrl) {
        this.finalValueScreenshotUrl = finalValueScreenshotUrl;
    }

    public void setStaffAdjustment(int staffAdjustment) {
        this.staffAdjustment = staffAdjustment;
    }

    public void setUnrankedStartingValue(int unrankedStartingValue) {
        this.unrankedStartingValue = unrankedStartingValue;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Contribution && ((Contribution) obj).getId() == this.id;
    }
    
}
