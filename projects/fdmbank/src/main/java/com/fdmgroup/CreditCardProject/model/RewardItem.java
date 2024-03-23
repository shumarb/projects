package com.fdmgroup.CreditCardProject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reward_item")
public class RewardItem {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long itemId;

	private String name;

	private String description;

	private int cost;

	private String imageUrl;

	public RewardItem() {
		super();
	}

	public RewardItem(String name, String description, int cost, String imageUrl) {
		super();
		this.name = name;
		this.description = description;
		this.cost = cost;
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public long getItemId() {
		return itemId;
	}

}
