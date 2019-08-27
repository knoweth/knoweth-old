package com.github.knoweth.common.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.teavm.flavour.json.JsonPersistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonPersistable
public class Section {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Note> notes = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Section> subsections = new ArrayList<>();

    public Section() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Section(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public List<Section> getSubsections() {
        return subsections;
    }
}
