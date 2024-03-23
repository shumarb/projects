package com.fdmgroup.CreditCardProject.io;

public class MccGroup {
	private String type;
	private String description;

	public MccGroup(String type, String description) {
		super();
		this.type = type;
		this.description = description;
	}

	public MccGroup() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
