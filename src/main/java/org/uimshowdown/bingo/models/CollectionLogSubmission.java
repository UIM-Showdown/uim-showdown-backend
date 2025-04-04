package org.uimshowdown.bingo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(name = "submission_id")
@Table(name = "collection_log_submissions")
public class CollectionLogSubmission extends Submission {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private CollectionLogItem item;

    public CollectionLogItem getItem() {
        return item;
    }

    public void setItem(CollectionLogItem collectionLogItem) {
        item = collectionLogItem;
    }
    
}
