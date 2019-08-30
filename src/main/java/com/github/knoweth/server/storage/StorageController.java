package com.github.knoweth.server.storage;

import com.github.knoweth.common.data.Document;
import com.github.knoweth.server.auth.User;
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
    private static final Logger logger = LoggerFactory.getLogger(StorageController.class);
    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/docs")
    public List<Document> getDocuments() {
        User user = UserUtils.getUser();

        return documentRepository.findByAuthor(user.getUsername());
    }

    @RequestMapping("/docs/{id}")
    public Document getDocument(@PathVariable long id) {
        return documentRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/docs/{id}")
    public void deleteDocument(@PathVariable long id) {
        documentRepository.deleteById(id);
    }

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
