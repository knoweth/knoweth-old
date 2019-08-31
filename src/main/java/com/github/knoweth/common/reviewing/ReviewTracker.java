package com.github.knoweth.common.reviewing;

import com.fasterxml.jackson.annotation.*;
import com.github.knoweth.client.util.DateUtil;
import com.github.knoweth.common.data.Card;
import com.github.knoweth.common.data.Document;
import com.github.knoweth.common.data.Note;
import com.github.knoweth.common.data.Section;

import org.teavm.flavour.json.JsonPersistable;
import org.threeten.bp.Duration;

import java.util.*;

/**
 * Keeps track, for a given user, of when cards should be reviewed.
 *
 * @author Kevin Liu
 */
@JsonPersistable
public class ReviewTracker {
    // TODO optimize for < O(n) search for current reviews
    private Map<String, Metadata> metadata;
    private Map<String, Card> cardIds = new HashMap<>();
    // TODO Use the generic ReviewAlgorithm interface
    // Currently breaks TeaVM deserialization, though
    private AnkiAlgorithm reviewAlgorithm;

    public ReviewTracker() {
    }

    /**
     * Creates a new ReviewTracker, with all cards unreviewed
     *
     * @param reviewAlgorithm the reviewing algorithm to use
     */
    public ReviewTracker(AnkiAlgorithm reviewAlgorithm) {
        this.reviewAlgorithm = reviewAlgorithm;
        this.metadata = new HashMap<>();
    }

    /**
     * Creates a new ReviewTracker with preloaded metadata
     *
     * @param reviewAlgorithm the reviewing algorithm to use
     * @param metadata        the metadata of previously-reviewed cards
     */
    public ReviewTracker(AnkiAlgorithm reviewAlgorithm, Map<String, Metadata> metadata) {
        this.reviewAlgorithm = reviewAlgorithm;
        this.metadata = new HashMap<>(metadata);
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
                    metadata.putIfAbsent(card.getId(), new Metadata(reviewDate));
                    cardIds.put(card.getId(), card);
                }
            }
        }
    }

    /**
     * Track a review of a given Card.
     *
     * @param card     the card reviewed
     * @param duration the duration of the review
     * @param quality  what the user reported for their card recall
     */
    public void markReviewed(Card card, Duration duration, ReviewQuality quality) {
        // Get the next review interval
        Duration reviewInterval = reviewAlgorithm.getNextReview(card, quality, 0); // TODO take into account overdue days

        // Update metadata
        Session reviewSession = new Session(new Date(), duration.toMillis(), quality);
        metadata.putIfAbsent(card.getId(), new Metadata(DateUtil.addDuration(new Date(), reviewInterval)));
        metadata.get(card.getId()).setReviewDate(DateUtil.addDuration(new Date(), reviewInterval));
        metadata.get(card.getId()).getPastReviews().add(reviewSession);
    }

    /**
     * @return the next card in the review queue
     */
    @JsonIgnore
    public Card getNextReview() {
        for (Map.Entry<String, Metadata> entry : metadata.entrySet()) {
            Date reviewDate = entry.getValue().getReviewDate();
            if (DateUtil.sameDay(reviewDate, new Date()) || reviewDate.getTime() < new Date().getTime()) {
                return cardIds.get(entry.getKey());
            }
        }
        return null; // no more entries to review
    }

    /**
     * @return the number of remaining reviews in the queue
     */
    @JsonIgnore
    public int getRemainingReviews() {
        int count = 0;
        for (Map.Entry<String, Metadata> entry : metadata.entrySet()) {
            Date reviewDate = entry.getValue().getReviewDate();
            if (DateUtil.sameDay(reviewDate, new Date()) || reviewDate.getTime() < new Date().getTime()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Get the next time a given card will be reviewed.
     * @param card the card to query
     * @return the review date
     */
    public Date getNextReviewDate(Card card) {
        if (!metadata.containsKey(card.getId())) {
            throw new IllegalArgumentException("Card " + card + " has not yet been tracked.");
        }
        return metadata.get(card.getId()).getReviewDate();
    }

    /**
     * Get a list of past review sessions for a given card.
     *
     * @param card the card to query
     * @return a list of past reviews
     */
    public List<Session> getPastReviews(Card card) {
        if (!metadata.containsKey(card.getId())) {
            return new ArrayList<>();
        }

        // Defensive copy
        return new ArrayList<>(metadata.get(card.getId()).getPastReviews());
    }

    // The following getters are for TeaVM JSON serialization purposes ONLY.
    // Do not use them as a normal user.

    public Map<String, Card> getCardIds() {
        return cardIds;
    }

    public Map<String, Metadata> getMetadata() {
        return metadata;
    }

    public ReviewAlgorithm getReviewAlgorithm() {
        return reviewAlgorithm;
    }

}
