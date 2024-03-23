package com.fdmgroup.CreditCardProject.controller;

import com.fdmgroup.CreditCardProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String defaultPage() {
        return "index";
    }
    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }
    @GetMapping("/login")
    public String showLoginForm() {
        return "redirect:/index";
    }

    @GetMapping("/logout-success")
    public String logoutSuccess(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("logoutMessage", "You have been logged out successfully.");
        redirectAttributes.addFlashAttribute("logoutMessage2", "See you again!");
        return "redirect:/index";
    }
}