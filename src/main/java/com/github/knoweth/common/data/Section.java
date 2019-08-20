package com.github.knoweth.common.data;

import org.teavm.flavour.json.JsonPersistable;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonPersistable
public class Section {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Note> notes;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Section> subsections;

    public Section() {}

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
