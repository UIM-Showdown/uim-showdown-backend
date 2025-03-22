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

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getSkill() {
        return skill;
    }

    public Record setDescription(String description) {
        this.description = description;
        return this;
    }

    public Record setSkill(String skill) {
        this.skill = skill;
        return this;
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
        return (
            getId() == otherRecord.getId()
            && getDescription() == otherRecord.getDescription()
            && getSkill() == otherRecord.getSkill()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, skill);
    }
}
