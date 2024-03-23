package com.fdmgroup.CreditCardProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.CreditCardProject.model.CreditCardTransaction;

@Repository
public interface CreditCardTransactionRepository extends JpaRepository<CreditCardTransaction, Long> {


}