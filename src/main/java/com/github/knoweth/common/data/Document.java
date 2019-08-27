package com.github.knoweth.common.data;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.teavm.flavour.json.JsonPersistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonPersistable
public class Document {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String author;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    protected Document() {}

    public Document(String author, String title, List<Section> sections) {
        this.author = author;
        this.title = title;
        this.sections = sections;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", author=" + author + "]";
    }
}
