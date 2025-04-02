package org.uimshowdown.bingo.models;

import java.util.Objects;
import java.util.Set;

import org.uimshowdown.bingo.enums.CollectionLogGroupType;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Base class for the `collection_log_groups` table.
 * 
 * @implNote Do not attempt to save an instance of this base class! Since no default `@DiscriminatorValue` annotation is defined, it will trigger a runtime exception!
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Table(name = "collection_log_groups")
public class CollectionLogGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 512)
    private String description;

    @OneToMany(mappedBy = "group")
    private Set<CollectionLogItem> items;

    @Column(length = 64, unique = true)
    private String name;

    /** insert and update are managed by discriminator mechanics */
    @Column(insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private CollectionLogGroupType type;

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Set<CollectionLogItem> getItems() {
        return items;
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

    public void setType(CollectionLogGroupType type) throws IllegalArgumentException {
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
        return Integer.compare(id, otherCollectionLogGroup.getId()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, name, type);
    }
}
