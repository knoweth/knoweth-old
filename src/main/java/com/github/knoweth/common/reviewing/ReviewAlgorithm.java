package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Card;

import java.time.LocalDate;

public interface ReviewAlgorithm {
    LocalDate getNextReview(Card card, ReviewQuality quality);
}
