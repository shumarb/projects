package com.fdmgroup.CreditCardProject.io;

public class MccImportModel {
	private String mcc;
	private MccGroup group;
	private String shortDescription;
	private String fullDescription;

	public MccImportModel() {
		super();
	}

	public MccImportModel(String mcc, MccGroup group, String shortDescription, String fullDescription) {
		super();
		this.mcc = mcc;
		this.group = group;
		this.shortDescription = shortDescription;
		this.fullDescription = fullDescription;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public MccGroup getGroup() {
		return group;
	}

	public void setGroup(MccGroup group) {
		this.group = group;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}
}
