package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Card;

import java.time.Duration;
import java.util.Set;

public interface ReviewAlgorithm {
    Duration getNextReview(Card card, ReviewQuality quality);
}
