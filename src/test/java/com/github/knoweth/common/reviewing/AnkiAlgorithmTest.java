package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Card;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnkiAlgorithmTest {
    private AnkiAlgorithm r = new AnkiAlgorithm();

    private Card newCard() {
        return new Card(null, null, UUID.randomUUID());
    }

    @Test
    void newCardLearning() {
        Card newCard = newCard();
        Duration nextReview = r.getNextReview(newCard, ReviewQuality.AGAIN, 0);
        assertEquals(AnkiAlgorithm.INTERVAL_LEARNING_ONE, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD, 0);
        assertEquals(AnkiAlgorithm.INTERVAL_LEARNING_TWO, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD, 0);
        assertEquals(AnkiAlgorithm.INTERVAL_REP_ONE, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD, 0);
        assertEquals(AnkiAlgorithm.INTERVAL_REP_TWO, nextReview);
    }

    @Test
    void newCardFail() {
        Card newCard = newCard();
        Duration nextReview = r.getNextReview(newCard, ReviewQuality.AGAIN, 0);
        assertEquals(AnkiAlgorithm.INTERVAL_LEARNING_ONE, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD, 0);
        assertEquals(AnkiAlgorithm.INTERVAL_LEARNING_TWO, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD, 0);
        assertEquals(AnkiAlgorithm.INTERVAL_REP_ONE, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.AGAIN, 0);
        assertEquals(AnkiAlgorithm.INTERVAL_LEARNING_TWO, nextReview);
    }

    @Test
    void newCardEasySchedulesIn4Days() {
        Card newCard = newCard();
        Duration nextReview = r.getNextReview(newCard, ReviewQuality.EASY, 0);
        // Should have graduated from learning to be seen in 4 days
        assertEquals(AnkiAlgorithm.INTERVAL_REP_TWO, nextReview);
    }

    @Test
    void hardNewCardStaysAtLearningStepOne() {
        Card c = newCard();
        Duration nextReview = r.getNextReview(c, ReviewQuality.HARD, 0);
        // Hard should not move on from learning step one
        assertEquals(AnkiAlgorithm.INTERVAL_LEARNING_ONE, nextReview);
        nextReview = r.getNextReview(c, ReviewQuality.HARD, 0);
        // Hard should not move on from learning step one
        assertEquals(AnkiAlgorithm.INTERVAL_LEARNING_ONE, nextReview);
    }
}
