package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Card;
import org.junit.Test;

import java.time.Duration;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class AnkiReviewAlgorithmTest {
    @Test
    public void newCardLearning() {
        AnkiReviewAlgorithm r = new AnkiReviewAlgorithm();
        Card newCard = new Card(null, null, UUID.randomUUID());
        Duration nextReview = r.getNextReview(newCard, ReviewQuality.AGAIN);
        assertEquals(AnkiReviewAlgorithm.LEARNING_STEP_ONE, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD);
        assertEquals(AnkiReviewAlgorithm.LEARNING_STEP_TWO, nextReview);
    }
}
