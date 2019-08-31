package com.github.knoweth.client.views;

import com.github.knoweth.client.services.Services;
import com.github.knoweth.common.data.Document;
import com.github.knoweth.common.data.Note;
import com.github.knoweth.common.data.Section;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.widgets.BackgroundWorker;

/**
 * State class for the document editor.
 *
 * @author Kevin Liu
 */
@BindTemplate("templates/document.html")
public class DocumentView extends AuthenticatedView {
    private Document document;
    private int id;
    private String saveMessage;
    private boolean inPreview;
    private String newUser;

    /**
     * Construct a new document view to show the editor.
     * @param id the id of the document to fetch
     */
    public DocumentView(int id) {
        this.id = id;
        System.out.println("Fetching document #" + id);
        new BackgroundWorker().run(() -> {
            document = Services.STORAGE.getDocument(id);
            System.out.println(document.getSharedUsers());
        });
    }

    /**
     * @return [binding] the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * @return [binding] the id of the document
     */
    public int getId() {
        return id;
    }

    /**
     * Add a new section to the document.
     */
    public void addSection() {
        document.getSections().add(new Section("New Section"));
    }

    /**
     * Add a new note to the given section.
     * @param sectionId the section for which to add a note
     */
    public void addNote(int sectionId) {
        document.getSections().get(sectionId).getNotes().add(
                new Note("", "", Note.TYPE_ONE_SIDED));
    }

    /**
     * @return the current status of saving, as a string
     */
    public String getSaveMessage() {
        return saveMessage;
    }

    /**
     * Save the document by uploading it back to the server.
     */
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

    /**
     * Remove a note from a given section, specifying it by the index of the
     * note in the section.
     *
     * @param sectionId the index of the section
     * @param noteId the index of the note
     */
    public void removeNote(int sectionId, int noteId) {
        document.getSections().get(sectionId).getNotes().remove(noteId);
    }

    /**
     * @return whether the editor is currently in "preview" mode (no editing)
     */
    public boolean getInPreview() {
        return inPreview;
    }

    /**
     * Toggle the editor from preview to edit and back.
     */
    public void togglePreview() {
        inPreview = !inPreview;
    }

    /**
     * @return the username the user is adding to the sharing list
     */
    public String getNewUser() {
        return newUser;
    }

    /**
     * Set the username the user is adding to the sharing list
     * @param newUser the username specified by the user
     */
    public void setNewUser(String newUser) {
        this.newUser = newUser;
    }

    /**
     * Add a new user to the sharing list by making a request to the server.
     *
     * This will refresh the view afterward to update the list.
     */
    public void addNewUser() {
        new BackgroundWorker().run(() -> {
            try {
                Services.STORAGE.shareDocument(id, newUser);
                document = Services.STORAGE.getDocument(id);
                saveMessage = "Successfully shared document.";
            } catch (Exception e) {
                saveMessage = "There was an error while sharing. Check that the username provided is valid.";
            }
        });
    }

    /**
     * Unshare the document by making a server request. This will refresh the
     * view afterward.
     *
     * @param username the username to remove
     */
    public void unshare(String username) {
        new BackgroundWorker().run(() -> {
            try {
                Services.STORAGE.unshareDocument(id, username);
                document = Services.STORAGE.getDocument(id);
                saveMessage = "Successfully unshared document.";
            } catch (Exception e) {
                saveMessage = "There was an error while unsharing.";
            }
        });
    }
}
