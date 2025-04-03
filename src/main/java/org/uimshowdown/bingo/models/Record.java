package org.uimshowdown.bingo.models;

import java.util.Objects;
import java.util.Set;

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
    private Integer id;

    @OneToMany(mappedBy = "record")
    private Set<RecordCompletion> completions;

    @Column(length = 512)
    private String description;

    @OneToMany(mappedBy = "record")
    private Set<RecordHandicap> handicaps;

    @Column(length = 16)
    private String skill;

    @OneToMany(mappedBy = "record")
    private Set<RecordSubmission> submissions;

    public Integer getId() {
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
        if (obj == this) {
            return true;
        }

        if ((obj instanceof Record) == false) {
            return false;
        }

        Record otherRecord = (Record) obj;
        return Integer.compare(id, otherRecord.getId()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, skill);
    }
}
