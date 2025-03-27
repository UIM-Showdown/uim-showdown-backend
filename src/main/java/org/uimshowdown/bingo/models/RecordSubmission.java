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

    public void setHandicap(RecordHandicap recordHandicap) {
        this.handicap = recordHandicap;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof RecordSubmission) == false) {
            return false;
        }

        RecordSubmission otherRecordSubmission = (RecordSubmission) obj;
        return (
            getId() instanceof Integer ? getId().equals(otherRecordSubmission.getId()) : getId() == otherRecordSubmission.getId()
            && getSubmittedAt() instanceof Timestamp ? getSubmittedAt().equals(otherRecordSubmission.getSubmittedAt()) : getSubmittedAt() == otherRecordSubmission.getSubmittedAt()
            && getValue() instanceof Integer ? getValue().equals(otherRecordSubmission.getValue()) : getValue() == otherRecordSubmission.getValue()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, submittedAt, value);
    }
}
