package com.fdmgroup.CreditCardProject.model;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MccCategoryConverter implements AttributeConverter<MccCategory, String> {

	@Override
	public String convertToDatabaseColumn(MccCategory category) {
		if (category == null) {
			return null;
		}
		return category.label;
	}

	@Override
	public MccCategory convertToEntityAttribute(String label) {
		if (label == null) {
			return null;
		}

		return Stream.of(MccCategory.values()).filter(x -> x.label.equals(label)).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
