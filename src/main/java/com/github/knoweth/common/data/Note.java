package com.github.knoweth.common.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import java.util.List;

@Entity
@Inheritance
abstract class Note {
    @Id
    @GeneratedValue
    private Long id;

    public abstract List<Card> getCards();
}
