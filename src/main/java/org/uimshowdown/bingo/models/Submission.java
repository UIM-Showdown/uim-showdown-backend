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
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) 
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "reviewed_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp reviewedAt;

    @Column(name = "reviewer", nullable = true, length = 64)
    private String reviewer;

    @OneToMany(mappedBy = "submission")
    private Set<SubmissionScreenshotUrl> screenshotUrls;

    @Column
    @Enumerated(EnumType.STRING)
    private SubmissionState state;

    public Integer getId() {
        return id;
    }

    public Timestamp getReviewedAt() {
        return reviewedAt;
    }

    public String getReviewer() {
        return reviewer;
    }

    public Player getPlayer() {
        return player;
    }

    public Set<SubmissionScreenshotUrl> getScreenshotUrls() {
        return screenshotUrls;
    }

    public SubmissionState getSubmissionState() {
        return state;
    }

    public void setReviewedAt(Timestamp reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
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
            && getReviewedAt() instanceof Timestamp ? getReviewedAt().equals(otherSubmission.getReviewedAt()) : getReviewedAt() == otherSubmission.getReviewedAt()
            && getReviewer() instanceof String ? getReviewer().equals(otherSubmission.getReviewer()) : getReviewer() == otherSubmission.getReviewer()
            && getSubmissionState() instanceof SubmissionState ? getSubmissionState().equals(otherSubmission.getSubmissionState()) : getSubmissionState() == otherSubmission.getSubmissionState()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reviewedAt, reviewer, state);
    }
}
