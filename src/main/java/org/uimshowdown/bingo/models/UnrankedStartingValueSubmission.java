package org.uimshowdown.bingo.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("UNRANKED_STARTING_VALUE")
public class UnrankedStartingValueSubmission extends Submission {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribution_method_id")
    private ContributionMethod contributionMethod;

    @Column
    private int value;

    public ContributionMethod getContributionMethod() {
        return contributionMethod;
    }

    public int getValue() {
        return value;
    }

    public void setContributionMethod(ContributionMethod contributionMethod) {
        this.contributionMethod = contributionMethod;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    @Override
    public void setType(Submission.Type type) throws IllegalArgumentException {
    	if(type != Submission.Type.UNRANKED_STARTING_VALUE) {
    		throw new IllegalArgumentException("Unranked starting value submission type must be set to 'UNRANKED_STARTING_VALUE'");
    	}
    	
    	super.setType(type);
    }
    
}
