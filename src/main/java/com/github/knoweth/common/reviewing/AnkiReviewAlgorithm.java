package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class AnkiReviewAlgorithm implements ReviewAlgorithm {
    // TODO Document
    // TODO Anki can configure these - do we want to?
    public static final Duration LEARNING_STEP_ONE = Duration.ofMinutes(1);
    public static final Duration LEARNING_STEP_TWO = Duration.ofMinutes(10);
    public static final Duration REPETITION_INTERVAL_ONE = Duration.ofDays(1);
    public static final Duration REPETITION_INTERVAL_TWO = Duration.ofDays(4);
    private static final double DEFAULT_EASE_FACTOR = 2.5;
    private static final double MIN_EASE_FACTOR = 1.3;
    /**
     * 130%
     */
    private static final double EASY_BONUS = 1.30;

    private final static Logger logger = LoggerFactory.getLogger(AnkiReviewAlgorithm.class);

    private Map<Card, Double> easeFactors = new HashMap<>();
    private Map<Card, Integer> repetitions = new HashMap<>();
    private Map<Card, LearningSteps> learningSteps = new HashMap<>();
    private Map<Card, Duration> intervals = new HashMap<>();

    private void changeEaseFactor(Card card, double delta) {
        easeFactors.put(card, easeFactors.getOrDefault(card, DEFAULT_EASE_FACTOR) + delta);
    }

    private void multiplyInterval(Card card, double multiplier) {
        intervals.put(card,
                Duration.ofSeconds(Math.round(
                        intervals.getOrDefault(card, REPETITION_INTERVAL_ONE).getSeconds() * multiplier)));
    }

    private void unlearn(Card card) {
        learningSteps.put(card, LearningSteps.LEARNING_ONE);
    }

    private void uplearn(Card card) {
        LearningSteps current = learningSteps.getOrDefault(card, LearningSteps.NEW);
        if (current != LearningSteps.GRADUATED) {
            LearningSteps next = current.nextStep();
            logger.debug("Moving card " + card + " from " + current + " to " + next);
            logger.debug("Ease: {}", easeFactors.get(card));
            logger.debug("Interval: {}", intervals.get(card));
            learningSteps.put(card, current.nextStep());

            if (next == LearningSteps.GRADUATED) {
                intervals.put(card, REPETITION_INTERVAL_ONE);
            }
        }
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
                multiplyInterval(card, 0);
                unlearn(card);
                break;
            case HARD:
                // The card’s ease is decreased by 15 percentage points and the
                // current interval is multiplied by 1.2.
                changeEaseFactor(card, -0.15);
                multiplyInterval(card, 1.2);
                break;
            case EASY:
                changeEaseFactor(card, 0.15);
            case GOOD:
                uplearn(card);
                // TODO ugly code
                int reps = repetitions.getOrDefault(card, 0);
                if (learningSteps.get(card) == LearningSteps.GRADUATED) {
                    reps++;
                    repetitions.put(card, reps);
                }
                // The current interval is multiplied by the current ease times
                // the easy bonus and the ease is increased by 15 percentage points.
                if (reps == 1) {
                    intervals.put(card, REPETITION_INTERVAL_ONE);
                } else if (reps == 2) {
                    intervals.put(card, REPETITION_INTERVAL_TWO);
                } else {
                    multiplyInterval(card, easeFactors.getOrDefault(card, DEFAULT_EASE_FACTOR) * EASY_BONUS);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown ReviewQuality type " + quality);
        }

        switch (learningSteps.getOrDefault(card, LearningSteps.LEARNING_ONE)) {
            case LEARNING_ONE:
                return LEARNING_STEP_ONE;
            case LEARNING_TWO:
                return LEARNING_STEP_TWO;
            case GRADUATED:
                logger.debug("Card graduated - returning interval {}", intervals.get(card));
                return intervals.get(card);
            case NEW:
            default:
                // Should not reach here.
                throw new UnsupportedOperationException("Should not reach here.");
        }
    }

    private enum LearningSteps {
        NEW, LEARNING_ONE, LEARNING_TWO, GRADUATED {
            @Override
            public LearningSteps nextStep() {
                throw new IllegalStateException("Cannot get the next step - card has already graduated.");
            }
        };

        public LearningSteps nextStep() {
            return values()[ordinal() + 1];
        }
    }
}
