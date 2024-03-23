package com.fdmgroup.CreditCardProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.CreditCardProject.model.RewardTransaction;

import org.springframework.stereotype.Repository;

@Repository
public interface RewardTransactionRepository extends JpaRepository<RewardTransaction, Long> {
	
}
