package com.github.knoweth.server.storage;

import com.github.knoweth.common.data.Document;
import com.github.knoweth.server.auth.User;
import com.github.knoweth.server.auth.UserRepository;
import com.github.knoweth.server.auth.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A controller that manages document persistence actions.
 *
 * @author Kevin Liu
 */
@RestController
@RequestMapping("/api/storage")
public class StorageController {
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * @param username the username to check
     * @param doc      the document to query
     * @return whether the given user has access to view a document
     */
    private boolean hasAccessToDocument(String username, Document doc) {
        return doc.getAuthor().equals(username) || doc.getSharedUsers().contains(username);
    }

    /**
     * @return the list of all documents the logged-in user can access. This
     * includes owned and shared documents.
     */
    @GetMapping("/docs")
    public List<Document> getDocuments() {
        User user = UserUtils.getUser();
        String username = user.getUsername();
        List<Document> docs = new ArrayList<>();

        // TODO: This becomes an inefficient O(n) search for large #s of
        // documents. Use an index for this.
        for (Document doc : documentRepository.findAll()) {
            if (hasAccessToDocument(username, doc)) {
                docs.add(doc);
            }
        }

        return docs;
    }

    /**
     * @param id the id of the document to retrieve
     * @return the Document requested, assuming the user has permission to
     * access it
     */
    @RequestMapping("/docs/{id}")
    public Document getDocument(@PathVariable long id) {
        Document doc = documentRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User user = UserUtils.getUser();
        if (!hasAccessToDocument(user.getUsername(), doc)) {
            throw new IllegalArgumentException("Cannot access document owned by another user.");
        }
        return doc;
    }

    /**
     * Remove the given document
     * @param id id of the document to remove
     */
    @DeleteMapping("/docs/{id}")
    public void deleteDocument(@PathVariable long id) {
        if (!hasAccessToDocument(UserUtils.getUser().getUsername(), documentRepository.findById(id).get())) {
            throw new IllegalArgumentException("Cannot delete a document you don't have access to.");
        }
        documentRepository.deleteById(id);
    }

    /**
     * Create a document from a request body.
     * @param doc the document to persist
     * @return either an error or the location (URI) of the new document
     */
    @PostMapping("/docs")
    public ResponseEntity<Object> createDocument(@RequestBody Document doc) {
        User user = UserUtils.getUser();
        if (!doc.getAuthor().equals(user.getUsername())) {
            return ResponseEntity.badRequest().body("Cannot create document by different author");
        }
        Document savedDoc = documentRepository.save(doc);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedDoc.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Share a document with a given user.
     * @param id the id of the document
     * @param username the user to add
     */
    @PostMapping("/docs/{id}/share/{username}")
    public void shareDocument(@PathVariable long id, @PathVariable String username) {
        User toShare = userRepository.findByUsername(username);
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nonexistent document"));
        doc.addSharedUser(toShare.getUsername());
        documentRepository.save(doc);
    }

    /**
     * Unshare a document with a given user.
     * @param id the id of the document
     * @param username the user to remove
     */
    @DeleteMapping("/docs/{id}/share/{username}")
    public void unshareDocument(@PathVariable long id, @PathVariable String username) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nonexistent document"));
        doc.removeSharedUser(username);
        documentRepository.save(doc);
    }

    /**
     * Update a preexisting document with new content.
     *
     * @param doc the document content that is the update
     * @param id the id of the document
     * @return 204 No Content
     */
    @PutMapping("/docs/{id}")
    public ResponseEntity<Object> updateDocument(@RequestBody Document doc, @PathVariable long id) {
        User user = UserUtils.getUser();
        Optional<Document> savedDoc = documentRepository.findById(id);
        if (!savedDoc.isPresent()) {
            return ResponseEntity.notFound().build();
        } else if (!savedDoc.get().getAuthor().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        doc.setId(id);
        documentRepository.save(doc);
        return ResponseEntity.noContent().build();
    }
}
