package com.github.knoweth.common.data;


import javax.persistence.*;
import java.util.List;

@Entity
public class Document {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Account author;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Section> sections;

    protected Document() {}

    public Document(Account author, List<Section> sections) {
        this.author = author;
        this.sections = sections;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", author=" + author + "]";
    }
}
