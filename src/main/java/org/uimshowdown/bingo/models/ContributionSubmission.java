package org.uimshowdown.bingo.models;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(name = "submission_id")
@Table(name = "contribution_submissions")
public class ContributionSubmission extends Submission {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribution_method_id")
    private ContributionMethod contributionMethod;

    @Column
    private Integer value;

    public ContributionMethod getContributionMethod() {
        return contributionMethod;
    }

    public Integer getValue() {
        return value;
    }

    public void setContributionMethod(ContributionMethod contributionMethod) {
        this.contributionMethod = contributionMethod;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof ContributionSubmission) == false) {
            return false;
        }
        
        ContributionSubmission otherContributionSubmission = (ContributionSubmission) obj;
        return (
            super.equals(otherContributionSubmission)
            && Integer.compare(value, otherContributionSubmission.getValue()) == 0
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }
}
