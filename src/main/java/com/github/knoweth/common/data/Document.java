package com.github.knoweth.common.data;


import javax.persistence.*;
import java.util.List;

@Entity
public class Document {
    @Id
    @GeneratedValue
    private Long id;
    private String author;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Section> sections;

    protected Document() {}

    public Document(String author, List<Section> sections) {
        this.author = author;
        this.sections = sections;
    }

    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
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
