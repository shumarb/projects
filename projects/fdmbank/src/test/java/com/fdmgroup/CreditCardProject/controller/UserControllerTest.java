package com.fdmgroup.CreditCardProject.controller;

import com.fdmgroup.CreditCardProject.model.User;
import com.fdmgroup.CreditCardProject.repository.UserRepository;
import com.fdmgroup.CreditCardProject.service.BankAccountService;
import com.fdmgroup.CreditCardProject.service.CreditCardService;
import com.fdmgroup.CreditCardProject.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @MockBean
    private UserService userService;
    
    @MockBean
    private BankAccountService bankAccountService;
    
    @MockBean
    private CreditCardService creditCardService;

    @Mock
    private User mockUser;


    @Test
    public void testLoginSuccess() throws Exception {
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.of(mockUser));
        when(mockUser.getPassword()).thenReturn("1234");
        mockMvc.perform(formLogin("/login").user("newUser").password("1234"))
                .andExpect(status().isFound()) // Redirection status code (302)
                .andExpect(redirectedUrl("/dashboard"));
    }
    @Test
    public void testLoginFailure() throws Exception {
        mockMvc.perform(formLogin("/login").user("user").password("wrong"))
                .andExpect(status().isFound()) // Redirection status code (302)
                .andExpect(redirectedUrl("/login?error=Login+Fail.+Invalid+username+or+password.+Please+try+again."));
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        when(userService.registerUser("newUser2", "1234")).thenReturn(true);
        when(userService.getUserByUsername("newUser2")).thenReturn(mockUser);
        mockMvc.perform(post("/register").param("username", "newUser2").param("password", "1234").param("confirmPassword", "1234"))
                .andExpect(status().isFound()) // Redirection status code (302)
                .andExpect(redirectedUrl("/index"));
        
        verify(bankAccountService).createBankAccountForUser(mockUser);
        verify(creditCardService).createCreditCardForUser(mockUser,5000,5000,1);
    }

    @Test
    public void testRegisterFailureMismatchPasswords() throws Exception {
        mockMvc.perform(post("/register").param("username", "newUser").param("password", "1234").param("confirmPassword", "12345"))
                .andExpect(status().isFound()) // Redirection status code (302)
                .andExpect(redirectedUrl("/register"));
    }
//    test when user already exists
    @Test
    public void testRegisterFailureUserExists() throws Exception {
        when(userService.registerUser("newUser", "1234")).thenReturn(false);
        mockMvc.perform(post("/register").param("username", "newUser").param("password", "1234").param("confirmPassword", "1234"))
                .andExpect(status().isFound()) // Redirection status code (302)
                .andExpect(redirectedUrl("/register"));
    }
}
