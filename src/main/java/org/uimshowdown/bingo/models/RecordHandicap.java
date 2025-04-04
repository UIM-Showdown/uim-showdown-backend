package org.uimshowdown.bingo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonProperty
    private int id;

    @Column
    @JsonProperty
    private double multiplier;

    @Column(length = 64)
    @JsonProperty
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    @JsonIgnore
    private Record record;

    public int getId() {
        return id;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public String getName() {
        return name;
    }

    public Record getRecord() {
        return record;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof RecordHandicap && ((RecordHandicap) obj).getId() == this.id;
    }
    
}
