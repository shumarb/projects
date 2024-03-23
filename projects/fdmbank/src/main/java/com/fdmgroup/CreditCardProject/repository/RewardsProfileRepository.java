package com.fdmgroup.CreditCardProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.CreditCardProject.model.RewardsProfile;

@Repository
public interface RewardsProfileRepository extends JpaRepository<RewardsProfile, Long>{

}
