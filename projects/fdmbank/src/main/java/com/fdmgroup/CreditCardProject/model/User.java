package com.fdmgroup.CreditCardProject.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "`User`")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userId")
	private long userId;

	@Column(name = "username", unique = true)
	private String username;

	@Column(name = "password")
	private String password;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@Column(name = "bankAccount")
	private List<BankAccount> bankAccounts = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<CreditCard> creditCards = new ArrayList<>();

	@Column(name = "rewardsPoints")
	private int rewardsPoints;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<RewardTransaction> rewardTransactions = new ArrayList<>();

	public List<CreditCard> getCreditCards() {
		return creditCards;
	}

	public void setCreditCards(List<CreditCard> creditCards) {
		this.creditCards = creditCards;
	}

	public User() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<BankAccount> getBankAccounts() {
		return bankAccounts;
	}

	public void setBankAccounts(List<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getRewardsPoints() {
		return rewardsPoints;
	}

	public void setRewardsPoints(int rewardsPoints) {
		this.rewardsPoints = rewardsPoints;
	}

	public List<RewardTransaction> getRewardTransactions() {
		return rewardTransactions;
	}

	public void setRewardTransactions(List<RewardTransaction> rewardTransactions) {
		this.rewardTransactions = rewardTransactions;
	}

	public void addRewardTransaction(RewardTransaction rewardTransaction) {
		rewardTransactions.add(rewardTransaction);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, password, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return userId == other.userId && Objects.equals(password, other.password)
				&& Objects.equals(username, other.username);
	}

}
