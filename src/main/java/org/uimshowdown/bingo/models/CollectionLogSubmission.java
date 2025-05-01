package org.uimshowdown.bingo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonProperty
    private CollectionLogItem item;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_option_id")
    @JsonProperty
    private ItemOption itemOption;

    public CollectionLogItem getItem() {
        return item;
    }

    public void setItem(CollectionLogItem collectionLogItem) {
        item = collectionLogItem;
    }
    
    public ItemOption itemOption() {
        return itemOption;
    }
    
    public void setItemOption(ItemOption itemOption) {
        this.itemOption = itemOption;
    }
    
    @Override
    public void setType(Submission.Type type) throws IllegalArgumentException {
        if(type != Submission.Type.COLLECTION_LOG) {
            throw new IllegalArgumentException("Collection log submission type must be set to 'COLLECTION_LOG'");
        }
        
        super.setType(type);
    }
    
}
