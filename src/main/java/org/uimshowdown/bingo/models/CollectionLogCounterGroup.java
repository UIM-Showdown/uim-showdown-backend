package org.uimshowdown.bingo.models;

import java.util.List;
import java.util.stream.IntStream;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

@Entity
@DiscriminatorValue("COUNTER")
public class CollectionLogCounterGroup extends CollectionLogGroup {
    @OneToMany(mappedBy = "collectionLogGroup")
    @OrderBy("value DESC")
    private List<CollectionLogGroupCounterPointValue> counterPointValues;

    public int[] getCounterPointValues() {
        return counterPointValues
            .stream()
            .mapToInt(CollectionLogGroupCounterPointValue::getValue)
            .toArray();
    }

    public void setCounterPointValues(int[] counterPointValues) {
        this.counterPointValues =
            IntStream.of(counterPointValues)
            .mapToObj(counterPointValue -> new CollectionLogGroupCounterPointValue(this, counterPointValue))
            .toList();
    }

    @Override
    public void setType(CollectionLogGroup.Type type) throws IllegalArgumentException {
        if (type != CollectionLogGroup.Type.COUNTER) {
            throw new IllegalArgumentException("Collection log group type must be set to 'COUNTER'!");
        }
        
        super.setType(type);
    }
}
