package org.uimshowdown.bingo.models;

import java.util.Objects;

import org.uimshowdown.bingo.enums.CollectionLogGroupType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "collection_log_groups")
public class CollectionLogGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 512)
    private String description;

    @Column(length = 64, unique = true)
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private CollectionLogGroupType type;

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public CollectionLogGroupType getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(CollectionLogGroupType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof CollectionLogGroup) == false) {
            return false;
        }

        CollectionLogGroup otherCollectionLogGroup = (CollectionLogGroup) obj;
        return (
            getId() instanceof Integer ? getId().equals(otherCollectionLogGroup.getId()) : getId() == otherCollectionLogGroup.getId()
            && getDescription() instanceof String ? getDescription().equals(otherCollectionLogGroup.getDescription()) : getDescription() == otherCollectionLogGroup.getDescription()
            && getName() instanceof String ? getName().equals(otherCollectionLogGroup.getName()) : getName() == otherCollectionLogGroup.getName()
            && getType() instanceof CollectionLogGroupType ? getType().equals(otherCollectionLogGroup.getType()) : getType() == otherCollectionLogGroup.getType()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, name, type);
    }
}
