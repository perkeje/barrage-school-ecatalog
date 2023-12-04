package net.barrage.school.java.ecatalog.app.services;

import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;
import net.barrage.school.java.ecatalog.repository.ProductRepository;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Override
    public List<Product> listProducts() {
        return Lists.newArrayList(productRepository.findAll().iterator());
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product getProduct(UUID id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + id));
    }
    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(query, query);
    }

    @Override
    public List<Product> listProductsByMerchant(Merchant merchant) {
        return productRepository.findByMerchant(merchant);
    }

}
