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
@Table(name = "challenge_submissions")
public class ChallengeSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_relay_component_id")
    private ChallengeRelayComponent relayComponent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submission;

    @Column
    private Double seconds;

    public Integer getId() {
        return id;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public ChallengeRelayComponent getRelayComponent() {
        return relayComponent;
    }

    public Submission getSubmission() {
        return submission;
    }

    public Double getSeconds() {
        return seconds;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void setRelayComponent(ChallengeRelayComponent challengeRelayComponent) {
        this.relayComponent = challengeRelayComponent;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public void setSeconds(Double seconds) {
        this.seconds = seconds;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof ChallengeSubmission) == false) {
            return false;
        }

        ChallengeSubmission otherChallengeSubmission = (ChallengeSubmission) obj;
        return (
            getId() instanceof Integer ? getId().equals(otherChallengeSubmission.getId()) : getId() == otherChallengeSubmission.getId()
            && getSeconds() instanceof Double ? getSeconds().equals(otherChallengeSubmission.getSeconds()) : getSeconds() == otherChallengeSubmission.getSeconds()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seconds);
    }
}
