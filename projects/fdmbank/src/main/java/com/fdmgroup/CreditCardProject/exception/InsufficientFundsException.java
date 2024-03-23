package com.fdmgroup.CreditCardProject.exception;

public class InsufficientFundsException extends Exception {

	private static final long serialVersionUID = 9185110164037684416L;

	public InsufficientFundsException(String message) {
        super(message);
    }
}