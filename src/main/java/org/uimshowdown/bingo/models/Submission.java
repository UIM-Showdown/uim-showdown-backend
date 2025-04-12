package org.uimshowdown.bingo.models;

import java.sql.Timestamp;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Table(name = "submissions")
public class Submission {
    
    public enum State { OPEN, APPROVED, DENIED }
    
    public enum Type { RECORD, CHALLENGE, CONTRIBUTION, COLLECTION_LOG, UNRANKED_STARTING_VALUE }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "reviewed_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp reviewedAt;

    @Column(name = "reviewer", nullable = true, length = 64)
    private String reviewer;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL)
    private Set<SubmissionScreenshotUrl> screenshotUrls;

    @Column
    @Enumerated(EnumType.STRING)
    private State state;
    
    /** insert and update are managed by discriminator mechanics */
    @Column(insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    @JsonProperty
    private Type type;

    public int getId() {
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
    
    public void setScreenshotUrls(Set<SubmissionScreenshotUrl> screenshotUrls) {
    	this.screenshotUrls = screenshotUrls;
    }

    public State getSubmissionState() {
        return state;
    }
    
    public Type getType() {
    	return type;
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

    public void setSubmissionState(State submissionState) {
        state = submissionState;
    }
    
    public void setType(Type type) throws IllegalArgumentException {
    	this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Submission && ((Submission) obj).getId() == this.id;
    }

}
