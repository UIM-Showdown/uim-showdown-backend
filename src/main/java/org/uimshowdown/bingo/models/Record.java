package org.uimshowdown.bingo.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "records")
public class Record {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private int id;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<RecordCompletion> completions = new HashSet<RecordCompletion>();

    @Column(length = 512)
    @JsonProperty
    private String description;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
    @JsonProperty
    private List<RecordHandicap> handicaps = new ArrayList<RecordHandicap>();

    @Column
    @JsonProperty
    private String name;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<RecordSubmission> submissions = new HashSet<RecordSubmission>();

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Set<RecordSubmission> getSubmissions() {
        return submissions;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<RecordHandicap> getHandicaps() {
        return handicaps;
    }

    public void setHandicaps(List<RecordHandicap> handicaps) {
        this.handicaps = handicaps;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Record && ((Record) obj).getId() == this.id;
    }
    
}
