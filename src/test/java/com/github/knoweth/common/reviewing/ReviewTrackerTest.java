package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Card;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReviewTrackerTest {
    @Test
    void constructsWithDefaults() {
        ReviewTracker t = new ReviewTracker(new AnkiAlgorithm());
        assertEquals(0, t.getCurrentReviews().size());
    }

    @Test
    void nextReviewDateForUnknownCardThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new ReviewTracker(new AnkiAlgorithm())
                        .getNextReviewDate(TestUtil.newCard()));
    }

    @Test
    void pastReviewsForUnknownCardReturnsEmptyList() {
        assertEquals(0, new ReviewTracker(new AnkiAlgorithm()).getPastReviews(TestUtil.newCard()).size());
    }

    @Test
    void recordsReview() {
        ReviewTracker t = new ReviewTracker(new AnkiAlgorithm());
        Card c = TestUtil.newCard();
        t.markReviewed(c, Duration.ZERO, ReviewQuality.GOOD);
        List<ReviewTracker.Session> reviews = t.getPastReviews(c);
        assertEquals(1, reviews.size());
        assertEquals(Duration.ZERO, reviews.get(0).getDuration());
        assertEquals(ReviewQuality.GOOD, reviews.get(0).getQuality());
    }
}
