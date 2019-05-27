package com.github.knoweth.common.data;

import java.util.UUID;

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
}
