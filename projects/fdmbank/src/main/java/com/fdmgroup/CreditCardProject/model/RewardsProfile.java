package com.fdmgroup.CreditCardProject.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a rewards profile associated with a credit card. It contains
 * information about the conversion percentage and the list of valid merchant
 * category codes for rewards.
 * 
 * @author Danny
 * @version 1.0
 * @since 2024-02-26
 */

@Entity
@Table(name = "`Reward Profile`")
public class RewardsProfile {

	@Id
	@Column(name = "rewardProfileId", unique = true)
	private Long rewardProfileId;

	@Column(name = "cardName")
	private String name;

	@Column(name = "conversionPercentage")
	private double conversionPercentage;

	@Column(name = "description")
	private String description;
	
	@ElementCollection
	@Convert(converter = MccCategoryConverter.class)
	private List<MccCategory> validCategories;

	public RewardsProfile() {
	}

	public RewardsProfile(long rewardProfileId, String name) {
		this.rewardProfileId = rewardProfileId;
		this.name = name;

	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public double getConversionPercentage() {
		return conversionPercentage;
	}

	public void setConversionPercentage(double conversionPercentage) {
		this.conversionPercentage = conversionPercentage;
	}

	public List<MccCategory> getValidCategories() {
		return validCategories;
	}

	public void setValidCategories(List<MccCategory> validCategories) {
		this.validCategories = validCategories;
	}

	public long getRewardProfileId() {
		return rewardProfileId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
