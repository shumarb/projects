package com.fdmgroup.CreditCardProject.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.fdmgroup.CreditCardProject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.CreditCardProject.service.UserService;
import com.fdmgroup.CreditCardProject.service.BankAccountService;

@Controller
public class BankAccountController {
	
	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private UserService userService;

	@PostMapping("/bankaccount")
	public String displayBankAccountDetails(@RequestParam String selectedBankAccountNumber,@AuthenticationPrincipal AuthUser principal, Model model) {
		User currentUser = userService.getUserByUsername(principal.getUsername());
		String bankAccountNumber = selectedBankAccountNumber;
		if (bankAccountNumber.length() != 0) {
			BankAccount bankAccount = bankAccountService.getBankAccountByBankAccountNumber(selectedBankAccountNumber);
			model.addAttribute("bankAccount", bankAccount);
			List<BankTransaction> transactionHistory = bankAccount.getTransactionHistoryDescending();
			List<BankTransaction> modifiedTransactionHistory = transactionHistory.stream().map(transaction -> {transaction.setAccountFromId(Long.parseLong(
					bankAccountService.getBankAccountNumberbyAccountId(transaction.getAccountFromId())));
			transaction.setAccountToId(Long.parseLong(bankAccountService.getBankAccountNumberbyAccountId(transaction.getAccountToId())));
			return transaction;
			}).collect(Collectors.toList());
			model.addAttribute("transactionHistory",modifiedTransactionHistory);
		}
        model.addAttribute("user", currentUser);
		return "bankaccount";
	}
}
