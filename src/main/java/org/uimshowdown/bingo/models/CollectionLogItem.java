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
@Table(name = "collection_log_items")
public class CollectionLogItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private CollectionLogGroup group;

    @Column(length = 512)
    private String description;

    @Column(length = 64, unique = true)
    private String name;

    @OneToMany(mappedBy = "item")
    private Set<CollectionLogSubmission> submissions;

    public Integer getId() {
        return id;
    }

    public CollectionLogGroup getGroup() {
        return group;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Set<CollectionLogSubmission> getSubmissions() {
        return submissions;
    }

    public void setGroup(CollectionLogGroup collectionLogGroup) {
        group = collectionLogGroup;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof CollectionLogItem) == false) {
            return false;
        }

        CollectionLogItem otherCollectionLogItem = (CollectionLogItem) obj;
        return (
            getId() instanceof Integer ? getId().equals(otherCollectionLogItem.getId()) : getId() == otherCollectionLogItem.getId()
            && getDescription() instanceof String ? getDescription().equals(otherCollectionLogItem.getDescription()) : getDescription() == otherCollectionLogItem.getDescription()
            && getName() instanceof String ? getName().equals(otherCollectionLogItem.getName()) : getName() == otherCollectionLogItem.getName()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, name);
    }
}
