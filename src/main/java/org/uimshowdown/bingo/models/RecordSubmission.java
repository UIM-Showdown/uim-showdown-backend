package org.uimshowdown.bingo.models;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("SUBMISSION")
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
    private int value;

    public RecordHandicap getHandicap() {
        return handicap;
    }

    public Record getRecord() {
        return record;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }

    public int getValue() {
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

    public void setValue(int value) {
        this.value = value;
    }
    
    @Override
    public void setType(Submission.Type type) throws IllegalArgumentException {
        if(type != Submission.Type.RECORD) {
            throw new IllegalArgumentException("Record submission type must be set to 'RECORD'");
        }
        
        super.setType(type);
    }
    
}
