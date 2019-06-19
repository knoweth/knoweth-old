package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Card;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps track of when cards need to be reviewed.
 *
 * @author Kevin Liu
 */
public class ReviewTracker {
    // TODO optimize for < O(n) search for current reviews
    private final Map<Card, Metadata> metadata = new HashMap<>();
    private final ReviewAlgorithm reviewAlgorithm;

    /**
     * Creates a new ReviewTracker based on a given document, with all cards unreviewed
     */
    public ReviewTracker(ReviewAlgorithm reviewAlgorithm) {
        this.reviewAlgorithm = reviewAlgorithm;
    }

    public void markReviewed(Card card, Duration duration, ReviewQuality quality) {
        Duration reviewInterval = reviewAlgorithm.getNextReview(card, quality, 0); // TODO take into account overdue days

        // Update metadata
        Session reviewSession = new Session(ZonedDateTime.now(), duration, quality);
        metadata.putIfAbsent(card, new Metadata());
        metadata.get(card).reviewDate = ZonedDateTime.now().plus(reviewInterval);
        metadata.get(card).pastReviews.add(reviewSession);
    }

    public List<Card> getCurrentReviews() {
        List<Card> cards = new ArrayList<>();
        for (Map.Entry<Card, Metadata> x : metadata.entrySet()) {
            if (x.getValue().reviewDate.toLocalDate().isEqual(LocalDate.now())) {
                cards.add(x.getKey());
            }
        }
        return cards;
    }

    public ZonedDateTime getNextReviewDate(Card card) {
        if (!metadata.containsKey(card)) {
            throw new IllegalArgumentException("Card " + card + " has not yet been tracked.");
        }
        return metadata.get(card).reviewDate;
    }

    public List<Session> getPastReviews(Card card) {
        if (!metadata.containsKey(card)) {
            return new ArrayList<>();
        }

        // Defensive copy
        return new ArrayList<>(metadata.get(card).pastReviews);
    }

    private class Metadata {
        private final List<Session> pastReviews = new ArrayList<>();
        private ZonedDateTime reviewDate;
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

        public ZonedDateTime getReviewStartDateTime() {
            return reviewStartDateTime;
        }

        public Duration getDuration() {
            return duration;
        }

        public ReviewQuality getQuality() {
            return quality;
        }
    }
}
