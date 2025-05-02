package org.uimshowdown.bingo.models;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "record_completions")
public class RecordCompletion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_handicap_id", nullable = true)
    public RecordHandicap handicap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "completed_at")
    @Temporal(TemporalType.TIMESTAMP)
    public Timestamp completedAt;

    @Column(name = "raw_value")
    public int rawValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    public Record record;

    @Column(name = "video_url", nullable = true, length = 512)
    public String videoUrl;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private RecordSubmission submission;

    public int getId() {
        return id;
    }

    public Timestamp getCompletedAt() {
        return completedAt;
    }

    public RecordHandicap getHandicap() {
        return handicap;
    }

    public Player getPlayer() {
        return player;
    }

    public int getRawValue() {
        return rawValue;
    }

    public Record getRecord() {
        return record;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
    
    public int getValue() {
        if(handicap != null) {
            return (int) (rawValue * handicap.getMultiplier());
        } else {
            return rawValue;
        }
    }

    public void setCompletedAt(Timestamp completedAt) {
        this.completedAt = completedAt;
    }

    public void setHandicap(RecordHandicap recordHandicap) {
        handicap = recordHandicap;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setRawValue(int rawValue) {
        this.rawValue = rawValue;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public RecordSubmission getSubmission() {
        return submission;
    }

    public void setSubmission(RecordSubmission submission) {
        this.submission = submission;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @PreRemove
    private void preRemove() {
        if(submission != null) {            
            submission.setCompletion(null);
            submission = null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof RecordCompletion && ((RecordCompletion) obj).getId() == this.id;
    }
    
}
