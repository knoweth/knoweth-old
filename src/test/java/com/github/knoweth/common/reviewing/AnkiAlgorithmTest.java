package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Card;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnkiAlgorithmTest {
    @Test
    public void newCardLearning() {
        AnkiAlgorithm r = new AnkiAlgorithm();
        Card newCard = new Card(null, null, UUID.randomUUID());
        Duration nextReview = r.getNextReview(newCard, ReviewQuality.AGAIN);
        assertEquals(AnkiAlgorithm.INTERVAL_LEARNING_ONE, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD);
        assertEquals(AnkiAlgorithm.INTERVAL_LEARNING_TWO, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD);
        assertEquals(AnkiAlgorithm.INTERVAL_REP_ONE, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD);
        assertEquals(AnkiAlgorithm.INTERVAL_REP_TWO, nextReview);
    }

    @Test
    public void newCardFail() {
        AnkiAlgorithm r = new AnkiAlgorithm();
        Card newCard = new Card(null, null, UUID.randomUUID());
        Duration nextReview = r.getNextReview(newCard, ReviewQuality.AGAIN);
        assertEquals(AnkiAlgorithm.INTERVAL_LEARNING_ONE, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD);
        assertEquals(AnkiAlgorithm.INTERVAL_LEARNING_TWO, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD);
        assertEquals(AnkiAlgorithm.INTERVAL_REP_ONE, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.AGAIN);
        assertEquals(AnkiAlgorithm.INTERVAL_LEARNING_TWO, nextReview);
    }
}
