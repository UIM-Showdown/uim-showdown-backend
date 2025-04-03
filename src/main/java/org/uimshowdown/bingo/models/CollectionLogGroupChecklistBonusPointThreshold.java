package org.uimshowdown.bingo.models;

import java.util.Objects;

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
@Table(name = "collection_log_group_checklist_bonus_point_thresholds")
public class CollectionLogGroupChecklistBonusPointThreshold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_log_group_id")
    private CollectionLogChecklistGroup collectionLogGroup;

    @Column
    private Integer value;

    /**
     * Classes that define constructors must also include a no-arg constructor.
     * @see https://openjpa.apache.org/builds/1.0.2/apache-openjpa-1.0.2/docs/manual/jpa_overview_pc.html#jpa_overview_pc_no_arg
     */
    public CollectionLogGroupChecklistBonusPointThreshold() {
        
    }

    public CollectionLogGroupChecklistBonusPointThreshold(CollectionLogChecklistGroup collectionLogChecklistGroup, Integer value) {
        collectionLogGroup = collectionLogChecklistGroup;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public CollectionLogChecklistGroup getCollectionLogGroup() {
        return collectionLogGroup;
    }

    public Integer getValue() {
        return value;
    }

    public void setCollectionLogGroup(CollectionLogChecklistGroup collectionLogChecklistGroup) {
        collectionLogGroup = collectionLogChecklistGroup;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof CollectionLogGroupChecklistBonusPointThreshold) == false) {
            return false;
        }

        CollectionLogGroupChecklistBonusPointThreshold otherCollectionLogGroupChecklistBonusPointThreshold = (CollectionLogGroupChecklistBonusPointThreshold) obj;
        return Integer.compare(id, otherCollectionLogGroupChecklistBonusPointThreshold.getId()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }
}
