package net.barrage.school.java.ecatalog.app;

import jakarta.transaction.Transactional;
import net.barrage.school.java.ecatalog.app.services.ProductServiceImpl;
import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;
import net.barrage.school.java.ecatalog.repository.MerchantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("fake")
@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    ProductServiceImpl impl;
    Merchant merchant;
    Product product;
    @Autowired
    MerchantRepository merchantRepository;

    @BeforeEach
    void createMerchant() {
        merchant = new Merchant().setName("Test");
        merchantRepository.save(merchant);
        product = new Product().setName("test").setMerchant(merchant).setPrice(10);
        impl.saveProduct(product);
    }

    @AfterEach
    void deleteMerchant() {
        if (merchant != null) {
            merchantRepository.delete(merchant);
        }
    }

    @Transactional
    @Test
    void products_are_not_empty() {
        assertFalse(impl.listProducts().isEmpty(), "Expect listProducts() to return something.");
    }

    @Transactional
    @Test
    void test_get_product_by_id() {
        assertSame(impl.getProduct(product.getId()), product);
    }

    @Transactional
    @Test
    void test_delete_product() {
        impl.deleteProduct(product.getId());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> impl.getProduct(product.getId()));
        assertSame(HttpStatus.NOT_FOUND, Objects.requireNonNull(exception.getStatusCode()));
    }

    @Transactional
    @Test
    void test_update_product() {
        Product updatedProduct = new Product()
                .setId(product.getId())
                .setName("Test2")
                .setDescription(product.getDescription())
                .setImageUrl(product.getImageUrl())
                .setMerchant(product.getMerchant())
                .setPrice(product.getPrice());
        impl.saveProduct(updatedProduct);
        assertSame(impl.getProduct(product.getId()), product);
    }


    @Transactional
    @Test
    void product_search_is_not_empty() {
        assertFalse(impl.searchProducts("test").isEmpty(), "Expect searchProducts() to return something.");
    }

    @Transactional
    @Test
    void product_search_is_empty() {
        assertTrue(impl.searchProducts("THIS IS EMPTY MESSAGE").isEmpty(), "Expect searchProducts() to return nothing.");
    }

    @Transactional
    @Test
    void getProductsByMerchantNotEmpty() {
        var products = impl.listProductsByMerchant(merchant);
        assertFalse(products.isEmpty());
    }
}