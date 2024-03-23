package com.fdmgroup.CreditCardProject.service;

import com.fdmgroup.CreditCardProject.model.*;
import com.fdmgroup.CreditCardProject.repository.RewardsProfileRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RewardsProfileService {

	@Autowired
	private RewardsProfileRepository rewardProfileRepository;
	
	public int calculateRewardPoints(MccCategory category, CreditCard selectedCreditCard, CreditCardTransaction transaction) {
		int rewardPoints = 0;
		if(selectedCreditCard.getRewardProfile().getValidCategories().contains(category)) {
			double conversionPercentage = selectedCreditCard.getRewardProfile().getConversionPercentage() / 100.0;
			rewardPoints = (int) (conversionPercentage * transaction.getAmount().doubleValue());
		}
		return -rewardPoints;
	}
	
	public List<RewardsProfile> getAllTypes() {
		return rewardProfileRepository.findAll();
	}
}
