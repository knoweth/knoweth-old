package com.github.knoweth.common.reviewing;

import com.github.knoweth.client.util.DateUtil;
import com.github.knoweth.common.data.Card;
import com.github.knoweth.common.data.Document;
import com.github.knoweth.common.data.Note;
import com.github.knoweth.common.data.Section;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;

import java.util.*;

/**
 * Keeps track, for a given user, of when cards should be reviewed.
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

    /**
     * Import all cards from a document.
     * <p>
     * Cards already being tracked by this Tracker will not have their metadata overwritten.
     */
    public void importCards(Document doc) {
        Date reviewDate = new Date();
        for (Section section : doc.getSections()) {
            for (Note note : section.getNotes()) {
                for (Card card : note.getCards()) {
                    metadata.putIfAbsent(card, new Metadata(reviewDate));
                }
            }
        }
    }

    /**
     * Track a review of a given Card.
     *
     * @param card the card reviewed
     * @param duration the duration of the review
     * @param quality what the user reported for their card recall
     */
    public void markReviewed(Card card, Duration duration, ReviewQuality quality) {
        // Get the next review interval
        Duration reviewInterval = reviewAlgorithm.getNextReview(card, quality, 0); // TODO take into account overdue days

        // Update metadata
        Session reviewSession = new Session(new Date(), duration, quality);
        metadata.putIfAbsent(card, new Metadata(DateUtil.addDuration(new Date(), reviewInterval)));
        metadata.get(card).reviewDate = DateUtil.addDuration(new Date(), reviewInterval);
        metadata.get(card).pastReviews.add(reviewSession);
    }

    public Card getNextReview() {
        for (Map.Entry<Card, Metadata> entry : metadata.entrySet()) {
            Date reviewDate = entry.getValue().reviewDate;
            if (DateUtil.sameDay(reviewDate, new Date()) || reviewDate.getTime() < new Date().getTime()) {
                return entry.getKey();
            }
        }
        return null; // no more entries to review
    }

    public int getRemainingReviews() {
        int count = 0;
        for (Map.Entry<Card, Metadata> entry : metadata.entrySet()) {
            Date reviewDate = entry.getValue().reviewDate;
            if (DateUtil.sameDay(reviewDate, new Date()) || reviewDate.getTime() < new Date().getTime()) {
                count++;
            }
        }
        return count;
    }

    public Date getNextReviewDate(Card card) {
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
        private Date reviewDate;

        Metadata(Date reviewDate) {
            this.reviewDate = reviewDate;
        }
    }

    public class Session {
        private final Date reviewStartDateTime;
        private final Duration duration;
        private final ReviewQuality quality;

        private Session(Date reviewStartDateTime, Duration duration, ReviewQuality quality) {
            this.reviewStartDateTime = reviewStartDateTime;
            this.duration = duration;
            this.quality = quality;
        }

        public Date getReviewStartDateTime() {
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
