package com.github.knoweth.client.views;

import com.github.knoweth.client.services.Services;
import com.github.knoweth.common.data.Document;
import com.github.knoweth.common.data.Note;
import com.github.knoweth.common.data.Section;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.widgets.BackgroundWorker;

import java.util.Random;

@BindTemplate("templates/document.html")
public class DocumentView extends AuthenticatedView {
    private Document document;
    private int id;
    private String saveMessage;

    public DocumentView(int id) {
        this.id = id;
        System.out.println("Fetching document #" + id);
        new BackgroundWorker().run(() -> {
            document = Services.STORAGE.getDocument(id);
        });
    }

    public Document getDocument() {
        return document;
    }

    public int getId() {
        return id;
    }

    public void addSection() {
        document.getSections().add(new Section("New Section"));
    }

    public void addNote(int sectionId) {
        document.getSections().get(sectionId).getNotes().add(
                new Note("", "", Note.Type.ONE_SIDED));
    }

    public String getSaveMessage() {
        return saveMessage;
    }

    public void save() {
        new BackgroundWorker().run(() -> {
            try {
                Services.STORAGE.setDocument(id, document);
                saveMessage = "Saved successfully.";
            } catch (Exception e) {
                e.printStackTrace();
                saveMessage = "There was an error while saving; please try again.";
            }
        });
    }

    public void removeNote(int sectionId, int noteId) {
        document.getSections().get(sectionId).getNotes().remove(noteId);
    }
}
