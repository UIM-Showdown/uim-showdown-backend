package org.uimshowdown.bingo.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("COLLECTION_LOG")
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
    
    @Override
    public void setType(Submission.Type type) throws IllegalArgumentException {
    	if(type != Submission.Type.COLLECTION_LOG) {
    		throw new IllegalArgumentException("Collection log submission type must be set to 'COLLECTION_LOG'");
    	}
    	
    	super.setType(type);
    }
    
}
