package org.uimshowdown.bingo.models;

import java.util.Date;
import java.util.Objects;

import org.uimshowdown.bingo.enums.SubmissionState;

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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column
    @Enumerated(EnumType.STRING)
    private SubmissionState state;

    @Column(name = "decision_made_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date decisionTimestamp;

    @Column(name = "decision_maker", nullable = true, length = 64)
    private String decisionMaker;

    public Integer getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public SubmissionState getSubmissionState() {
        return state;
    }

    public Date getDecisionTimestamp() {
        return decisionTimestamp;
    }

    public String getDecisionMaker() {
        return decisionMaker;
    }

    public Submission setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public Submission setSubmissionState(SubmissionState submissionState) {
        state = submissionState;
        return this;
    }

    public Submission setDecisionTimestamp(Date decisionTimestamp) {
        this.decisionTimestamp = decisionTimestamp;
        return this;
    }

    public Submission setDecisionMaker(String decisionMaker) {
        this.decisionMaker = decisionMaker;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof Submission) == false) {
            return false;
        }

        Submission otherSubmission = (Submission) obj;
        return (
            this.getId() == otherSubmission.getId()
            && this.getDecisionMaker() == otherSubmission.getDecisionMaker()
            && this.getDecisionTimestamp() == otherSubmission.getDecisionTimestamp()
            && this.getPlayer() == otherSubmission.getPlayer()
            && this.getSubmissionState() == otherSubmission.getSubmissionState()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, decisionMaker, decisionTimestamp, player, state);
    }
}
