package com.github.knoweth.common.reviewing;

import org.teavm.flavour.json.JsonPersistable;

@JsonPersistable
public enum ReviewQuality {
    AGAIN, HARD, GOOD, EASY
}
