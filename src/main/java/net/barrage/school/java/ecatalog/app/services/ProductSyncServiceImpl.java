package net.barrage.school.java.ecatalog.app.services;

import jakarta.transaction.Transactional;
import net.barrage.school.java.ecatalog.app.product_sources.ProductSource;
import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.repository.MerchantRepository;
import net.barrage.school.java.ecatalog.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSyncServiceImpl implements ProductSyncService{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    private final List<ProductSource> productSources;
    public ProductSyncServiceImpl(
            List<ProductSource> productSources) {
        this.productSources = productSources;
    }

    @Scheduled(cron = "0 * * * * *") //Every hour
    @Transactional
    public void syncDatabase() {
        for (ProductSource source : productSources) {
            if(source.isRemote()) {
                syncWithProductSource(source);
            }
        }
    }
    @Transactional
    public void syncWithMerchantName(String name) {
        for (ProductSource source : productSources) {
            if(source.getMerchantName().equalsIgnoreCase(name)) {
                syncWithProductSource(source);
            }
        }
    }

    private void syncWithProductSource(ProductSource source) {
        Merchant merchant = merchantRepository.findByName(source.getMerchantName());
        if (merchant != null) {
            merchant.getProducts().clear();
        }
        else {
            merchant = new Merchant().setName(source.getMerchantName());
            merchantRepository.save(merchant);
        }
        Merchant finalMerchant = merchant;
        var products = source.getProducts()
                .stream()
                .map(product -> product.setMerchant(finalMerchant))
                .toList();
        productRepository.saveAll(products);
    }
}
