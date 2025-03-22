package org.uimshowdown.bingo.models;

import java.util.Objects;

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
@Table(name = "submission_screenshot_urls")
public class SubmissionScreenshotUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "screenshot_url", length = 512)
    private String screenshotUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submission;

    public Integer getId() {
        return id;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public Submission getSubmission() {
        return submission;
    }

    public SubmissionScreenshotUrl setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
        return this;
    }

    public SubmissionScreenshotUrl setSubmission(Submission submission) {
        this.submission = submission;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof SubmissionScreenshotUrl) == false) {
            return false;
        }

        SubmissionScreenshotUrl otherSubmissionScreenshotUrl = (SubmissionScreenshotUrl) obj;
        return (
            getId() == otherSubmissionScreenshotUrl.getId()
            && getScreenshotUrl() == otherSubmissionScreenshotUrl.getScreenshotUrl()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, screenshotUrl);
    }
}
