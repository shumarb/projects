package com.fdmgroup.CreditCardProject.service;

import com.fdmgroup.CreditCardProject.model.MccCategory;
import com.fdmgroup.CreditCardProject.model.MerchantCategoryCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.CreditCardProject.repository.MerchantCategoryCodeRepository;

import java.util.Optional;

@Service
public class MerchantCategoryCodeService {
	@Autowired
	private MerchantCategoryCodeRepository merchantCategoryCodeRepository;

	public MccCategory getCategoryByCategoryCode(long categoryCode) {
		Optional<MerchantCategoryCode> mccOptional = merchantCategoryCodeRepository.findByCategoryCode(categoryCode);
        return mccOptional.map(MerchantCategoryCode::getCategory).orElse(null);
    }

}