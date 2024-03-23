package com.fdmgroup.CreditCardProject.controller;

import java.math.BigDecimal;

import com.fdmgroup.CreditCardProject.exception.SelfReferenceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.CreditCardProject.exception.BankAccountNotFoundException;
import com.fdmgroup.CreditCardProject.exception.InsufficientBalanceException;
import com.fdmgroup.CreditCardProject.model.AuthUser;
import com.fdmgroup.CreditCardProject.model.User;
import com.fdmgroup.CreditCardProject.service.BankAccountService;
import com.fdmgroup.CreditCardProject.service.BankTransactionService;
import com.fdmgroup.CreditCardProject.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class TransferController {

	@Autowired
	BankAccountService bankAccountService;

	@Autowired
	UserService userService;

	@Autowired
	BankTransactionService bankTransactionService;

	private final Logger log = LogManager.getLogger(TransferController.class);

	@PostMapping("/transfer")
	public String goToAccountTransfer(@AuthenticationPrincipal AuthUser principal, @RequestParam String accountId,
			Model model) {
		User currentUser = userService.getUserByUsername(principal.getUsername());
		model.addAttribute("user", currentUser);
		if (!bankAccountService.isAccountNumberValid(accountId)) {
			return "redirect:/dashboard";
		}
		try {
			if (bankAccountService.getUsernameOfAccountByAccountNumber(accountId).equals(currentUser.getUsername())) {
				model.addAttribute("accountId", accountId);
				return "transfer";
			}
		} catch (BankAccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/dashboard";

	}

	@PostMapping("/transfer/confirm")
	public ModelAndView handleTransferRequest(@AuthenticationPrincipal AuthUser principal, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		// get request params
		String accountId = request.getParameter("accountId");
		String amount = request.getParameter("amount");
		String accountTo = request.getParameter("accountTo");

		if (accountId.equals(accountTo)) {
			// trying to transfer to same account
			return new ModelAndView("redirect:/dashboard");
		}

		log.info("Received transfer request from user '" + principal.getUsername() + "' for $" + amount
				+ " from account number '" + accountId + "' to account number '" + accountTo + "'");

		// make sure account belongs to logged in user
		try {
			if (!principal.getUsername().equals(bankAccountService.getUsernameOfAccountByAccountNumber(accountId))) {
				// account does not belong to user, return to dashboard
				log.error(
						"User '" + principal.getUsername() + "' does not control account number '" + accountId + "'.");
				return new ModelAndView("redirect:/dashboard");
			}

			BigDecimal transferAmount = new BigDecimal(amount);

			long transactionId;
			transactionId = bankAccountService.transferBetweenAccounts(accountId, accountTo, transferAmount);
			log.info("Successful transfer '" + transactionId +  "' from user '" + principal.getUsername() + "' for $" + amount
					+ " from account number '" + accountId + "' to account number '" + accountTo + "'\n");
			return new ModelAndView("redirect:/transaction/receipt/" + transactionId);

		} catch (BankAccountNotFoundException e) {
			log.error("Account number '" + accountId + "' does not exist.");
			// TODO Auto-generated catch block
			e.printStackTrace();
			redirectAttributes.addAttribute("error", "bankAccountNotFound");
			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
			return new ModelAndView("redirect:/transfer");
		} catch (InsufficientBalanceException e) {
			// TODO Auto-generated catch block
			log.error("Account number '" + accountId + "' did not have enough funds for transfer.");
			e.printStackTrace();
			redirectAttributes.addAttribute("error", "insufficientFunds");
			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
			return new ModelAndView("redirect:/transfer");
		} catch (SelfReferenceException e) {
			// should never run
		}
		return new ModelAndView("redirect:/transfer/");

	}
}
