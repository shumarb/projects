package com.fdmgroup.CreditCardProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fdmgroup.CreditCardProject.model.User;
import com.fdmgroup.CreditCardProject.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

	public boolean registerUser(String username, String password) {// throws EmailExistsException {
		if (userRepository.existsByUsername(username)) {
			return false;
		}
		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
		return true;
	}

	public User getUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
	}

	public void saveUser(User user) {
		userRepository.save(user);
	}
}
