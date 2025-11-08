package org.uimshowdown.bingo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("CONTRIBUTION_INCREMENT")
public class ContributionIncrementSubmission extends Submission {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribution_method_id")
    @JsonProperty
    private ContributionMethod contributionMethod;

    @Column
    @JsonProperty
    private int amount;

    public ContributionMethod getContributionMethod() {
        return contributionMethod;
    }

    public int getAmount() {
        return amount;
    }

    public void setContributionMethod(ContributionMethod contributionMethod) {
        this.contributionMethod = contributionMethod;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
}
