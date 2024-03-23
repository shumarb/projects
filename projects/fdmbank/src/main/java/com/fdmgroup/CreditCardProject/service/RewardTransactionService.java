package com.fdmgroup.CreditCardProject.service;

import com.fdmgroup.CreditCardProject.exception.InsufficientBalanceException;
import com.fdmgroup.CreditCardProject.exception.ItemNotFoundException;
import com.fdmgroup.CreditCardProject.model.RewardItem;
import com.fdmgroup.CreditCardProject.model.RewardTransaction;
import com.fdmgroup.CreditCardProject.model.User;
import com.fdmgroup.CreditCardProject.repository.RewardTransactionRepository;
import com.fdmgroup.CreditCardProject.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RewardTransactionService {

	@Autowired
	RewardTransactionRepository rewardTransactionRepository;

	@Autowired
	UserRepository userRepository;

	@Transactional
	public RewardTransaction handleTransaction(User user, RewardItem rewardItem) throws InsufficientBalanceException {
		if (user.getRewardsPoints() < rewardItem.getCost()) {
			throw new InsufficientBalanceException();
		}
		user.setRewardsPoints(user.getRewardsPoints() - rewardItem.getCost());
		RewardTransaction transaction = rewardTransactionRepository.save(new RewardTransaction(user, rewardItem));
		user.addRewardTransaction(transaction);
		userRepository.save(user);
		return transaction;
	}
	public RewardTransaction getTransactionById(String id) throws NumberFormatException, ItemNotFoundException {
		return rewardTransactionRepository.findById(Long.parseLong(id)).orElseThrow(ItemNotFoundException::new);
	}

}
