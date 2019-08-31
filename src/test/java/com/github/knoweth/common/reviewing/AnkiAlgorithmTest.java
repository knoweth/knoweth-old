package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Card;
import org.junit.jupiter.api.Test;

import org.threeten.bp.Duration;
import java.util.UUID;

import static com.github.knoweth.common.reviewing.TestUtil.newCard;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnkiAlgorithmTest {
    private AnkiAlgorithm r = new AnkiAlgorithm();

    @Test
    void continualGoodIntervalTests() {
        Card newCard = newCard();
        Duration nextReview = r.getNextReview(newCard, ReviewQuality.AGAIN, 0);
        assertEquals(AnkiAlgorithm.INTERVAL_LEARNING_ONE, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD, 0);
        assertEquals(AnkiAlgorithm.INTERVAL_LEARNING_TWO, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD, 0);
        assertEquals(AnkiAlgorithm.INTERVAL_REP_ONE, nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD, 0);
        assertEquals(Duration.ofDays(3), nextReview);
        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD, 0);
        assertEquals(Duration.ofHours(180), nextReview);
        // Ease factor should be unchanged after repeated Goods
        assertEquals(AnkiAlgorithm.DEFAULT_EASE_FACTOR, r.getEaseFactor(newCard));
        // Randomization makes tests unreliable beyond this point
//        nextReview = r.getNextReview(newCard, ReviewQuality.GOOD, 0);
//        assertEquals(Duration.ofDays(22), nextReview);
    }

    @Test
    void learningCardsShouldNotChangeEaseFactor() {
        Card newCard = newCard();
        Duration nextReview = r.getNextReview(newCard, ReviewQuality.AGAIN, 0);
        assertEquals(AnkiAlgorithm.INTERVAL_LEARNING_ONE, nextReview);
        // Saying "again" in the learning period should not change the ease factor
        assertEquals(AnkiAlgorithm.DEFAULT_EASE_FACTOR, r.getEaseFactor(newCard));
        r.getNextReview(newCard, ReviewQuality.AGAIN, 0);
        assertEquals(AnkiAlgorithm.DEFAULT_EASE_FACTOR, r.getEaseFactor(newCard));
    }

    @Test
    void learnedCardLapseChecks() {
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
