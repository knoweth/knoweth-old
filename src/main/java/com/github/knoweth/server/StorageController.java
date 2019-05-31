package com.github.knoweth.server;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller that manages document persistence actions.
 *
 * @author Kevin Liu
 */
@RestController
@RequestMapping("/api/storage")
public class StorageController {
    @PutMapping("/docs")
    public void createOrUpdateDocument() {

    }
}
