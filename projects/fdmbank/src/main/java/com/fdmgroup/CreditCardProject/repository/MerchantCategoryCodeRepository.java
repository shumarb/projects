package com.fdmgroup.CreditCardProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.CreditCardProject.model.MerchantCategoryCode;

import java.util.Optional;

@Repository
public interface MerchantCategoryCodeRepository extends JpaRepository<MerchantCategoryCode, Long> {
    Optional<MerchantCategoryCode> findByCategoryCode(long categoryCode);

    boolean existsByCategoryCode(long categoryCode);
}