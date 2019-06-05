package com.github.knoweth.common.data;

import java.util.Objects;
import java.util.UUID;

// TODO should we add a Reviewable interface that Card implements?
public class Card {
    private String front;
    private String back;
    private UUID uuid;

    public Card(String front, String back, UUID uuid) {
        this.front = front;
        this.back = back;
        this.uuid = uuid;
    }

    public String getFront() {
        return front;
    }

    public String getBack() {
        return back;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return uuid.equals(card.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
