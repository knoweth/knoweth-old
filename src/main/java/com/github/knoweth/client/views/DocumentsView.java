package com.github.knoweth.client.views;

import com.github.knoweth.client.cache.UserCache;
import com.github.knoweth.client.services.Services;
import com.github.knoweth.client.services.StorageService;
import com.github.knoweth.common.data.Document;
import com.github.knoweth.server.auth.User;
import org.teavm.flavour.json.JSON;
import org.teavm.flavour.rest.RESTClient;
import org.teavm.flavour.rest.ResourceFactory;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.widgets.BackgroundWorker;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.browser.Location;
import org.teavm.jso.browser.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * List of all documents that the user can access.
 */
@BindTemplate("templates/documents.html")
public class DocumentsView extends AuthenticatedView {
    private List<Document> documents;
    private UserCache userCache;
    private String newDocumentName;

    public DocumentsView(UserCache userCache) {
        this.userCache = userCache;
        new BackgroundWorker().run(() -> documents = Services.STORAGE.getDocuments());
    }

    /**
     * @return the current list of the user's documents
     */
    public List<Document> getDocuments() {
        return documents;
    }

    /**
     * Create a new document with the given name.
     */
    public void onCreateDocument() {
        new BackgroundWorker().run(() -> {
            // Get current user to make author
            User user = userCache.loadCurrentUser();
            Document doc = new Document(user.getUsername(), newDocumentName, new ArrayList<>());
            XMLHttpRequest xhr = XMLHttpRequest.create();
            xhr.open("POST", "/api/storage/docs", true);
            xhr.onComplete(() -> {
                String location = xhr.getResponseHeader("Location");
                System.out.println("Location: " + location);
                String[] urlParts = location.split("/");
                String id = urlParts[urlParts.length - 1];
                System.out.println("ID: " + id);
                Location.current().setHash("/documents/" + id);
            });
            xhr.setRequestHeader("Content-Type", "application/json");

            xhr.send(JSON.serialize(doc).stringify());
        });
    }

    /**
     * @return the name, if any, of the new document the user is making
     */
    public String getNewDocumentName() {
        return newDocumentName;
    }

    /**
     * Set the name of the new document the user is making.
     */
    public void setNewDocumentName(String newDocumentName) {
        this.newDocumentName = newDocumentName;
    }

    /**
     * Delete a document that the user owns.
     * @param id the document to delete
     */
    public void onDeleteDocument(long id) {
        new BackgroundWorker().run(() -> {
            Services.STORAGE.deleteDocument(id);
            documents = Services.STORAGE.getDocuments(); // Reload documents
        });
    }
}
