package com.fdmgroup.CreditCardProject.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


/**
 * An abstract class representing a financial transaction.
 * This class serves as a base class for various types of financial transactions in the system.
 * @author Danny
 * @version 1.0
 * @since 2022-02-20
 */

public abstract class Transaction {
	/**
     * The unique identifier for the transaction.
     */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "transactionId")
	private long transactionId;
	
	/**
     * The identifier of the account from which the transaction is made.
     */
	@Column(name = "accountFromId")
	private long accountFromId;
	
	/**
     * The date of the transaction.
     */
	@Column(name = "date")
	private Date date;
	
	/**
    * The amount involved in the transaction.
    */
	@Column(name = "amount")
	private double amount;
	
	/**
     * Default constructor for Transaction.
     * Creates an instance with default values.
     */
	public Transaction() {
		super();
	}
	
	/**
     * Parameterized constructor for Transaction.
     * @param accountFromId The identifier of the account from which the transaction is made.
     * @param date          The date of the transaction.
     * @param amount        The amount involved in the transaction.
     */
	public Transaction(long accountFromId, Date date, double amount) {
		super();
		setAccountFromId(accountFromId);
		setDate(date);
		setAmount(amount);	
	}

	/**
     * Gets the unique identifier for the transaction.
     * @return The transactionId.
     */
	public long getTransactionId() {
		return transactionId;
	}

	/**
     * Gets the identifier of the account from which the transaction is made.
     * @return The accountFromId.
     */
	public long getAccountFromId() {
		return accountFromId;
	}

	/**
     * Sets the identifier of the account from which the transaction is made.
     * @param accountFromId The accountFromId to set.
     */
	public void setAccountFromId(long accountFromId) {
		this.accountFromId = accountFromId;
	}

	 /**
     * Gets the date of the transaction.
     * @return The date.
     */
	public Date getDate() {
		return date;
	}

	/**
     * Sets the date of the transaction.
     * @param date The date to set.
     */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
     * Gets the amount involved in the transaction.
     * @return The amount.
     */
	public double getAmount() {
		return amount;
	}

	/**
     * Sets the amount involved in the transaction.
     * @param amount The amount to set.
     */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}
