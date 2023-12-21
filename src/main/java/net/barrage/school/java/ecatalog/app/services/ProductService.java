package net.barrage.school.java.ecatalog.app.services;

import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    /**
     * List all available products
     */
    List<Product> listProducts();
    Product saveProduct(Product product);
    void deleteProduct(UUID id);
    Product getProduct(UUID id);
    List<Product> searchProducts(String query);
    List<Product> listProductsByMerchant(Merchant merchant);

    long countProducts();
}
