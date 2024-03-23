package com.fdmgroup.CreditCardProject.model;

import java.util.HashMap;
import java.util.Map;

public enum MccCategory {
	AGRICUTURAL_SERVICES("Agricultura Services"), CONTRACT_SERVICES("Contract services"),
	WHOLESALE("Wholesale suppliers and manufacturers"), AIRLINES("Airlines"), CAR_RENT("Car rent"),
	HOTELS("Hotels / Resorts"), UNCATEGORIZED("Not categorized"), TRANSPORTATION("Transportation Services"),
	UTILITY_SERVICES("Utility Services"), SERVICE_PROVIDER("Service provider"),
	RETAIL_OUTLETS("Retail Outlet Services"), VEHICLES("Cars and vehicles"), CLOTHING("Clothing stores"),
	MISC_STORES("Miscellaneous Stores"), MAIL_PHONE("Mail / Telephone Sales"), PERSONAL_SERVICES("Personal services"),
	BUSINESS_SERVICES("Business Services"), REPAIR_SERVICES("Repair services"),
	ENTERTAINMENT_SERVICES("Entertainment services"), PROFESSIONAL_SERVICES("Professional services"),
	MEMBER_ORGS("Membership оrganizations"), GOVT_SERVICES("Government Services");
//	FINANCIAL_SERVICES("Financial Services");

	public final String label;

	private MccCategory(String label) {
		this.label = label;
	}

	private static final Map<String, MccCategory> BY_LABEL = new HashMap<>();

	static {
		for (MccCategory e : values()) {
			BY_LABEL.put(e.label, e);
		}
	}

	public static MccCategory valueOfLabel(String label) {
		return BY_LABEL.get(label);
	}
}
