package com.github.knoweth.client.views;

import com.github.knoweth.client.services.Services;
import com.github.knoweth.common.data.Document;
import com.github.knoweth.common.data.Section;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.widgets.BackgroundWorker;

@BindTemplate("templates/document.html")
public class DocumentView extends AuthenticatedView {
    private Document document;
    private int id;
    private String saveMessage;

    public DocumentView(int id) {
        this.id = id;
        System.out.println("Fetching document #" + id);
        new BackgroundWorker().run(() -> document = Services.STORAGE.getDocument(id));
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

    public String getSaveMessage() {
        return saveMessage;
    }

    public void save() {
        new BackgroundWorker().run(() -> {
            try {
                Services.STORAGE.setDocument(id, document);
                saveMessage = "Saved successfully.";
            } catch (Exception e) {
                saveMessage = "There was an error while saving; please try again.";
            }
        });
    }
}
