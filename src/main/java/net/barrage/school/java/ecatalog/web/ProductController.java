package net.barrage.school.java.ecatalog.web;

import jakarta.validation.Valid;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.barrage.school.java.ecatalog.app.services.ProductService;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/e-catalog/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final MeterRegistry meterRegistry;

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final Counter listProductsCounter = meterRegistry
            .counter("ecatalog.products.listProducts");

    @GetMapping
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    public List<Product> listProducts() {
        var products = productService.listProducts();
        log.trace("listProducts -> {}", products);
        getListProductsCounter().increment();
        return products;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    public Product createProduct(@Valid @RequestBody Product product) {
        var createdProduct = productService.saveProduct(product);
        log.trace("createProduct -> {}", createdProduct);
        return createdProduct;
    }

    @PutMapping("/{product_id}")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    public Product updateProduct(@PathVariable("product_id") UUID productId, @Valid @RequestBody Product product) {
        product = product.setId(productId);
        var updatedProduct = productService.saveProduct(product);
        log.trace("updateProduct -> {}", updatedProduct);
        return updatedProduct;
    }

    @DeleteMapping("/{product_id}")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    public void deleteProduct(@PathVariable("product_id") UUID productId) {
        productService.deleteProduct(productId);
        log.trace("deleteProduct -> {}", productId);
    }

    @GetMapping("/{product_id}")
    public Product getProduct(@PathVariable("product_id") UUID productId) {
        var product = productService.getProduct(productId);
        log.trace("deleteProduct -> {}", product);
        return product;
    }

    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam("q") String query
    ) {
        var products = productService.searchProducts(query);
        log.trace("searchProducts[{}] -> {}", query, products);
        return products;
    }
}
