package com.fdmgroup.CreditCardProject.controller;

import com.fdmgroup.CreditCardProject.exception.InsufficientFundsException;
import com.fdmgroup.CreditCardProject.model.*;
import com.fdmgroup.CreditCardProject.repository.MerchantCategoryCodeRepository;
import com.fdmgroup.CreditCardProject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
public class CreditCardTransactionController {


    @Autowired
    private UserService userService;
    @Autowired
    private CreditCardTransactionService creditCardTransactionService;
    @Autowired
    private RewardsProfileService rewardsProfileService;
    @Autowired
    private MerchantCategoryCodeRepository merchantCategoryCodeRepository;
    @Autowired
    private MerchantCategoryCodeService merchantCategoryCodeService;


    @GetMapping("/creditcard-add")
    public String showCreditCardForm(Model model) {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser principal = (AuthUser) authentication.getPrincipal();
        User currentUser = userService.getUserByUsername(principal.getUsername());

        // Ensure that the user has credit cards
        if (currentUser.getCreditCards() == null) {
            currentUser.setCreditCards(new ArrayList<>());
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("transaction", new CreditCardTransaction());
        return "creditcard-add";
    }

    @PostMapping("/creditcard-add")
    public String processCreditCardForm(@ModelAttribute("transaction") CreditCardTransaction transaction,
                                        @RequestParam("creditCard") String creditCardAccountNumber,
                                        @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime transactionDate,
                                        @RequestParam("merchantCategoryCode") String merchantCategoryCodeStr,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {

        // Initialize the transaction object if not already done
        if (transaction == null) {
            transaction = new CreditCardTransaction();
        }

        // Parse merchantCategoryCodeStr to integer
            long categoryCode = Long.parseLong(merchantCategoryCodeStr);

            // Check if the category code is valid

            if (!isValidCategoryCode(categoryCode)) {
                redirectAttributes.addFlashAttribute("error", "Invalid merchant category code");
                return "redirect:/creditcard-add";
            }

            // Set the category code
            transaction.getMerchantCategoryCode().setCategoryCode(categoryCode);

        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser principal = (AuthUser) authentication.getPrincipal();
        User currentUser = userService.getUserByUsername(principal.getUsername());

        // Find the selected credit card from the user's list of credit cards
        CreditCard selectedCreditCard = currentUser.getCreditCards().stream()
                .filter(card -> card.getAccountNumber().equals(creditCardAccountNumber))
                .findFirst()
                .orElse(null);

        if (selectedCreditCard == null) {
            // Credit card not found
            return "redirect:/creditcard-add?error=credit_card_not_found";
        }

        // Currency conversion rate
        double conversionRate = 1;
        if (!"USD".equals(transaction.getOriginalCurrencyCode())) {
            try {
                conversionRate = creditCardTransactionService.getCurrencyConversionRate(transaction.getOriginalCurrencyCode());
                transaction.setStoreInfo(transaction.getStoreInfo() + " ("  + transaction.getOriginalCurrencyAmount() + transaction.getOriginalCurrencyCode() + ")");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Calculate the amount based on the original currency amount and conversion rate
        BigDecimal amount = BigDecimal.valueOf(transaction.getOriginalCurrencyAmount() * conversionRate);
        amount = amount.setScale(2, RoundingMode.DOWN); // Round to two decimal places
        amount = amount.negate();
        transaction.setAmount(amount);
        // Set the transaction date
        transaction.setDate(transactionDate);

        // Process the transaction
        try {
            creditCardTransactionService.processTransaction(selectedCreditCard, transaction);
            redirectAttributes.addFlashAttribute("message", "Amount added successfully: " +
                    transaction.getAmount() + "USD (" + transaction.getOriginalCurrencyAmount() + transaction.getOriginalCurrencyCode() + ")");

        } catch (InsufficientFundsException e) {
            // Handle insufficient funds
            return "redirect:/creditcard-add?error=insufficient_funds";
        }

        // Add the transaction object back to the model for the form
        model.addAttribute("transaction", transaction);

//        reward points stuffs
        MccCategory category = merchantCategoryCodeService.getCategoryByCategoryCode(categoryCode);
        int rewardPoints = rewardsProfileService.calculateRewardPoints(category, selectedCreditCard, transaction);
        currentUser.setRewardsPoints(currentUser.getRewardsPoints() + rewardPoints);
        userService.saveUser(currentUser);

        return "redirect:/creditcard-add";
    }

    private boolean isValidCategoryCode(long categoryCode) {
        // Check if the categoryCode is valid (e.g., if it exists in your database)
        return merchantCategoryCodeRepository.existsByCategoryCode(categoryCode);
    }
}
