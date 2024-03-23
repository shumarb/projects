package com.fdmgroup.CreditCardProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.CreditCardProject.exception.BankTransactionNotFoundException;
import com.fdmgroup.CreditCardProject.model.BankTransaction;
import com.fdmgroup.CreditCardProject.repository.BankTransactionRepository;

@Service
public class BankTransactionService {

	@Autowired
	private BankTransactionRepository bankTransactionRepo;

	public BankTransaction getTransactionById(String id) throws BankTransactionNotFoundException {
		long longId = Long.parseLong(id);
		return bankTransactionRepo.findById(longId).orElseThrow(BankTransactionNotFoundException::new);
	}
}
