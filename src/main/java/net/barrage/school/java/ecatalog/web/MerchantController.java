package net.barrage.school.java.ecatalog.web;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.barrage.school.java.ecatalog.app.services.MerchantService;
import net.barrage.school.java.ecatalog.app.services.ProductService;
import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/e-catalog/api/v1/merchants")
public class MerchantController {
    private final MerchantService merchantService;
    private final ProductService productService;
    private final MeterRegistry meterRegistry;

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final Counter listMerchantsCounter = meterRegistry
            .counter("ecatalog.products.listMerchants");

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final Counter listMerchantProductsCounter = meterRegistry
            .counter("ecatalog.products.listMerchantProductsCounter");

    @GetMapping
    public List<Merchant> listMerchants() {
        var merchants = merchantService.listMerchants();
        getListMerchantsCounter().increment();
        log.trace("listMerchants -> {}", merchants);
        return merchants;
    }

    @GetMapping("/{merchant_id}")
    public Merchant getMerchantById(@PathVariable("merchant_id") Long merchantId) {
        var merchant = merchantService.getMerchantById(merchantId);
        log.trace("getMerchantById -> {}", merchant);
        return merchant;
    }

    @GetMapping("/{merchant_id}/products")
    public List<Product> listProductsByMerchantId(@PathVariable("merchant_id") Long merchantId) {
        var products = productService.listProductsByMerchant(merchantService.getMerchantById(merchantId));
        getListMerchantProductsCounter().increment();
        log.trace("listProductsByMerchantId -> {}", products);
        return products;
    }
}
