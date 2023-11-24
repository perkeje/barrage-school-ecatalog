package net.barrage.school.java.ecatalog.web;

import lombok.extern.slf4j.Slf4j;
import net.barrage.school.java.ecatalog.app.services.ProductSyncService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/e-catalog/api/v1/sync")
public class ProductSyncController {
    private final ProductSyncService productSyncService;

    public ProductSyncController(
            ProductSyncService productSyncService) {
        this.productSyncService = productSyncService;
    }
    @PostMapping("/{merchant_name}")
    public void syncWitMerchantName(@PathVariable("merchant_name") String merchantName) {
        productSyncService.syncWithMerchantName(merchantName);
    }
}
