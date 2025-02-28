package net.barrage.school.java.ecatalog.app.services;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.repository.MerchantRepository;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MerchantServiceImpl implements MerchantService{
    @Autowired
    private MerchantRepository merchantRepository;
    private final MeterRegistry meterRegistry;

    @Autowired
    public MerchantServiceImpl(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        Gauge.builder("ecatalog.merchants.count", this, MerchantService::countMerchants)
                .description("Number of products in the application")
                .register(meterRegistry);
    }

    @Override
    @Cacheable("merchants")
    public List<Merchant> listMerchants() {
        return Lists.newArrayList(merchantRepository.findAll().iterator());
    }

    @Override
    public Merchant getMerchantById(Long merchantId) {
        return merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant not found with id: " + merchantId));
    }

    @Scheduled(fixedDelayString = "${ecatalog.products.fetchDelay}")
    @CacheEvict(value = "merchants", allEntries = true)
    public void clearMerchantCache() {
    }

    @Override
    public long countMerchants() {
        return merchantRepository.count();
    }
}
