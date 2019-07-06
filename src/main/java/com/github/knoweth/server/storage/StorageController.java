package com.github.knoweth.server.storage;

import com.github.knoweth.common.data.Document;
import com.github.knoweth.server.auth.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
