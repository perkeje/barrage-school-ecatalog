package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.Product;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final List<ProductSource> productSources;
    @Autowired
    @Lazy
    private ProductServiceImpl self;

    public ProductServiceImpl(
            List<ProductSource> productSources) {
        this.productSources = productSources;
    }

    @Override
    @Cacheable("products")
    public List<Product> listProducts() {
        List<Product> products = new ArrayList<Product>();
        for (ProductSource source : productSources) {
            products.addAll(source.getProducts());
        }
        products.sort(Comparator.comparing(Product::getName));
        return products;
    }

    @Scheduled(fixedDelayString = "${ecatalog.products.fetchDelay}")
    @CacheEvict(value = "products", allEntries = true)
    public void clearProductCache() {
    }

    @Override
    public List<Product> searchProducts(String query) {
        return self.listProducts()
                .stream()
                .filter(product -> StringUtils.containsIgnoreCase(product.getName(), query) ||
                        (product.getDescription() != null && StringUtils.containsIgnoreCase(product.getDescription(), query)))
                .toList();
    }

}
