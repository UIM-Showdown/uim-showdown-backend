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
import jakarta.persistence.Table;

@Entity
@Table(name = "record_submissions")
public class RecordSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handicap_id", nullable = true)
    private RecordHandicap handicap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submission;

    @Column(name = "submitted_at", nullable = true)
    private Timestamp submittedAt;

    @Column
    private Integer value;

    public Integer getId() {
        return id;
    }

    public RecordHandicap getHandicap() {
        return handicap;
    }

    public Record getRecord() {
        return record;
    }

    public Submission getSubmission() {
        return submission;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }

    public Integer getValue() {
        return value;
    }

    public RecordSubmission setHandicap(RecordHandicap recordHandicap) {
        this.handicap = recordHandicap;
        return this;
    }

    public RecordSubmission setRecord(Record record) {
        this.record = record;
        return this;
    }

    public RecordSubmission setSubmission(Submission submission) {
        this.submission = submission;
        return this;
    }

    public RecordSubmission setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
        return this;
    }

    public RecordSubmission setValue(Integer value) {
        this.value = value;
        return this;
    }
}
