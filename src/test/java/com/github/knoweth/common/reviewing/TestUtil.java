package com.github.knoweth.common.reviewing;

import com.github.knoweth.common.data.Card;

import java.util.UUID;

class TestUtil {
    static Card newCard() {
        return new Card("", "", UUID.randomUUID());
    }
}
