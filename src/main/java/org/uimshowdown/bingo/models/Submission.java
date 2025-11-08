package org.uimshowdown.bingo.models;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Table(name = "submissions")
public class Submission {
    
    public enum State { OPEN, APPROVED, DENIED }
    
    public enum Type { RECORD, CHALLENGE, CONTRIBUTION, CONTRIBUTION_INCREMENT, COLLECTION_LOG, UNRANKED_STARTING_VALUE }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    @JsonProperty
    private Player player;
    
    @Column(name = "submitted_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty
    private Timestamp submittedAt;

    @Column(name = "reviewed_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty
    private Timestamp reviewedAt;

    @Column(name = "reviewer", nullable = true, length = 64)
    @JsonProperty
    private String reviewer;
    
    @Column(name = "screenshot_urls_csv", length = 2048)
    @JsonIgnore
    private String screenshotUrlsCSV = "";

    @Column
    @Enumerated(EnumType.STRING)
    @JsonProperty
    private State state;
    
    /** insert and update are managed by discriminator mechanics */
    @Column(insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    @JsonProperty
    private Type type;
    
    @Column(name = "description", length = 256)
    @JsonProperty
    private String description;

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

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    @JsonProperty("screenshotUrls")
    public List<String> getScreenshotUrls() {
        return Arrays.asList(screenshotUrlsCSV.split(","));
    }
    
    public void setScreenshotUrls(List<String> screenshotUrlsCSV) {
        this.screenshotUrlsCSV = String.join(",", screenshotUrlsCSV);
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Submission && ((Submission) obj).getId() == this.id;
    }

}
