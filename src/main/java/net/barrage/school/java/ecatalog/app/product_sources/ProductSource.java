package net.barrage.school.java.ecatalog.app.product_sources;

import net.barrage.school.java.ecatalog.config.ProductSourceProperties;
import net.barrage.school.java.ecatalog.model.Product;

import java.util.List;
import java.util.Set;

public interface ProductSource {
    List<Product> getProducts();

    String getMerchantName();

    boolean isRemote();

    interface Factory {
        Set<String> getSupportedFormats();

        ProductSource create(ProductSourceProperties.SourceProperty psp);
    }
}
