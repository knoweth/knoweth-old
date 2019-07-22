package com.github.knoweth.server.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class StorageControllerTest {
    @Autowired
    private StorageController storageController;

    @Test
    public void test() {
        System.out.println(storageController);
    }
}
