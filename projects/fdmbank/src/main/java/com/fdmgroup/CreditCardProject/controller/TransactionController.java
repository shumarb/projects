package com.fdmgroup.CreditCardProject.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.CreditCardProject.exception.BankAccountNotFoundException;
import com.fdmgroup.CreditCardProject.exception.BankTransactionNotFoundException;
import com.fdmgroup.CreditCardProject.exception.InsufficientBalanceException;
import com.fdmgroup.CreditCardProject.model.AuthUser;
import com.fdmgroup.CreditCardProject.model.BankTransaction;
import com.fdmgroup.CreditCardProject.model.User;
import com.fdmgroup.CreditCardProject.service.BankAccountService;
import com.fdmgroup.CreditCardProject.service.BankTransactionService;
import com.fdmgroup.CreditCardProject.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class TransactionController {

	@Autowired
	BankAccountService bankAccountService;

	@Autowired
	UserService userService;

	@Autowired
	BankTransactionService bankTransactionService;

	@PostMapping("/transaction")
	public String goToTransactionOrDashboardPage(@AuthenticationPrincipal AuthUser principal,
			@RequestParam String accountId, Model model) {
		User currentUser = userService.getUserByUsername(principal.getUsername());
		model.addAttribute("user", currentUser);
		if (!bankAccountService.isAccountNumberValid(accountId)) {
			return "redirect:/dashboard";
		}
		try {
			if (bankAccountService.getUsernameOfAccountByAccountNumber(accountId).equals(currentUser.getUsername())) {
				model.addAttribute("accountId", accountId);
				return "transaction";
			}
		} catch (BankAccountNotFoundException e) {
			e.printStackTrace();
		}
		return "redirect:/dashboard";
	}

	@PostMapping("/transaction/confirm")
	public ModelAndView handleTransactionRequest(@AuthenticationPrincipal AuthUser principal,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		// get request params
		String action = request.getParameter("action");
		String accountId = request.getParameter("accountId");
		String amount = request.getParameter("amount");

		try {
			if (!principal.getUsername().equals(bankAccountService.getUsernameOfAccountByAccountNumber(accountId))) {
				// account does not belong to user, return to dashboard
				return new ModelAndView("redirect:/dashboard");
			}
		} catch (BankAccountNotFoundException e) {
			e.printStackTrace();
		}

		if (action.equals("withdraw")) {
			try {
				long transactionId = bankAccountService.withdrawFromAccount(accountId, new BigDecimal(amount));
				return new ModelAndView("redirect:/transaction/receipt/" + transactionId);
			} catch (BankAccountNotFoundException e) {
				e.printStackTrace();
				return new ModelAndView("redirect:/dashboard");
			} catch (InsufficientBalanceException e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("error", "insufficientFunds");
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				return new ModelAndView("redirect:/transaction?error=insufficientFunds");
			}
		} else if (action.equals("deposit")) {
			// make sure account belongs to logged in user
			try {
				long transactionId = bankAccountService.depositToAccount(accountId, new BigDecimal(amount));
				return new ModelAndView("redirect:/transaction/receipt/" + transactionId);
			} catch (BankAccountNotFoundException e) {
				e.printStackTrace();
				return new ModelAndView("redirect:/dashboard");
			}
		} else {
			return new ModelAndView("redirect:/dashboard");
		}
	}

	@GetMapping("/transaction/receipt/{transactionId}")
	public String goToTransactionReceiptPage(@AuthenticationPrincipal AuthUser principal,
			@PathVariable String transactionId, Model model) {
		User currentUser = userService.getUserByUsername(principal.getUsername());
		model.addAttribute("user", currentUser);
		try {
			BankTransaction transaction = bankTransactionService.getTransactionById(transactionId);
			List<Long> userBankAccounts = bankAccountService.getBankAccountIdsByUsername(principal.getUsername());
			// verify user should be able to view the transaction
			if (!userBankAccounts.contains(transaction.getAccountFromId())
					&& !userBankAccounts.contains(transaction.getAccountToId())) {
				return "redirect:/dashboard";
			}

			LocalDateTime transactionTime = transaction.getDate();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			String formattedTimestamp = dtf.format(transactionTime);
			double amount = transaction.getAmount().doubleValue();
			String transactionType = switch (transaction.getType()) {
			case DEPOSIT -> "Deposit";
			case WITHDRAWAL -> "Withdrawal";
			case TRANSFER -> "Transfer";
			case INVALID -> null;
			case PAYMENT -> "Credit Card Payment";
			};
			if (transactionType == null) {
				return "redirect:/dashboard";
			}
			String source = null;
			if (transactionType.equals("Transfer")) {
				source = "Transfer from "
						+ bankAccountService.getBankAccountNumberbyAccountId(transaction.getAccountFromId()) + " to "
						+ bankAccountService.getBankAccountNumberbyAccountId(transaction.getAccountToId());
			} else {

				source = "Cash " + transactionType;
			}
			model.addAttribute("id", transactionId);
			model.addAttribute("amount", amount);
			model.addAttribute("source", source);
			model.addAttribute("time", formattedTimestamp);
			model.addAttribute("type", transactionType);
			return "receipt";
		} catch (BankTransactionNotFoundException e) {
			e.printStackTrace();
			return "redirect:/dashboard";
		}
	}
}
