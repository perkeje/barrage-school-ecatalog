package net.barrage.school.java.ecatalog.repository;

import net.barrage.school.java.ecatalog.model.Merchant;
import org.springframework.data.repository.CrudRepository;

public interface MerchantRepository extends CrudRepository<Merchant, Long> {
    Merchant findByName(String name);
}
