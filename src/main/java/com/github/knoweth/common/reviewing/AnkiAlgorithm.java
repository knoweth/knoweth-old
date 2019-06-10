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

    private void answerLearningCard(Card card, ReviewQuality quality) {
        LearningSteps step = learningSteps.getOrDefault(card, LearningSteps.NEW);
        switch (quality) {
            case EASY:
                // Immediate graduation
                // Reschedule as a review card
                step = LearningSteps.GRADUATED;
                // TODO reschedule as rev
                break;
            case GOOD:
                // Move one step toward graduation
                step = step.nextStep();
                break;
            case HARD:
                // Hard is not actually implemented in Anki's schedv1 algorithm
                // so for our purposes we will just consider it an AGAIN
            case AGAIN:
                step = LearningSteps.LEARNING_ONE;
                intervals.put(card, Duration.ofDays(1)); // reset interval to 1 day
                break;
            default:
                throw new IllegalStateException("Unknown review quality " + quality);
        }
        learningSteps.put(card, step);
    }

    private void answerReviewCard(Card card, ReviewQuality quality) {
        switch (quality) {
            case AGAIN:
                rescheduleLapse(card);
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

    /**
     * Reschedules a learning card that has graduated for the first time.
     * @param card the card that has just graduated
     * @param graduatedEarly whether the card graduated "early", e.g. by
     *                       clicking {@link ReviewQuality#EASY} on it.
     */
    private void rescheduleGraduatedCard(Card card, boolean graduatedEarly) {
        intervals.put(card, graduatingInterval(graduatedEarly));
        easeFactors.put(card, DEFAULT_EASE_FACTOR);
    }

    private Duration graduatingInterval(boolean graduatedEarly) {
        // TODO adjust the review interval (adjRevIvl) to make it better in
        // some way as Anki does
        if (graduatedEarly) {
            return INTERVAL_REP_TWO;
        } else {
            return INTERVAL_REP_ONE;
        }
    }

    private void rescheduleLapse(Card card) {
        // TODO leech, dynamic decks, etc.
        // The card is placed into relearning mode, the ease is
        // decreased by 20 percentage points (that is, 20 is subtracted
        // from the ease value, which is in units of percentage points),
        // and the current interval is multiplied by the value of new
        // interval (this interval will be used when the card exits
        // relearning mode).
        // Set interval to max(minimum interval, card_interval * lapse_multiplier)
        // Which is hardcoded at 1 day (which is the default minimum interval)
        // since lapse_multiplier is also 0 by default
        intervals.put(card, Duration.ofDays(1));
        changeEaseFactor(card, -0.2);
        learningSteps.put(card, LearningSteps.LEARNING_TWO);
    }

    public Duration getNextReview(Card card, ReviewQuality quality) {
        learningSteps.putIfAbsent(card, LearningSteps.NEW);
        if (learningSteps.get(card) == LearningSteps.NEW) {
            // Move to learning step one
            learningSteps.put(card, LearningSteps.LEARNING_ONE);
        }

        if (learningSteps.get(card) == LearningSteps.LEARNING_ONE ||
                learningSteps.get(card) == LearningSteps.LEARNING_TWO) {
            answerLearningCard(card, quality);
        } else if (learningSteps.get(card) == LearningSteps.GRADUATED) {
            answerReviewCard(card, quality);
        } else {
            throw new IllegalStateException("Invalid learning step");
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
