package com.fdmgroup.CreditCardProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.CreditCardProject.model.BankTransaction;

@Repository
public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long>{

}
