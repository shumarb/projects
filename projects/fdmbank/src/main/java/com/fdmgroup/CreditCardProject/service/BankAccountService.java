package com.fdmgroup.CreditCardProject.service;

import com.fdmgroup.CreditCardProject.exception.*;
import com.fdmgroup.CreditCardProject.model.*;

import com.fdmgroup.CreditCardProject.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class BankAccountService {

	@Autowired
	private BankAccountRepository bankAccountRepository;

	@Autowired
	private CreditCardRepository creditCardRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BankTransactionRepository bankTransactionRepository;
	
	private static final Logger log = LogManager.getLogger(BankAccountService.class);

	public void createBankAccountForUser(User user) {

		String bankNumber;
		do {
			bankNumber = generateAccountNumber();
		} while (!isAccountNumberUnique(bankNumber));

		BankAccount bankAccount = new BankAccount(user, bankNumber, BigDecimal.ZERO);
		user.getBankAccounts().add(bankAccount);
		log.info("Bank Account with bank account number {} created for {}",bankNumber,user.getUsername());
		bankAccountRepository.save(bankAccount);
	}

	@Transactional
	public long transferBetweenAccounts(String accountFromId, String accountToId, BigDecimal amount)
			throws BankAccountNotFoundException, InsufficientBalanceException, SelfReferenceException {
		BankAccount bankAccountFrom = bankAccountRepository.findByAccountNumber(accountFromId)
				.orElseThrow(BankAccountNotFoundException::new);

		BankAccount bankAccountTo = bankAccountRepository.findByAccountNumber(accountToId)
				.orElseThrow(BankAccountNotFoundException::new);

		if(bankAccountFrom.getAccountId() == bankAccountTo.getAccountId()) {
			// same account
			throw new SelfReferenceException();
		}

		if (bankAccountFrom.getCurrentBalance().compareTo(amount) < 0) {
			throw new InsufficientBalanceException();
		}

		BankTransaction transaction = bankTransactionRepository
				.save(new BankTransaction(bankAccountFrom.getAccountId(), amount, bankAccountTo.getAccountId()));
		
		BigDecimal newFromBalance = bankAccountFrom.getCurrentBalance().subtract(amount);
		bankAccountFrom.setCurrentBalance(newFromBalance);
		bankAccountFrom.addTransactionHistory(transaction);
		BigDecimal newToBalance = bankAccountTo.getCurrentBalance().add(amount);
		bankAccountTo.setCurrentBalance(newToBalance);
		bankAccountTo.addTransactionHistory(transaction);
		bankAccountRepository.saveAll(List.of(bankAccountFrom, bankAccountTo));
		return transaction.getTransactionId();

	}

	@Transactional
	public long depositToAccount(String accountId, BigDecimal amount) throws BankAccountNotFoundException {
		BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountId)
				.orElseThrow(BankAccountNotFoundException::new);

		BankTransaction transaction = bankTransactionRepository
				.save(new BankTransaction(amount, bankAccount.getAccountId()));

		BigDecimal currentBalance = bankAccount.getCurrentBalance();
		BigDecimal newBalance = amount.add(currentBalance);
		bankAccount.setCurrentBalance(newBalance);
		bankAccount.addTransactionHistory(transaction);
		bankAccountRepository.save(bankAccount);
		return transaction.getTransactionId();
	}

	@Transactional
	public long payBills(String accountId, BigDecimal amount, CreditCard card) throws BankAccountNotFoundException, InsufficientBalanceException, ExcessPaymentException, BelowMinimumPayment {
		BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountId)
				.orElseThrow(BankAccountNotFoundException::new);
	    // if the bank account does not have enough funds to pay the bill, throw new InsufficientFundsException("Insufficient funds")
		if (bankAccount.getCurrentBalance().compareTo(amount) < 0) {
			log.error("BankAccountServiceError: Insufficient funds to pay bills from {} to {}.",accountId,card.getAccountNumber());
			throw new InsufficientBalanceException();
		}

		BigDecimal currentTotal = card.getSpendingLimit().subtract(card.getCurrentBalance());
		if (currentTotal.compareTo(amount)<0){
			log.error("BankAccountServiceError: Excess payment to {}.",card.getAccountNumber());
			throw new ExcessPaymentException();
		}

		// If below minimum payment throw error
		BigDecimal minimumPayment = currentTotal.multiply(new BigDecimal("0.1").setScale(2, RoundingMode.DOWN));
		if (amount.compareTo(minimumPayment) < 0) {
			log.error("BankAccountServiceError: Payment to {} is below minimum payment.",card.getAccountNumber());
			throw new BelowMinimumPayment();
		}
		// processing the transaction between the bank account and the credit card
		BankTransaction transaction = bankTransactionRepository
				.save(new BankTransaction(bankAccount.getAccountId(), amount));
		transaction.setAccountToId(Long.parseLong(String.valueOf(card.getAccountNumber())));
		transaction.setAccountFromId(Long.parseLong(String.valueOf(bankAccount.getAccountNumber())));
		transaction.setCreditCard(card);


		// updating the bank account and credit card
		bankAccount.setCurrentBalance(bankAccount.getCurrentBalance().subtract(amount));
		bankAccount.addTransactionHistory(transaction);

		// bill to be charged according to 1st of each month
			// anything paid after 1st of the month will be charged to the next month


		// updating the credit card
		card.setCurrentBalance(card.getCurrentBalance().add(amount));
		card.addPaymentHistory(transaction);

		// saving the changes
		bankAccountRepository.save(bankAccount);
		creditCardRepository.save(card);

		log.info("BankAccountServiceSuccess: Bills paid from {} to {}.",accountId,card.getAccountNumber());
		log.info("BankAccountServiceSuccess: New balance of {} is {}.",accountId,bankAccount.getCurrentBalance());
		return transaction.getTransactionId();



	}

	@Transactional
	public long withdrawFromAccount(String accountId, BigDecimal amount) throws BankAccountNotFoundException, InsufficientBalanceException {
		BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountId)
				.orElseThrow(BankAccountNotFoundException::new);

		// check if account has enough funds to withdraw
		if (bankAccount.getCurrentBalance().compareTo(amount) < 0) {
			throw new InsufficientBalanceException();
		}
		BankTransaction transaction = bankTransactionRepository
				.save(new BankTransaction(bankAccount.getAccountId(), amount));

		BigDecimal currentBalance = bankAccount.getCurrentBalance();
		BigDecimal newBalance = currentBalance.subtract(amount);
		bankAccount.setCurrentBalance(newBalance);
		bankAccount.addTransactionHistory(transaction);
		bankAccountRepository.save(bankAccount);
		return transaction.getTransactionId();
	}

	public String getUsernameOfAccountByAccountNumber(String accountNumber) throws BankAccountNotFoundException {
		if (!isAccountNumberValid(accountNumber)) {
			throw new BankAccountNotFoundException();
		}

		BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountNumber).get();
		return bankAccount.getUser().getUsername();
	}

	private boolean isAccountNumberUnique(String accountNumber) {
		return bankAccountRepository.findByAccountNumber(accountNumber).isEmpty();
	}

	private String generateAccountNumber() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 16; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}

	/**
     * Retrieves the current balance of a bank account by its unique identifier.
     * @param bankAccountNumber The unique identifier of the bank account.
     * @return The current balance of the specified bank account, or 0.0 if the account does not exist.
     */
	public BigDecimal getAccountBalanceByBankAccountNumber(String bankAccountNumber) {
		Optional<BankAccount> bankAccount = bankAccountRepository.findByAccountNumber(bankAccountNumber);
		if (bankAccount.isPresent()) {
			log.info("BankAccountServiceSuccess: The current balance of {} was obtained from {}.",bankAccount.get().getCurrentBalance(),bankAccountNumber);
			return bankAccount.get().getCurrentBalance();
		}else {
			log.error("BankAccountServiceError: Could not obtain current balance of {} as it does not exist.",bankAccountNumber);
			return BigDecimal.ZERO;
		}
	}

	/**
	 * Retrieves a list of bank account IDs associated with the given username.
	 * @param username The username for which to fetch bank account IDs.
	 * @return A list of bank account IDs associated with the specified username.
	 */
	public List<Long> getBankAccountIdsByUsername(String username){
		List<Long> bankAccountIds = new ArrayList<>();
		Optional<User> userOptional = userRepository.findByUsername(username);
		if(userOptional.isPresent()) {
			for (BankAccount b:userOptional.get().getBankAccounts()) {
				bankAccountIds.add(b.getAccountId());
			}
		}
		return bankAccountIds;
	}
	
	/**
	 * Retrieves a bank account associated with the given bank account number.
	 * @param bankAccountNumber The bank account number for which to fetch bank account.
	 * @return A bank account associated with the specified bank account number.
	 */
	public BankAccount getBankAccountByBankAccountNumber(String bankAccountNumber){
		return bankAccountRepository.findByAccountNumber(bankAccountNumber).get();
	}

	public boolean isAccountNumberValid(String accountNumber) {
		return bankAccountRepository.findByAccountNumber(accountNumber).isPresent();
	}
	
	public String getBankAccountNumberbyAccountId(long accountId) {
		Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(accountId);
		if (bankAccountOptional.isPresent()) {
			return bankAccountRepository.findById(accountId).get().getAccountNumber();
		}else {
			return "-1";
		}
	}
}

