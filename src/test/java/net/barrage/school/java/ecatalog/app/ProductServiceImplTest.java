package net.barrage.school.java.ecatalog.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("fake")
@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    ProductServiceImpl impl;

    @Test
    void products_are_not_empty() {
        assertFalse(impl.listProducts().isEmpty(), "Expect listProducts() to return something.");
    }

    @Test
    void product_search_is_not_empty() {
        assertFalse(impl.searchProducts("Socks smell better").isEmpty(), "Expect listProducts() to return something.");
    }

    @Test
    void product_search_is_empty() {
        assertTrue(impl.searchProducts("THIS IS EMPTY MESSAGE").isEmpty(), "Expect listProducts() to return nothing.");
    }
}