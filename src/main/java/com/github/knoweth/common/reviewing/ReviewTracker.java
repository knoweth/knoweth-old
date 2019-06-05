package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Card;
import com.github.knoweth.common.data.Document;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class ReviewTracker {
    private Map<LocalDate, List<Card>> reviewTimes;
    private Map<Card, MetaData> metadata;
    private final ReviewAlgorithm reviewAlgorithm;

    /**
     * Creates a new ReviewTracker based on a given document, with all cards unreviewed
     */
    public ReviewTracker(Document document, ReviewAlgorithm reviewAlgorithm) {
        this.reviewAlgorithm = reviewAlgorithm;
    }

    public void markReviewed(Card card, Duration duration, ReviewQuality quality) {
        Session reviewSession = new Session(ZonedDateTime.now(), duration, quality);
        if (metadata.get(card) == null) {
            metadata.put(card, new MetaData());
        }
    }

    public List<Card> getCurrentReviews() {
        return null;
    }

    public class MetaData {
        private LocalDate reviewDate;
        private List<Session> pastReviews;
    }

    public class Session {
        private final ZonedDateTime reviewStartDateTime;
        private final Duration duration;
        private final ReviewQuality quality;

        private Session(ZonedDateTime reviewStartDateTime, Duration duration, ReviewQuality quality) {
            this.reviewStartDateTime = reviewStartDateTime;
            this.duration = duration;
            this.quality = quality;
        }
    }
}
