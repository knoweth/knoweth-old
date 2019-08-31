package com.github.knoweth.common.reviewing;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.teavm.flavour.json.JsonPersistable;

import java.util.Date;

@JsonPersistable
public class Session {
    private Date reviewStartDateTime;
    private long durationMillis;
    private ReviewQuality quality;

    @JsonCreator
    public Session(
            @JsonProperty("reviewStartTime") Date reviewStartDateTime,
            @JsonProperty("durationMillis") long duration,
            @JsonProperty("quality") ReviewQuality quality) {
        this.reviewStartDateTime = reviewStartDateTime;
        this.durationMillis = duration;
        this.quality = quality;
    }

    // Getters and setters are for TeaVM JSON serialization only.

    public Date getReviewStartDateTime() {
        return reviewStartDateTime;
    }

    public void setReviewStartDateTime(Date reviewStartDateTime) {
        this.reviewStartDateTime = reviewStartDateTime;
    }

    public long getDuration() {
        return durationMillis;
    }

    public void setDuration(long duration) {
        this.durationMillis = duration;
    }

    public ReviewQuality getQuality() {
        return quality;
    }

    public void setQuality(ReviewQuality quality) {
        this.quality = quality;
    }
}
