package org.uimshowdown.bingo.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @OneToMany(mappedBy = "record")
    private Set<RecordCompletion> completions;

    @Column(length = 512)
    @JsonProperty
    private String description;

    @OneToMany(mappedBy = "record")
    @JsonProperty
    private Set<RecordHandicap> handicaps;

    @Column(length = 16)
    @JsonProperty
    private String skill;

    @OneToMany(mappedBy = "record")
    private Set<RecordSubmission> submissions;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getSkill() {
        return skill;
    }

    public Set<RecordSubmission> getSubmissions() {
        return submissions;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Record && ((Record) obj).getId() == this.id;
    }
    
}
