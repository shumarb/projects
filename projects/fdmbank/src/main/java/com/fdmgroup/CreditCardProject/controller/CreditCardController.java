package com.fdmgroup.CreditCardProject.controller;

import com.fdmgroup.CreditCardProject.exception.*;
import com.fdmgroup.CreditCardProject.model.*;
import com.fdmgroup.CreditCardProject.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CreditCardController {
	@Autowired
	private CreditCardService creditCardService;

	Logger log = LoggerFactory.getLogger(CreditCardController.class);

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private UserService userService;

	@Autowired
	private BankTransactionService bankTransactionService;
	
	@Autowired
    private RewardsProfileService rewardsProfileService;

	@PostMapping("/creditcard")
	public String gotoCreditCard(@AuthenticationPrincipal AuthUser principal, @RequestParam String creditCardNumber,
			Model model) {
		User currentUser = userService.getUserByUsername(principal.getUsername());
		model.addAttribute("user", currentUser);
		CreditCard creditCard = creditCardService.getCardByNumber(creditCardNumber);
		model.addAttribute("creditCard", creditCard);
		List<CreditCardTransaction> transactionHistory = creditCard.getTransactionHistoryDescending();
		List<BankTransaction> paymentHistory = creditCard.getPaymentHistory();
		for (BankTransaction bt : paymentHistory) {
			CreditCardTransaction cct = new CreditCardTransaction(bt.getTransactionId(), null, bt.getAmount(),
					"Credit card payment from account " + bt.getAccountFromId(), null, null, 0.0);
			cct.setDate(bt.getDate());
			transactionHistory.add(cct);
		}
			transactionHistory = transactionHistory.stream()
					.sorted(Comparator.comparing(CreditCardTransaction::getDate).reversed())
					.collect(Collectors.toList());
		model.addAttribute("transactionHistory", transactionHistory);
		return "creditcard";
	}

	@PostMapping("/paybills")
	public String gotoPaybills(@AuthenticationPrincipal AuthUser principal, @RequestParam String creditCardNumber,
			Model model) {
		User currentUser = userService.getUserByUsername(principal.getUsername());
		model.addAttribute("user", currentUser);
		CreditCard creditCard = creditCardService.getCardByNumber(creditCardNumber);
		model.addAttribute("creditCard", creditCard);
		List<BankAccount> bankAccounts = currentUser.getBankAccounts();
		model.addAttribute("bankAccounts", bankAccounts);
		return "paybills";
	}

	// Temp Controller
	@PostMapping("/paybills/confirm")
	public ModelAndView gotoPB(@AuthenticationPrincipal AuthUser principal, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws BankAccountNotFoundException, InsufficientBalanceException {

		String creditCardNumber = request.getParameter("creditCardNumber");
		String account = request.getParameter("account");
		String amountValue = request.getParameter("amountValue");

		User currentUser = userService.getUserByUsername(principal.getUsername());
		CreditCard creditCards = creditCardService.getCardByNumber(creditCardNumber);
		BankAccount selectedBankAccount = currentUser.getBankAccounts().stream()
				.filter(bankAccount -> bankAccount.getAccountNumber().equals(account)).findFirst()
				.orElseThrow(BankAccountNotFoundException::new);

		BigDecimal bgamount = new BigDecimal(amountValue);
		try {
			long transactionID = bankAccountService.payBills(selectedBankAccount.getAccountNumber(), bgamount,
					creditCards);
			return new ModelAndView("redirect:/paybills/receipt/" + transactionID);

		} catch (InsufficientBalanceException e) {
			e.printStackTrace();
			redirectAttributes.addAttribute("error", "insufficientFunds");
			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
			model.addAttribute("creditCard", creditCards);
			return new ModelAndView("redirect:/paybills?error=insufficientFunds");

		} catch (ExcessPaymentException e) {
			e.printStackTrace();
			redirectAttributes.addAttribute("error", "excessiveFunds");
			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
			model.addAttribute("creditCard", creditCards);
			return new ModelAndView("redirect:/paybills?error=excessiveFunds");

		} catch (BelowMinimumPayment e) {
			e.printStackTrace();
			redirectAttributes.addAttribute("error", "belowMinimumPayment");
			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
			model.addAttribute("creditCard", creditCards);
			return new ModelAndView("redirect:/paybills?error=belowMinimumPayment");
		}

	}

	@GetMapping("/paybills/receipt/{transactionId}")
	public String gotoPBReceipt(@AuthenticationPrincipal AuthUser principal, @PathVariable String transactionId,
			Model model) throws BankTransactionNotFoundException {
		User currentUser = userService.getUserByUsername(principal.getUsername());
		model.addAttribute("user", currentUser);
		BankTransaction transaction = bankTransactionService.getTransactionById(transactionId);
		LocalDateTime date = transaction.getDate();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		double amount = transaction.getAmount().doubleValue();
		model.addAttribute("time", dtf.format(date));
		model.addAttribute("amount", amount);
		model.addAttribute("type", "Credit Card Payment");
		model.addAttribute("id", transactionId);
		model.addAttribute("source", transaction.getAccountFromId());
		model.addAttribute("destination", transaction.getAccountToId());
		return "paybills_receipt";
	}
	
	@GetMapping("/cardsinfo")
    public String goToCardinfo(@AuthenticationPrincipal AuthUser principal, Model model) {
    	User currentUser = userService.getUserByUsername(principal.getUsername());
    	List<RewardsProfile> rewardsProfiles = rewardsProfileService.getAllTypes();
    	model.addAttribute("user", currentUser);
    	model.addAttribute("rewardsProfiles", rewardsProfiles);

    	return "cardsinfo";
    }

}
