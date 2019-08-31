package com.github.knoweth.server;

import com.github.knoweth.common.data.Document;
import com.github.knoweth.server.auth.User;
import com.github.knoweth.server.auth.UserRepository;
import com.github.knoweth.server.storage.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

/**
 * The main Spring application. Run this to run the backend server.
 */
@SpringBootApplication
// Scan all JPA entities in the data package (that of Document)
@EntityScan(basePackages = "com.github.knoweth.*")
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}