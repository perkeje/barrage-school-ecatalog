package net.barrage.school.java.ecatalog.app.product_sources;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.barrage.school.java.ecatalog.config.ProductSourceProperties;
import net.barrage.school.java.ecatalog.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class JsonProductSource implements ProductSource {
    private static final Logger log = LoggerFactory.getLogger(JsonProductSource.class);
    private final ObjectMapper objectMapper;
    private final ProductSourceProperties.SourceProperty property;

    @Override
    public List<Product> getProducts() {
        try {
            return objectMapper.readValue(URI.create(property.getUrl()).toURL().openStream(), SourceProductList.class).stream()
                    .map(this::convert)
                    .toList();
        } catch (Exception e) {
            log.warn("Oops!", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getMerchantName() {
        return property.getName();
    }

    @Override
    public boolean isRemote() {
        return property.isRemote();
    }

    private Product convert(SourceProduct sourceProduct) {
        var product = new Product();
        product.setId(UUID.randomUUID());
        product.setName(sourceProduct.getName());
        product.setDescription(sourceProduct.getNotes());
        product.setImageUrl(Optional.ofNullable(sourceProduct.productMedia)
                .flatMap(media -> media.stream().findFirst())
                .orElse(null));
        product.setPrice(sourceProduct.getPrice());
        return product;
    }

    @RequiredArgsConstructor
    @Component
    public static class Factory implements ProductSource.Factory {
        private final ObjectMapper objectMapper;

        @Override
        public Set<String> getSupportedFormats() {
            return Set.of("json");
        }

        @Override
        public ProductSource create(ProductSourceProperties.SourceProperty property) {
            return new JsonProductSource(objectMapper, property);
        }
    }

    private static class SourceProductList extends ArrayList<SourceProduct> {
    }

    @Data
    private static class SourceProduct {
        private String name;
        private String notes;
        private List<String> productMedia;
        private double price;
    }
}
