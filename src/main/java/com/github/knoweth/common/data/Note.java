package com.github.knoweth.common.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.teavm.flavour.json.JsonPersistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A note that provides reviewable cards. Can be either ONE_SIDED or
 * DOUBLE_SIDED.
 *
 * Implementation note: this could have been implemented with inheritance or
 * interfaces (which may have been the technically-superior design), but
 * unfortunately TeaVM's JSON deserializer does not handle abstract classes and
 * inheritance, so we cannot do that.
 */
@Entity
@JsonPersistable
public class Note {
    @JsonPersistable
    public enum Type {
        ONE_SIDED, TWO_SIDED
    }

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    private String front;
    private String back;
    private Type type;

    /**
     * Parameterless constructor required from TeaVM json deserialization
     */
    public Note() {

    }

    public Note(String front, String back, Type type) {
        this.front = front;
        this.back = back;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Transient
    @JsonIgnore
    public List<Card> getCards() {
        List<Card> cards = new ArrayList<>();
        switch (type) {
            case ONE_SIDED:
                cards.add(new Card(front, back, id + "-1"));
            case TWO_SIDED:
                cards.add(new Card(back, front, id + "-2"));
                break;
            default:
                throw new UnsupportedOperationException("Unimplemented note type.");
        }

        return cards;
    }
}
