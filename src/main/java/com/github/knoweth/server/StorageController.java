package com.github.knoweth.server;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller that manages document persistence actions.
 *
 * @author Kevin Liu
 */
@RestController
public class StorageController {
    @PutMapping("/docs")
    public void createOrUpdateDocument() {

    }
}
