package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Card;

import org.threeten.bp.Duration;

public interface ReviewAlgorithm {
    Duration getNextReview(Card card, ReviewQuality quality, int daysOverdue);
}
