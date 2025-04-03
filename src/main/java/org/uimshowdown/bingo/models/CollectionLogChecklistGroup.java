package org.uimshowdown.bingo.models;

import java.util.List;
import java.util.stream.IntStream;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

@Entity
@DiscriminatorValue("CHECKLIST")
public class CollectionLogChecklistGroup extends CollectionLogGroup {
	
    @OneToMany(mappedBy = "collectionLogGroup")
    @OrderBy("value ASC")
    private List<CollectionLogGroupChecklistBonusPointThreshold> bonusPointThresholds;

    public int[] getBonusPointThresholds() {
        return bonusPointThresholds
            .stream()
            .mapToInt(CollectionLogGroupChecklistBonusPointThreshold::getValue)
            .toArray();
    }

    public void setBonusPointThresholds(int[] bonusPointThresholds) {
        this.bonusPointThresholds =
            IntStream.of(bonusPointThresholds)
            .mapToObj(bonusPointThreshold -> new CollectionLogGroupChecklistBonusPointThreshold(this, bonusPointThreshold))
            .toList();
    }

    @Override
    public void setType(CollectionLogGroup.Type type) throws IllegalArgumentException {
        if (type != CollectionLogGroup.Type.CHECKLIST) {
            throw new IllegalArgumentException("Collection log group type must be set to 'CHECKLIST'!");
        }
        
        super.setType(type);
    }
    
}
