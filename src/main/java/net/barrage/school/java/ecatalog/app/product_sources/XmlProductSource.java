package net.barrage.school.java.ecatalog.app.product_sources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class XmlProductSource implements ProductSource {
    private static final Logger log = LoggerFactory.getLogger(XmlProductSource.class);
    private final XmlMapper xmlMapper;
    private final ProductSourceProperties.SourceProperty property;

    @Override
    public List<Product> getProducts() {
        try {
            return xmlMapper.readValue(URI.create(property.getUrl()).toURL().openStream(), SourceProductList.class)
                    .stream()
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
        product.setName(sourceProduct.getTitle());
        product.setDescription(sourceProduct.getDescription());
        product.setPrice(sourceProduct.getPrice());
        return product;
    }

    @Component
    public static class Factory implements ProductSource.Factory {
        @Override
        public Set<String> getSupportedFormats() {
            return Set.of("xml");
        }

        @Override
        public ProductSource create(ProductSourceProperties.SourceProperty property) {
            return new XmlProductSource(new XmlMapper(), property);
        }
    }

    private static class SourceProductList extends ArrayList<SourceProduct> {
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class SourceProduct {
        private String title;
        private String description;
        private double price;
    }
}
