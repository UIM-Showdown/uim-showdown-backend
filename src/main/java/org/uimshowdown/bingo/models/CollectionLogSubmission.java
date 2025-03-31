package org.uimshowdown.bingo.models;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "collection_log_submissions")
public class CollectionLogSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private CollectionLogItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submission;

    public Integer getId() {
        return id;
    }

    public CollectionLogItem getItem() {
        return item;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setItem(CollectionLogItem collectionLogItem) {
        item = collectionLogItem;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof CollectionLogSubmission) == false) {
            return false;
        }

        CollectionLogSubmission otherCollectionLogSubmission = (CollectionLogSubmission) obj;
        return (
            getId() instanceof Integer ? getId().equals(otherCollectionLogSubmission.getId()) : getId() == otherCollectionLogSubmission.getId()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
