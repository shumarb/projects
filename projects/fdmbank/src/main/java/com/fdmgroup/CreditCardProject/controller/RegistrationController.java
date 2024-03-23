package com.fdmgroup.CreditCardProject.controller;


import com.fdmgroup.CreditCardProject.service.BankAccountService;
import com.fdmgroup.CreditCardProject.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.CreditCardProject.model.User;
import com.fdmgroup.CreditCardProject.service.UserService;

@Controller
public class RegistrationController {

	@Autowired
	private UserService userService;
	@Autowired
	private BankAccountService bankAccountService;
	@Autowired
	private CreditCardService creditCardService;

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("signUpActive", true);
		return "index";
	}

	@PostMapping("/register")
	public String registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String confirmPassword, RedirectAttributes redirectAttributes) {

		if (!password.equals(confirmPassword)) {
			redirectAttributes.addFlashAttribute("unsuccessfulMessage", "Passwords do not match. Please try again.");
			redirectAttributes.addFlashAttribute("signUpActive", true);
			return "redirect:/index";
		}

		if (userService.registerUser(username, password)) {
			User user = userService.getUserByUsername(username);

			// Create a new BankAccount for the user
			bankAccountService.createBankAccountForUser(user);

			// Create a new CreditCardAccount for the user
			creditCardService.createCreditCardForUser(user,5000,5000,1);


			redirectAttributes.addFlashAttribute("successMessage", "Registration for " + username + " successful.");
			redirectAttributes.addFlashAttribute("successMessage2", "Please Proceed to Login.");

			return "redirect:/index";
		} else {
			redirectAttributes.addFlashAttribute("unsuccessfulMessage", "Registration Error! Possible Username is already in use! Please try again.");
			return "redirect:/register";
		}
	}
}