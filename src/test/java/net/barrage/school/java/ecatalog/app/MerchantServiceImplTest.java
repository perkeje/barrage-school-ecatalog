package net.barrage.school.java.ecatalog.app;

import jakarta.transaction.Transactional;
import net.barrage.school.java.ecatalog.app.services.MerchantServiceImpl;
import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.repository.MerchantRepository;
import net.barrage.school.java.ecatalog.repository.ProductRepository;
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

@ActiveProfiles("fake")
@SpringBootTest
@Transactional
class MerchantServiceImplTest {

    @Autowired
    MerchantServiceImpl impl;

    Merchant merchant;
    @Autowired
    MerchantRepository merchantRepository;
    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void createMerchant() {
        merchant = new Merchant().setName("Test");
        merchantRepository.save(merchant);
    }

    @AfterEach
    void deleteMerchant() {
        if (merchant != null) {
            merchantRepository.delete(merchant);
            merchant = null;
        }
    }

    @Test
    void merchants_not_empty() {
        assertFalse(impl.listMerchants().isEmpty(), "Expect listMerchants() to return something.");
    }

    @Test
    void getMerchantById() {
        assertSame(impl.getMerchantById(merchant.getId()), merchant);
    }

    @Test
    void getMerchantByIdNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> impl.getMerchantById(merchant.getId() + 1));
        assertSame(HttpStatus.NOT_FOUND, Objects.requireNonNull(exception.getStatusCode()));
    }

}