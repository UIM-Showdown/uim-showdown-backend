package org.uimshowdown.bingo.models;

import java.sql.Timestamp;
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
@Table(name = "record_submissions")
public class RecordSubmission extends Submission {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handicap_id", nullable = true)
    private RecordHandicap handicap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    @Column(name = "submitted_at", nullable = true)
    private Timestamp submittedAt;

    @Column
    private Integer value;

    public RecordHandicap getHandicap() {
        return handicap;
    }

    public Record getRecord() {
        return record;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }

    public Integer getValue() {
        return value;
    }

    public void setHandicap(RecordHandicap recordHandicap) {
        this.handicap = recordHandicap;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), submittedAt, value);
    }
}
