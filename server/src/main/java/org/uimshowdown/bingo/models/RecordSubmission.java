package org.uimshowdown.bingo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
@DiscriminatorValue("RECORD")
public class RecordSubmission extends Submission {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handicap_id", nullable = true)
    @JsonProperty
    private RecordHandicap handicap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    @JsonProperty
    private Record record;
    
    @Column(name = "video_url", nullable = true, length = 512)
    @JsonProperty
    private String videoUrl;

    @Column
    @JsonIgnore
    private int rawValue;
    
    @OneToOne(mappedBy = "submission", cascade = CascadeType.ALL)
    private RecordCompletion completion;

    public RecordHandicap getHandicap() {
        return handicap;
    }

    public Record getRecord() {
        return record;
    }
    
    public String getVideoUrl() {
        return videoUrl;
    }

    public int getRawValue() {
        return rawValue;
    }
    
    @JsonProperty
    public int getValue() {
        if(handicap != null) {
            return (int) (rawValue * handicap.getMultiplier());
        } else {
            return rawValue;
        }
    }

    public void setHandicap(RecordHandicap recordHandicap) {
        this.handicap = recordHandicap;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
    
    public void setVideoURL(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setRawValue(int rawValue) {
        this.rawValue = rawValue;
    }
    
    @Override
    public void setType(Submission.Type type) throws IllegalArgumentException {
        if(type != Submission.Type.RECORD) {
            throw new IllegalArgumentException("Record submission type must be set to 'RECORD'");
        }
        
        super.setType(type);
    }

    public RecordCompletion getCompletion() {
        return completion;
    }

    public void setCompletion(RecordCompletion completion) {
        this.completion = completion;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    
}
