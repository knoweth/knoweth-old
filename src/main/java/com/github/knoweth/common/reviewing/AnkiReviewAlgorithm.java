package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Card;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class AnkiReviewAlgorithm implements ReviewAlgorithm {
    // TODO Document
    // TODO Anki can configure these - do we want to?
    public static final Duration LEARNING_STEP_ONE = Duration.ofMinutes(1);
    public static final Duration LEARNING_STEP_TWO = Duration.ofMinutes(10);
    private static final int REPETITION_INTERVAL_ONE = 1;
    private static final int REPETITION_INTERVAL_TWO = 4;
    private static final double DEFAULT_EASE_FACTOR = 2.5;
    private static final double MIN_EASE_FACTOR = 1.3;
    /**
     * When a card has "lapsed" (the user has pressed Again on it), this is the
     * multiplier by which the card's current interval is multiplied. By
     * default, with a multiplier of 0, the card's past interval is entirely
     * erased.
     */
    private static final double NEW_INTERVAL_PCT = 0;
    /**
     * 130%
     */
    private static final double EASY_BONUS = 1.30;
    private Map<Card, Double> easeFactors = new HashMap<>();
    private Map<Card, Integer> intervals = new HashMap<>();

    private void changeEaseFactor(Card card, double delta) {
        easeFactors.put(card, easeFactors.getOrDefault(card, DEFAULT_EASE_FACTOR) + delta);
    }

    private void multiplyInterval(Card card, double multiplier) {
        intervals.put(card, (int) Math.round(intervals.getOrDefault(card, REPETITION_INTERVAL_ONE) * multiplier));
    }

    public Duration getNextReview(Card card, ReviewQuality quality) {
        // TODO: For easy, good, hard, the interval is also multiplied by the Interval Modifier
        switch (quality) {
            case AGAIN:
                // The card is placed into relearning mode, the ease is
                // decreased by 20 percentage points (that is, 20 is subtracted
                // from the ease value, which is in units of percentage points),
                // and the current interval is multiplied by the value of new
                // interval (this interval will be used when the card exits
                // relearning mode).
                changeEaseFactor(card, -0.2);
                multiplyInterval(card, NEW_INTERVAL_PCT);
                return LearningSteps.ONE.duration;
            case HARD:
                // The card’s ease is decreased by 15 percentage points and the
                // current interval is multiplied by 1.2.
                changeEaseFactor(card, -0.15);
                multiplyInterval(card, 1.2);
                break;
            case GOOD:
                // The current interval is multiplied by the current ease. The
                // ease is unchanged.
                multiplyInterval(card, easeFactors.getOrDefault(card, DEFAULT_EASE_FACTOR));
                break;
            case EASY:
                // The current interval is multiplied by the current ease times
                // the easy bonus and the ease is increased by 15 percentage points.
                multiplyInterval(card, easeFactors.getOrDefault(card, DEFAULT_EASE_FACTOR) * EASY_BONUS);
                changeEaseFactor(card, 0.15);
                break;
            default:
                throw new UnsupportedOperationException("Unknown ReviewQuality type " + quality);
        }
        return Duration.ofDays(intervals.get(card));
    }

    private enum LearningSteps {
        ONE(1), TWO(10), LEARNED;

        private final Duration duration;

        LearningSteps(int minutes) {
            this.duration = Duration.ofMinutes(minutes);
        }
        LearningSteps() {
            this.duration = null;
        }
    }
}
