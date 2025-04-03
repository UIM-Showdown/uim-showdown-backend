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

    public void setContributionMethod(ContributionMethod contributionMethod) {
        this.contributionMethod = contributionMethod;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setInitialValue(Integer initialValue) {
        this.initialValue = initialValue;
    }

    public void setInitialValueScreenshotUrl(String initialValueScreenshotUrl) {
        this.initialValueScreenshotUrl = initialValueScreenshotUrl;
    }

    public void setFinalValue(Integer finalValue) {
        this.finalValue = finalValue;
    }

    public void setFinalValueScreenshotUrl(String finalValueScreenshotUrl) {
        this.finalValueScreenshotUrl = finalValueScreenshotUrl;
    }

    public void setStaffAdjustment(Integer staffAdjustment) {
        this.staffAdjustment = staffAdjustment;
    }

    public void setUnrankedStartingValue(Integer unrankedStartingValue) {
        this.unrankedStartingValue = unrankedStartingValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof Contribution) == false) {
            return false;
        }

        Contribution otherContribution = (Contribution) obj;
        return Integer.compare(id, otherContribution.getId()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, finalValue, finalValueScreenshotUrl, initialValue, initialValueScreenshotUrl, staffAdjustment, unrankedStartingValue);
    }
}
