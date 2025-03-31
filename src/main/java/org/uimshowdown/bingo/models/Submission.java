package org.uimshowdown.bingo.models;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

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
import jakarta.persistence.OneToMany;
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

    @Column(name = "decision_made_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp decisionMadeAt;

    @Column(name = "decision_maker", nullable = true, length = 64)
    private String decisionMaker;

    @OneToMany(mappedBy = "submission")
    private Set<CollectionLogSubmission> collectionLogSubmissions;

    @OneToMany(mappedBy = "submission")
    private Set<ChallengeSubmission> challengeSubmissions;

    @OneToMany(mappedBy = "submission")
    private Set<RecordSubmission> recordSubmissions;

    @OneToMany(mappedBy = "submission")
    private Set<SubmissionScreenshotUrl> screenshotUrls;

    @Column
    @Enumerated(EnumType.STRING)
    private SubmissionState state;

    public Integer getId() {
        return id;
    }

    public Timestamp getDecisionMadeAt() {
        return decisionMadeAt;
    }

    public String getDecisionMaker() {
        return decisionMaker;
    }

    public Player getPlayer() {
        return player;
    }

    public Set<CollectionLogSubmission> getCollectionLogSubmissions() {
        return collectionLogSubmissions;
    }

    public Set<ChallengeSubmission> getChallengeSubmissions() {
        return challengeSubmissions;
    }

    public Set<RecordSubmission> getRecordSubmissions() {
        return recordSubmissions;
    }

    public Set<SubmissionScreenshotUrl> getScreenshotUrls() {
        return screenshotUrls;
    }

    public SubmissionState getSubmissionState() {
        return state;
    }

    public void setDecisionMadeAt(Timestamp decisionMadeAt) {
        this.decisionMadeAt = decisionMadeAt;
    }

    public void setDecisionMaker(String decisionMaker) {
        this.decisionMaker = decisionMaker;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setSubmissionState(SubmissionState submissionState) {
        state = submissionState;
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
            getId() instanceof Integer ? getId().equals(otherSubmission.getId()) : getId() == otherSubmission.getId()
            && getDecisionMadeAt() instanceof Timestamp ? getDecisionMadeAt().equals(otherSubmission.getDecisionMadeAt()) : getDecisionMadeAt() == otherSubmission.getDecisionMadeAt()
            && getDecisionMaker() instanceof String ? getDecisionMaker().equals(otherSubmission.getDecisionMaker()) : getDecisionMaker() == otherSubmission.getDecisionMaker()
            && getSubmissionState() instanceof SubmissionState ? getSubmissionState().equals(otherSubmission.getSubmissionState()) : getSubmissionState() == otherSubmission.getSubmissionState()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, decisionMadeAt, decisionMaker, state);
    }
}
