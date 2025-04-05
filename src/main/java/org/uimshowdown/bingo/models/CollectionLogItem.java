package org.uimshowdown.bingo.models;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
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
    @JsonProperty
    private int id;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CollectionLogCompletion> completions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @JsonIgnore
    private CollectionLogGroup group;
    
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ItemOption> itemOptions;
    
    @Column(nullable = true)
    @JsonProperty
    private Integer points;

    @Column(length = 512)
    @JsonProperty
    private String description;

    @Column(length = 64, unique = true)
    @JsonProperty
    private String name;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CollectionLogSubmission> submissions;

    public int getId() {
        return id;
    }

    public Set<CollectionLogCompletion> getCompletions() {
        return completions;
    }

    public CollectionLogGroup getGroup() {
        return group;
    }
    
    public Integer getPoints() {
    	return points;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
    
    public Set<ItemOption> getItemOptions() {
    	return itemOptions;
    }
    
    @JsonProperty("itemOptions")
    public Set<String> getItemOptionNames() {
    	Set<String> names = new HashSet<String>();
    	for(ItemOption option : itemOptions) {
    		names.add(option.getName());
    	}
    	return names;
    }
    

    public Set<CollectionLogSubmission> getSubmissions() {
        return submissions;
    }

    public void setGroup(CollectionLogGroup collectionLogGroup) {
        group = collectionLogGroup;
    }
    
    public void setPoints(Integer points) {
    	this.points = points;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof CollectionLogItem && ((CollectionLogItem) obj).getId() == this.id;
    }
    
}
