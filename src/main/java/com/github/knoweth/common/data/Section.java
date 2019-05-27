package com.github.knoweth.common.data;

import java.util.List;

public class Section {
    private String title;
    private List<Note> notes;
    private List<Section> subsections;

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
