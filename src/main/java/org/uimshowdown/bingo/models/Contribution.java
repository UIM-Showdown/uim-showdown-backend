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
    private int staffAdjustment = 0;
    
    @Column(name = "purchase_amount")
    private int purchaseAmount = 0;

    @Column(name = "unranked_starting_value")
    private int unrankedStartingValue = -1;
    
    @Column(name = "is_empty")
    private boolean isEmpty = true;

    /**
     * Classes that define constructors must also include a no-arg constructor.
     * @see https://openjpa.apache.org/builds/1.0.2/apache-openjpa-1.0.2/docs/manual/jpa_overview_pc.html#jpa_overview_pc_no_arg
     */
    public Contribution() {
        
    }

    public Contribution(Player player, ContributionMethod contributionMethod, int initialValue, int finalValue, boolean isEmpty) {
        this.player = player;
        this.contributionMethod = contributionMethod;
        this.initialValue = initialValue;
        this.finalValue = finalValue;
        this.isEmpty = isEmpty;
    }

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

    public int getPurchaseAmount() {
        return purchaseAmount;
    }

    public int getUnrankedStartingValue() {
        return unrankedStartingValue;
    }
    
    public boolean isEmpty() {
        return isEmpty;
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
    
    public void setPurchaseAmount(int purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public void setUnrankedStartingValue(int unrankedStartingValue) {
        this.unrankedStartingValue = unrankedStartingValue;
    }
    
    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
    
    /**
     * Returns the final number of units contributed, which is the difference between the final 
     * and start values (with the start value overridden with the unranked starting value if needed), 
     * plus the staff adjustment.
     * @return
     */
    public int getUnitsContributed() {
        if(isEmpty || !isRanked()) {
            return 0;
        }
        if(unrankedStartingValue == -1) {
            return finalValue - initialValue + staffAdjustment + purchaseAmount;
        } else {
            return finalValue - unrankedStartingValue + staffAdjustment + purchaseAmount;
        }
    }
    
    /**
     * Returns true if the contribution is enough to be ranked on the OSRS hiscores, or if this is not a hiscores-tracked boss
     * @return
     */
    public boolean isRanked() {
        if(contributionMethod.getContributionMethodType() != ContributionMethod.Type.TEMPLE_KC) {
            return true;
        }
        return finalValue >= contributionMethod.getRankingThreshold();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Contribution && ((Contribution) obj).getId() == this.id;
    }
    
}
