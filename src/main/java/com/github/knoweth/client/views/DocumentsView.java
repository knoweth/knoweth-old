package com.github.knoweth.client.views;

import com.github.knoweth.client.cache.UserCache;
import com.github.knoweth.client.services.Services;
import com.github.knoweth.client.services.StorageService;
import com.github.knoweth.common.data.Document;
import com.github.knoweth.server.auth.User;
import org.teavm.flavour.rest.RESTClient;
import org.teavm.flavour.rest.ResourceFactory;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.widgets.BackgroundWorker;

import java.util.ArrayList;
import java.util.List;

@BindTemplate("templates/documents.html")
public class DocumentsView extends AuthenticatedView {
    private List<Document> documents;
    private UserCache userCache;
    public String newDocumentName;

    public DocumentsView(UserCache userCache) {
        this.userCache = userCache;
    }

    public List<Document> getDocuments() {
        if (documents == null) {
            new BackgroundWorker().run(() -> documents = Services.STORAGE.getDocuments());
        }

        return documents;
    }

    public void onCreateDocument() {
        new BackgroundWorker().run(() -> {
            // Get current user to make author
            User user = userCache.loadCurrentUser();
            Services.STORAGE.createDocument(new Document(user.getUsername(), newDocumentName, new ArrayList<>()));
        });
    }
}
