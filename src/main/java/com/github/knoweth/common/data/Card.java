package com.github.knoweth.common.data;

import org.teavm.flavour.json.JsonPersistable;

import java.util.Objects;
import java.util.UUID;

// TODO should we add a Reviewable interface that Card implements?
@JsonPersistable
public class Card {
    private String front;
    private String back;
    private String id;

    public Card(String front, String back, String id) {
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
