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
@Table(name = "collection_log_group_counter_point_values")
public class CollectionLogGroupCounterPointValue {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_log_group_id")
    private CollectionLogCounterGroup collectionLogGroup;

    @Column
    private int value;

    /**
     * Classes that define constructors must also include a no-arg constructor.
     * @see https://openjpa.apache.org/builds/1.0.2/apache-openjpa-1.0.2/docs/manual/jpa_overview_pc.html#jpa_overview_pc_no_arg
     */
    public CollectionLogGroupCounterPointValue() {
        
    }

    public CollectionLogGroupCounterPointValue(CollectionLogCounterGroup collectionLogCounterGroup, int value) {
        collectionLogGroup = collectionLogCounterGroup;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public CollectionLogCounterGroup getCollectionLogGroup() {
        return collectionLogGroup;
    }

    public int getValue() {
        return value;
    }

    public void setCollectionLogGroup(CollectionLogCounterGroup collectionLogCounterGroup) {
        collectionLogGroup = collectionLogCounterGroup;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof CollectionLogGroupCounterPointValue && ((CollectionLogGroupCounterPointValue) obj).getId() == this.id;
    }
    
}
