package net.barrage.school.java.ecatalog.app.services;

import net.barrage.school.java.ecatalog.model.Merchant;

import java.util.List;

public interface MerchantService {
    List<Merchant> listMerchants();
    Merchant getMerchantById(Long merchantId);
}
