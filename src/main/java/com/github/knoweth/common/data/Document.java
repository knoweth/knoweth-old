package com.github.knoweth.common.data;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.teavm.flavour.json.JsonPersistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    /**
     * A set of shared users on this document, specified by their username.
     */
    @ElementCollection
    private Set<String> sharedUsers = new HashSet<>();

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

    /**
     * @return a set of the usernames shared on this document
     */
    public Set<String> getSharedUsers() {
        // Defensive copy
        return new HashSet<>(sharedUsers);
    }

    public void addSharedUser(String username) {
        sharedUsers.add(username);
    }

    public void removeSharedUser(String username) {
        sharedUsers.remove(username);
    }
}
