package com.fdmgroup.CreditCardProject.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.CreditCardProject.exception.InsufficientFundsException;
import com.fdmgroup.CreditCardProject.model.CreditCard;
import com.fdmgroup.CreditCardProject.model.CreditCardTransaction;
import com.fdmgroup.CreditCardProject.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class CreditCardTransactionService {

	@Autowired
	private CreditCardRepository creditCardRepository;

	@Transactional
	public void processTransaction(CreditCard creditCard, CreditCardTransaction transaction)
			throws InsufficientFundsException {
		// Check if the user has sufficient funds
		if (creditCard.getCurrentBalance().compareTo(transaction.getAmount()) < 0) {
			throw new InsufficientFundsException("Insufficient funds");
		}

		// Update the user's balance
		BigDecimal newBalance = creditCard.getCurrentBalance().add(transaction.getAmount());
		creditCard.setCurrentBalance(newBalance);

		// Save the transaction
		transaction.setCreditCard(creditCard);
		creditCard.addTransactionHistory(transaction);
		creditCardRepository.save(creditCard);
	}

	public double getCurrencyConversionRate(String originalCurrencyCode) throws IOException {
		String apiKey = "2133ee2fec9f9c16937537f5";
		String apiUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/"
				+ originalCurrencyCode;

		ObjectMapper objectMapper = new ObjectMapper();

		try {
			URL url = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuilder response = new StringBuilder();

				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();

				JsonNode jsonNode = objectMapper.readTree(response.toString());

				// Access the value
				return jsonNode.get("conversion_rates")
						.get("USD")
						.asDouble();
				// System.out.println(value);

			} else {
				throw new IOException("API request failed with response code: " + responseCode);
			}
		} catch (IOException e) {
			throw new IOException("Failed to retrieve conversion rate", e);
		}
	}
}