package net.barrage.school.java.ecatalog.repository;

import jakarta.transaction.Transactional;
import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends CrudRepository<Product, UUID> {
    List<Product> findByMerchant(Merchant merchant);
    List<Product> findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String name, String description);
}
