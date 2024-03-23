package com.fdmgroup.CreditCardProject.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * An abstract class representing an account.
 * This class serves as a base class for various types of accounts in the system.
 * @author Danny
 * @version 1.0
 * @since 2022-02-20
 */

public abstract class Account {
	/**
     * The unique identifier for the account.
     */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "accountId")
	private long accountId;
	
	/**
     * The identifier of the owner of the account.
     */
	@Column(name = "ownerId")
	private long ownerId;
	
	/**
     * The account number associated with this account.
     */
	@Column(name = "accountNumber")
	private String accountNumber;
	
	/**
     * The current balance of the account.
     */
	@Column(name = "currentBalance")
	private double currentBalance;
	
	/**
     * Default constructor for Account.
     * Creates an instance with default values.
     */
	public Account() {
		super();
	}
	
	/**
     * Parameterized constructor for Account.
     * @param ownerId        The identifier of the owner of the account.
     * @param accountNumber  The account number associated with the account.
     * @param currentBalance The current balance of the account.
     */
	public Account(long ownerId, String accountNumber,double currentBalance) {
		setOwnerId(ownerId);
		setAccountNumber(accountNumber);
		setCurrentBalance(currentBalance);
	}

	/**
     * Gets the identifier of the owner of the account.
     * @return The ownerId.
     */
	public long getOwnerId() {
		return ownerId;
	}
	
	/**
     * Sets the identifier of the owner of the account.
     * @param ownerId The ownerId to set.
     */
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	/**
     * Gets the account number associated with this account.
     * @return The accountNumber.
     */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
     * Sets the account number associated with this account.
     * @param accountNumber The accountNumber to set.
     */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
     * Gets the current balance of the account.
     * @return The currentBalance.
     */
	public double getCurrentBalance() {
		return currentBalance;
	}

	/**
     * Sets the current balance of the account.
     * @param currentBalance The currentBalance to set.
     */
	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	/**
     * Gets the unique identifier for the account.
     * @return The accountId.
     */
	public long getAccountId() {
		return accountId;
	}
	
	
}
