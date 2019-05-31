package com.github.knoweth.server;

import com.github.knoweth.common.data.Account;
import com.github.knoweth.common.data.Document;
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
@EntityScan(basePackageClasses = Document.class)
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(DocumentRepository repository) {
        return (args) -> {
            repository.save(new Document(new Account("pog", "pog"), new ArrayList<>()));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (Document doc : repository.findAll()) {
                log.info(doc.toString());
            }
            log.info("");

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