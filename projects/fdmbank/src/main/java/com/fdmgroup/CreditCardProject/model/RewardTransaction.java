package com.fdmgroup.CreditCardProject.model;

import java.util.Date;

import jakarta.persistence.*;

/**
 * Represents a reward transaction. This
 * class is used to model transactions where reward points are used to purchase items.
 *
 * @author Danny
 * @version 1.0
 * @since 2022-02-20
 */

@Entity
@Table(name = "`RewardTransaction`")
public class RewardTransaction {

	/**
	 * The unique identifier for the transaction.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "transactionId")
	private long transactionId;

	/**
	 * The user who made the transaction.
	 */
	@ManyToOne
	private User user;

	/**
	 * The date of the transaction.
	 */
	@Column(name = "date")
	private Date date;

	/**
	 * The amount involved in the transaction.
	 */
	@Column(name = "amount")
	private int amount;

	/**
	 * The reward item that was purchased
	 */
	@ManyToOne
	private RewardItem rewardItem;

	/**
	 * Default constructor for RewardTransaction. Creates an instance with default
	 * values.
	 */
	public RewardTransaction() {
		super();
	}

	public RewardTransaction(User user, RewardItem rewardItem) {
		this.user = user;
		this.amount = rewardItem.getCost();
		this.rewardItem = rewardItem;
		this.date = new Date();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public RewardItem getRewardItem() {
		return rewardItem;
	}

	public void setRewardItem(RewardItem rewardItem) {
		this.rewardItem = rewardItem;
	}

	public long getTransactionId() {
		return transactionId;
	}
}
