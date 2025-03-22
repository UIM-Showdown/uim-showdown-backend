package org.uimshowdown.bingo.models;

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
@Table(name = "record_handicaps")
public class RecordHandicap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Double multiplier;

    @Column(length = 64)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    public Integer getId() {
        return id;
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
}
