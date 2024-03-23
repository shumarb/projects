package com.fdmgroup.CreditCardProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.CreditCardProject.model.RewardItem;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardItemRepository extends JpaRepository<RewardItem, Long> {
	Optional<RewardItem> findByName(String name);
}
