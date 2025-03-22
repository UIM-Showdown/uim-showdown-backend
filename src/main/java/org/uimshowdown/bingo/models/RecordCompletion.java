package org.uimshowdown.bingo.models;

import java.sql.Timestamp;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "record_completions")
public class RecordCompletion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

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
    public Integer rawValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    public Record record;

    @Column(name = "video_url", nullable = true, length = 512)
    public String videoUrl;

    public Integer getId() {
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

    public Integer getRawValue() {
        return rawValue;
    }

    public Record getRecord() {
        return record;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public RecordCompletion setCompletedAt(Timestamp completedAt) {
        this.completedAt = completedAt;
        return this;
    }

    public RecordCompletion setHandicap(RecordHandicap recordHandicap) {
        handicap = recordHandicap;
        return this;
    }

    public RecordCompletion setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public RecordCompletion setRawValue(Integer rawValue) {
        this.rawValue = rawValue;
        return this;
    }

    public RecordCompletion setRecord(Record record) {
        this.record = record;
        return this;
    }

    public RecordCompletion setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof RecordCompletion) == false) {
            return false;
        }

        RecordCompletion otherRecordCompletion = (RecordCompletion) obj;
        return (
            getCompletedAt() == otherRecordCompletion.getCompletedAt()
            && getId() == otherRecordCompletion.getId()
            && getRawValue() == otherRecordCompletion.getRawValue()
            && getVideoUrl() == otherRecordCompletion.getVideoUrl()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, completedAt, rawValue, videoUrl);
    }
}
