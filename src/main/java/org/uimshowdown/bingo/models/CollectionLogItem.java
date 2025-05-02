package org.uimshowdown.bingo.models;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "collection_log_items")
public class CollectionLogItem {
    
    public enum Type { NORMAL, PET, JAR }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private int id;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CollectionLogCompletion> completions = new HashSet<CollectionLogCompletion>();
    
    @Column
    @Enumerated(EnumType.STRING)
    @JsonProperty
    private Type type = Type.NORMAL;
    
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ItemOption> itemOptions = new HashSet<ItemOption>();
    
    @Column(nullable = true)
    @JsonProperty
    private Integer points = 0;

    @Column(length = 512)
    @JsonProperty
    private String description;

    @Column(length = 64, unique = true)
    @JsonProperty
    private String name;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CollectionLogSubmission> submissions = new HashSet<CollectionLogSubmission>();

    public int getId() {
        return id;
    }

    public Set<CollectionLogCompletion> getCompletions() {
        return completions;
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
    
    public void setItemOptions(Set<ItemOption> itemOptions) {
        this.itemOptions = itemOptions;
    }

    @JsonProperty("itemOptions")
    public Set<String> getItemOptionNames() {
        Set<String> names = new HashSet<String>();
        if(itemOptions == null) {
            return names;
        }
        for(ItemOption option : itemOptions) {
            names.add(option.getName());
        }
        return names;
    }
    

    public Set<CollectionLogSubmission> getSubmissions() {
        return submissions;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCompletions(Set<CollectionLogCompletion> completions) {
        this.completions = completions;
    }

    public void setSubmissions(Set<CollectionLogSubmission> submissions) {
        this.submissions = submissions;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof CollectionLogItem && ((CollectionLogItem) obj).getId() == this.id;
    }
    
}
