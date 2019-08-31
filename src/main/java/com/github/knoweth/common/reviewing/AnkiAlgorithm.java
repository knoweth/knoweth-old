package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.teavm.flavour.json.JsonPersistable;
import org.threeten.bp.Duration;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of a {@link ReviewAlgorithm} similar to Anki's SchedV1
 * algorithm.
 *
 * @author Kevin Liu
 */
@JsonPersistable
public class AnkiAlgorithm implements ReviewAlgorithm {
    // TODO Document
    // TODO Anki can configure these - do we want to?
    public static final Duration INTERVAL_LEARNING_ONE = Duration.ofMinutes(1);
    public static final Duration INTERVAL_LEARNING_TWO = Duration.ofMinutes(10);
    public static final Duration INTERVAL_REP_ONE = Duration.ofDays(1);
    public static final Duration INTERVAL_REP_TWO = Duration.ofDays(4);
    public static final double DEFAULT_EASE_FACTOR = 2.5;
    private static final double MIN_EASE_FACTOR = 1.3;
    /**
     * 130%
     */
    private static final double EASY_BONUS = 1.30;

    private Map<String, Double> easeFactors = new HashMap<>();
    private Map<String, Integer> repetitions = new HashMap<>();
    private Map<String, LearningSteps> learningSteps = new HashMap<>();
    /**
     * Map of millisecond-intervals
     */
    private Map<String, Long> intervals = new HashMap<>();

    private static Duration fractionalMultiplyDuration(Duration duration, double multiplier) {
        return Duration.ofSeconds(Math.round(duration.getSeconds() * multiplier));
    }

    private void changeEaseFactor(Card card, double delta) {
        easeFactors.put(card.getId(), Math.max(easeFactors.getOrDefault(card.getId(), DEFAULT_EASE_FACTOR) + delta, MIN_EASE_FACTOR));
    }

    private void multiplyInterval(Card card, double multiplier) {
        intervals.put(card.getId(),
                (Math.round(
                        intervals.getOrDefault(card.getId(), INTERVAL_REP_ONE.toMillis()) * multiplier)));
    }

    private int addRepetition(Card card) {
        int reps = repetitions.getOrDefault(card.getId(), 0) + 1;
        repetitions.put(card.getId(), reps);
        return reps;
    }

    private static LearningSteps nextStep(LearningSteps step) {
        return LearningSteps.values()[step.ordinal() + 1];
    }

    private void answerLearningCard(Card card, ReviewQuality quality) {
        LearningSteps step = learningSteps.getOrDefault(card.getId(), LearningSteps.NEW);
        switch (quality) {
            case EASY:
                // Immediate graduation
                // Reschedule as a review card
                step = LearningSteps.GRADUATED;
                rescheduleGraduatedCard(card, true);
                break;
            case GOOD:
                // Move one step toward graduation
                step = nextStep(step);
                if (step == LearningSteps.GRADUATED) {
                    rescheduleGraduatedCard(card, false);
                }
                break;
            case HARD:
                // Hard is not actually implemented in Anki's schedv1 algorithm
                // so for our purposes we will just consider it an AGAIN
            case AGAIN:
                step = LearningSteps.LEARNING_ONE;
                intervals.put(card.getId(), Duration.ofDays(1).toMillis()); // reset interval to 1 day
                break;
            default:
                throw new IllegalStateException("Unknown review quality " + quality);
        }
        learningSteps.put(card.getId(), step);
    }

    /**
     * Returns the next ideal review interval for the given graduated card, with a
     * review of a given quality.
     *
     * @param card    the card reviewed (should have graduated)
     * @param quality the quality of the review
     * @return the interval until the next review
     */
    private Duration nextReviewInterval(Card card, ReviewQuality quality, int daysOverdue) {
        double easeFactor = easeFactors.get(card.getId());
        Duration currentInterval = Duration.ofMillis(intervals.get(card.getId()));
        // (interval + delay / 4) * 1.2
        Duration hardInterval = constrainedIntervalAfter(currentInterval
                .plus(Duration.ofDays(daysOverdue).dividedBy(4))
                .multipliedBy(5).dividedBy(4), currentInterval);
        // (interval + delay / 2 ) * easeFactor
        Duration goodInterval = constrainedIntervalAfter(
                fractionalMultiplyDuration(currentInterval
                        .plus(Duration.ofDays(daysOverdue).dividedBy(2)), easeFactor),
                hardInterval);
        Duration easyInterval = constrainedIntervalAfter(
                fractionalMultiplyDuration(currentInterval
                        .plus(Duration.ofDays(daysOverdue)), easeFactor),
                goodInterval);
        switch (quality) {
            case EASY:
                return easyInterval;
            case GOOD:
                return goodInterval;
            case HARD:
                return hardInterval;
            case AGAIN:
            default:
                throw new IllegalArgumentException(
                        "Cannot calculate next review interval for a card marked as AGAIN/unknown.");
        }
    }

    /**
     * Returns an interval of max(newInterval, greaterThan + 1 day). That is,
     * the interval must be at least one day longer than greaterThan.
     * <p>
     * This is done to constrain the interval so that, e.g. the GOOD interval
     * isn't shorter than the HARD interval.
     *
     * @param newInterval the interval to use as a baseline value, given that
     *                    the returned interval will be newInterval or one day
     *                    longer than greaterThan (whichever is larger).
     * @param greaterThan the returned interval will be at least one day longer
     *                    than this interval
     * @return max(newInterval, greaterThan + 1 day)
     */
    private Duration constrainedIntervalAfter(Duration newInterval, Duration greaterThan) {
        Duration minimum = greaterThan.plusDays(1);
        // Return the larger of the two
        return minimum.compareTo(newInterval) > 0 ? minimum : newInterval;
    }

    private void answerReviewCard(Card card, ReviewQuality quality, int daysOverdue) {
        // Adjust review factor
        if (quality == ReviewQuality.AGAIN) {
            rescheduleLapse(card);
            return;
        }
        switch (quality) {
            case HARD:
                // The card’s ease is decreased by 15 percentage points and the
                // current interval is multiplied by 1.2.
                changeEaseFactor(card, -0.15);
                break;
            case EASY:
                changeEaseFactor(card, 0.15);
                break;
            case GOOD:
                break;
        }

        intervals.put(card.getId(), nextReviewInterval(card, quality, daysOverdue).toMillis());
    }

    /**
     * Reschedules a learning card that has graduated for the first time.
     *
     * @param card           the card that has just graduated
     * @param graduatedEarly whether the card graduated "early", e.g. by
     *                       clicking {@link ReviewQuality#EASY} on it.
     */
    private void rescheduleGraduatedCard(Card card, boolean graduatedEarly) {
        intervals.put(card.getId(), getGraduatingInterval(graduatedEarly).toMillis());
        easeFactors.put(card.getId(), DEFAULT_EASE_FACTOR);
    }

    private Duration getGraduatingInterval(boolean graduatedEarly) {
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
        intervals.put(card.getId(), Duration.ofDays(1).toMillis());
        changeEaseFactor(card, -0.2);
        learningSteps.put(card.getId(), LearningSteps.LEARNING_TWO);
    }

    @Override
    public Duration getNextReview(Card card, ReviewQuality quality, int daysOverdue) {
        learningSteps.putIfAbsent(card.getId(), LearningSteps.NEW);
        if (learningSteps.get(card.getId()) == LearningSteps.NEW) {
            // Move to learning step one
            learningSteps.put(card.getId(), LearningSteps.LEARNING_ONE);
        }

        if (learningSteps.get(card.getId()) == LearningSteps.LEARNING_ONE ||
                learningSteps.get(card.getId()) == LearningSteps.LEARNING_TWO) {
            answerLearningCard(card, quality);
        } else if (learningSteps.get(card.getId()) == LearningSteps.GRADUATED) {
            answerReviewCard(card, quality, daysOverdue);
        } else {
            throw new IllegalStateException("Invalid learning step");
        }

        switch (learningSteps.get(card.getId())) {
            case LEARNING_ONE:
                return INTERVAL_LEARNING_ONE;
            case LEARNING_TWO:
                return INTERVAL_LEARNING_TWO;
            case GRADUATED:
                // TODO truncate to days
                return Duration.ofMillis(intervals.get(card.getId()));
            case NEW:
            default:
                throw new IllegalStateException("Invalid learning step");
        }
    }

    public double getEaseFactor(Card card) {
        return easeFactors.getOrDefault(card.getId(), DEFAULT_EASE_FACTOR);
    }

    // Getters for TeaVM serialization ONLY

    public Map<String, Double> getEaseFactors() {
        return easeFactors;
    }

    public Map<String, Long> getIntervals() {
        return intervals;
    }

    public Map<String, Integer> getRepetitions() {
        return repetitions;
    }

    public Map<String, LearningSteps> getLearningSteps() {
        return learningSteps;
    }

    // TODO randomization of interval as anki does in fuzzedIvl

    @JsonPersistable
    private enum LearningSteps {
        NEW, LEARNING_ONE, LEARNING_TWO, GRADUATED
    }
}
