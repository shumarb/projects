package com.fdmgroup.CreditCardProject.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.CreditCardProject.model.MccCategory;
import com.fdmgroup.CreditCardProject.model.MerchantCategoryCode;

public class MccImporter {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	String fileSeparator = FileSystems.getDefault().getSeparator();
	private static final Logger logger = LogManager.getLogger(MccImporter.class);

	public List<MerchantCategoryCode> loadMccCodes(String filePath) {
		String properPath = filePath.replace("/", fileSeparator);
		File file = new File(properPath);

		List<MccImportModel> input = new ArrayList<>();

		try {
			input = OBJECT_MAPPER.readValue(file, new TypeReference<>() {
			});
		} catch (DatabindException dbe) {

			logger.error("Unable to parse JSON file data at path " + properPath + " to List<MccImportModel>");
			return null;
		} catch (IOException ioe) {
			logger.error("Unable to read JSON file at path " + properPath);
			return null;
		}

		if (input.isEmpty()) {
			logger.warn("Parsed FX JSON file at path " + properPath + " as empty");
		}

		logger.info("Successfully parsed JSON file data at path " + properPath + ": " + input.size() + " items");

		List<MerchantCategoryCode> output = new ArrayList<>();

		for (MccImportModel mcc : input) {
			MerchantCategoryCode newMcc = new MerchantCategoryCode();
			newMcc.setCategoryName(mcc.getFullDescription());
			newMcc.setCategoryCode(Long.parseLong(mcc.getMcc()));
			newMcc.setCategory(MccCategory.valueOfLabel(mcc.getGroup().getDescription()));
			output.add(newMcc);
		}

		return output;
	}

}
