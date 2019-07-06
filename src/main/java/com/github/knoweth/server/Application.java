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

@SpringBootApplication
// Scan all JPA entities in the data package (that of Document)
@EntityScan(basePackages = "com.github.knoweth.*")
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(DocumentRepository repository, UserRepository userRepository) {
        return (args) -> {
            repository.save(new Document("test", new ArrayList<>()));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (Document doc : repository.findAll()) {
                log.info(doc.toString());
            }
            for (User user : userRepository.findAll()) {
                log.info(user.toString());
            }

            // fetch an individual customer by ID
//            repository.findById(1L)
//                    .ifPresent(customer -> {
//                        log.info("Customer found with findById(1L):");
//                        log.info("--------------------------------");
//                        log.info(customer.toString());
//                        log.info("");
//                    });
//
//            // fetch customers by last name
//            log.info("Customer found with findByLastName('Bauer'):");
//            log.info("--------------------------------------------");
//            repository.findByLastName("Bauer").forEach(bauer -> {
//                log.info(bauer.toString());
//            });
//            // for (Customer bauer : repository.findByLastName("Bauer")) {
//            // 	log.info(bauer.toString());
//            // }
//            log.info("");
        };
    }
}