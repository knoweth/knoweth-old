package com.github.knoweth.common.reviewing;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.teavm.flavour.json.JsonPersistable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonPersistable
public class Metadata {
    private List<Session> pastReviews = new ArrayList<>();
    private Date reviewDate;

    @JsonCreator
    Metadata(@JsonProperty("reviewDate") Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    // Getters and setters are for TeaVM JSON serialization only.

    public List<Session> getPastReviews() {
        return pastReviews;
    }

    public void setPastReviews(List<Session> pastReviews) {
        this.pastReviews = pastReviews;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
}
