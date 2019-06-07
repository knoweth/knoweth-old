package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of a {@link ReviewAlgorithm} similar to Anki's SchedV2
 * algorithm.
 *
 * @author Kevin Liu
 */
public class AnkiAlgorithm implements ReviewAlgorithm {
    // TODO Document
    // TODO Anki can configure these - do we want to?
    public static final Duration INTERVAL_LEARNING_ONE = Duration.ofMinutes(1);
    public static final Duration INTERVAL_LEARNING_TWO = Duration.ofMinutes(10);
    public static final Duration INTERVAL_REP_ONE = Duration.ofDays(1);
    public static final Duration INTERVAL_REP_TWO = Duration.ofDays(4);
    private static final double DEFAULT_EASE_FACTOR = 2.5;
    private static final double MIN_EASE_FACTOR = 1.3;
    /**
     * 130%
     */
    private static final double EASY_BONUS = 1.30;

    private final static Logger logger = LoggerFactory.getLogger(AnkiAlgorithm.class);

    private Map<Card, Double> easeFactors = new HashMap<>();
    private Map<Card, Integer> repetitions = new HashMap<>();
    private Map<Card, LearningSteps> learningSteps = new HashMap<>();
    private Map<Card, Duration> intervals = new HashMap<>();

    private void changeEaseFactor(Card card, double delta) {
        easeFactors.put(card, Math.max(easeFactors.getOrDefault(card, DEFAULT_EASE_FACTOR) + delta, MIN_EASE_FACTOR));
    }

    private void multiplyInterval(Card card, double multiplier) {
        intervals.put(card,
                Duration.ofSeconds(Math.round(
                        intervals.getOrDefault(card, INTERVAL_REP_ONE).getSeconds() * multiplier)));
    }

    private int addRepetition(Card card) {
        int reps = repetitions.getOrDefault(card, 0) + 1;
        repetitions.put(card, reps);
        return reps;
    }

    private void reviewLearningCard(Card card, ReviewQuality quality) {
        LearningSteps step = learningSteps.getOrDefault(card, LearningSteps.NEW);
        switch (quality) {
            case EASY:
                step = LearningSteps.GRADUATED;
                break;
            case GOOD:
                step = step.nextStep();
                break;
            case HARD:
                // Hard repeats the current step.
                break;
            case AGAIN:
                step = LearningSteps.LEARNING_ONE;
                break;
            default:
                throw new IllegalStateException("Unknown review quality " + quality);
        }
        learningSteps.put(card, step);
        if (step == LearningSteps.GRADUATED) {
            // First-time graduated card review.
            reviewGraduatedCard(card, quality);
        }
    }

    private void reviewGraduatedCard(Card card, ReviewQuality quality) {
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
                learningSteps.put(card, LearningSteps.LEARNING_TWO);
                break;
            case HARD:
                // The card’s ease is decreased by 15 percentage points and the
                // current interval is multiplied by 1.2.
                changeEaseFactor(card, -0.15);
                multiplyInterval(card, 1.2);
                break;
            case EASY:
                addRepetition(card);
                changeEaseFactor(card, 0.15);
                multiplyInterval(card, EASY_BONUS);
            case GOOD:
                int reps = addRepetition(card);
                if (reps == 1) {
                    intervals.put(card, INTERVAL_REP_ONE);
                } else if (reps == 2) {
                    intervals.put(card, INTERVAL_REP_TWO);
                } else {
                    multiplyInterval(card, easeFactors.getOrDefault(card, DEFAULT_EASE_FACTOR));
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown ReviewQuality type " + quality);
        }
    }

    public Duration getNextReview(Card card, ReviewQuality quality) {
        // TODO: For easy, good, hard, the interval is also multiplied by the Interval Modifier
        switch (learningSteps.getOrDefault(card, LearningSteps.NEW)) {
            case NEW:
            case LEARNING_ONE:
            case LEARNING_TWO:
                reviewLearningCard(card, quality);
                break;
            case GRADUATED:
                reviewGraduatedCard(card, quality);
                break;
            default:
                throw new UnsupportedOperationException("Should not reach here.");
        }

        switch (learningSteps.get(card)) {
            case LEARNING_ONE:
                return INTERVAL_LEARNING_ONE;
            case LEARNING_TWO:
                return INTERVAL_LEARNING_TWO;
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
