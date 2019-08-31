package com.github.knoweth.common.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    // An enum is not used here because we must commonly reference Types by
    // their integer value in the UI
    public static final int TYPE_ONE_SIDED = 0;
    public static final int TYPE_TWO_SIDED = 1;

    @Id
    @GeneratedValue
    private Long id;
    private String front;
    private String back;
    private int type;

    /**
     * Parameterless constructor required from TeaVM json deserialization
     */
    public Note() {

    }

    public Note(String front, String back, int type) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the cards generated from this note
     */
    @Transient
    @JsonIgnore
    public List<Card> getCards() {
        List<Card> cards = new ArrayList<>();
        switch (type) {
            case TYPE_TWO_SIDED:
                cards.add(new Card(back, front, id + "-2"));
            case TYPE_ONE_SIDED:
                cards.add(new Card(front, back, id + "-1"));
                break;
            default:
                throw new UnsupportedOperationException("Unimplemented note type.");
        }

        return cards;
    }
}
