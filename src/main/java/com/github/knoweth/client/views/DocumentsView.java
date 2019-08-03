package com.github.knoweth.client.views;

import com.github.knoweth.client.services.StorageService;
import com.github.knoweth.common.data.Document;
import org.teavm.flavour.rest.RESTClient;
import org.teavm.flavour.rest.ResourceFactory;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.widgets.BackgroundWorker;

import java.util.ArrayList;
import java.util.List;

@BindTemplate("templates/documents.html")
public class DocumentsView {
    private List<Document> documents = new ArrayList<>();

    public DocumentsView() {
        StorageService r = RESTClient.factory(StorageService.class).createResource("api");
        BackgroundWorker worker = new BackgroundWorker();
        worker.run(() -> {
            try {
                System.out.println(r.getDocuments(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        documents.add(new Document("profound", new ArrayList<>()));
    }

    public List<Document> getDocuments() {
        return documents;
    }
}
