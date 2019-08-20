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

    public DocumentView(int id) {
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
}
