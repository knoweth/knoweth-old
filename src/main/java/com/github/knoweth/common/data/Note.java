package com.github.knoweth.common.data;

import org.teavm.flavour.json.JsonPersistable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import java.util.List;

@Entity
@Inheritance
@JsonPersistable
abstract class Note {
    @Id
    @GeneratedValue
    private Long id;

    public abstract List<Card> getCards();
}
