package org.uimshowdown.bingo.models;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "record_handicaps")
public class RecordHandicap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "handicap")
    private Set<RecordCompletion> completions;

    @Column
    private Double multiplier;

    @Column(length = 64)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    @OneToMany(mappedBy = "handicap")
    private Set<RecordSubmission> submissions;

    public Integer getId() {
        return id;
    }

    public Set<RecordCompletion> getCompletions() {
        return completions;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public String getName() {
        return name;
    }

    public Record getRecord() {
        return record;
    }

    public Set<RecordSubmission> getSubmissions() {
        return submissions;
    }

    public RecordHandicap setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
        return this;
    }

    public RecordHandicap setName(String name) {
        this.name = name;
        return this;
    }

    public RecordHandicap setRecord(Record record) {
        this.record = record;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof RecordHandicap) == false) {
            return false;
        }

        RecordHandicap otherRecordHandicap = (RecordHandicap) obj;
        return (
            getId() == otherRecordHandicap.getId()
            && Double.compare(getMultiplier(), otherRecordHandicap.getMultiplier()) == 0
            && getName() == otherRecordHandicap.getName()  
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, multiplier, name);
    }
}
