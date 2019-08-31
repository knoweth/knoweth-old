package com.github.knoweth.common.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.teavm.flavour.json.JsonPersistable;

import java.util.Objects;
import java.util.UUID;

/**
 * An atomized reviewable item. Has a front and back.
 */
@JsonPersistable
public class Card {
    private String front;
    private String back;
    private String id;

    @JsonCreator
    public Card(@JsonProperty("front") String front,
                @JsonProperty("back") String back,
                @JsonProperty("id") String id) {
        this.front = front;
        this.back = back;
        this.id = id;
    }

    public String getFront() {
        return front;
    }

    public String getBack() {
        return back;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id.equals(card.id);
    }

    @Override
    public String toString() {
        return "Card[id=" + getId() + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
