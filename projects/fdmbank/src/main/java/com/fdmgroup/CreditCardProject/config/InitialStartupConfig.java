package com.fdmgroup.CreditCardProject.config;

import com.fdmgroup.CreditCardProject.io.MccImporter;
import com.fdmgroup.CreditCardProject.model.MccCategory;
import com.fdmgroup.CreditCardProject.model.MerchantCategoryCode;
import com.fdmgroup.CreditCardProject.model.RewardItem;
import com.fdmgroup.CreditCardProject.model.RewardsProfile;
import com.fdmgroup.CreditCardProject.repository.MerchantCategoryCodeRepository;
import com.fdmgroup.CreditCardProject.repository.RewardItemRepository;
import com.fdmgroup.CreditCardProject.repository.RewardsProfileRepository;
import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialStartupConfig {

	@Autowired
	private RewardsProfileRepository rewardsProfileRepository;

	@Autowired
	private RewardItemRepository rewardItemRepository;

	@Autowired
	private MerchantCategoryCodeRepository merchantCategoryCodeRepository;

	private MccImporter mccImporter = new MccImporter();

	@PostConstruct
	public void init() {
		initializeRewardsProfiles();
		initializeMerchantCategoryCodes();
		initializeRewardItems();
	}

	private void initializeRewardsProfiles() {
		initializeRewardsProfile(1L, 7, "FDM REVOLUTION", List.of(MccCategory.MISC_STORES, MccCategory.TRANSPORTATION), "Transportation and Miscellaneous Stores");
		initializeRewardsProfile(2L, 10, "FDM EVOL",
				List.of(MccCategory.AIRLINES, MccCategory.CAR_RENT, MccCategory.HOTELS), "Airlines, Car Rent and Hotels");
		initializeRewardsProfile(3L, 12, "FDM SIGNATURE", List.of(MccCategory.AIRLINES,MccCategory.PERSONAL_SERVICES, MccCategory.BUSINESS_SERVICES ), "Airlines, Personal and Business Services");
		initializeRewardsProfile(4L, 15, "FDM LADY'S CARD", List.of(MccCategory.CLOTHING, MccCategory.RETAIL_OUTLETS ), "Clothing and Retail Outlet");
	}

	private void initializeRewardsProfile(Long id, double conversionPercentage, String name,
			List<MccCategory> categories, String description) {
		if (rewardsProfileRepository.findById(id).isEmpty()) {
			RewardsProfile rewardsProfile = new RewardsProfile(id, name);
			rewardsProfile.setConversionPercentage(conversionPercentage);
			if (categories != null) {
				rewardsProfile.setValidCategories(categories);
			}
			rewardsProfile.setDescription(description);
			rewardsProfileRepository.save(rewardsProfile);
		}
	}

	private void initializeMerchantCategoryCodes() {
		List<MerchantCategoryCode> existing = merchantCategoryCodeRepository.findAll();
		if (existing.isEmpty()) {
			importMerchantCategoryCodes("src/main/resources/mcc_data.json");
		}
	}

	private void importMerchantCategoryCodes(String filePath) {
		List<MerchantCategoryCode> imported = mccImporter.loadMccCodes(filePath);
		merchantCategoryCodeRepository.saveAll(imported);
	}

	private void initializeRewardItems() {
		initializeRewardItem("Wireless Bluetooth Headphones",
				"Enjoy high-quality audio with these wireless Bluetooth headphones. They feature noise cancellation technology and a long battery life for an immersive listening experience.",
				100, "https://m.media-amazon.com/images/I/51+RQaYsHkL._AC_UF894,1000_QL80_.jpg");
		initializeRewardItem("Smart Fitness Tracker",
				"Stay on top of your health and fitness goals with this smart fitness tracker. It tracks your steps, heart rate, sleep patterns, and offers personalized workout recommendations.",
				80, "https://m.media-amazon.com/images/I/61q-MAfPbfL._AC_SX679_.jpg");
		initializeRewardItem("Pressure Cooker",
				"Cook delicious meals in a fraction of the time with this versatile pressure cooker. It offers multiple cooking functions, including slow cooking, sautéing, and steaming.",
				120, "https://m.media-amazon.com/images/I/71LmN7FsaZL.__AC_SX300_SY300_QL70_FMwebp_.jpg");
		initializeRewardItem("Coffee Machine",
				"Enjoy barista-quality coffee at home with this coffee machine. It uses a unique brewing technology to deliver rich, flavorful coffee with a silky finish.",
				150, "https://m.media-amazon.com/images/I/61NUqSDpwiL._AC_SX679_.jpg");
		initializeRewardItem("Luggage Set",
				"Travel in style with this durable and spacious Samsonite luggage set. It includes multiple suitcases of different sizes, featuring secure locks and smooth-rolling wheels.",
				250, "https://m.media-amazon.com/images/I/914pn9kFiUL.__AC_SX300_SY300_QL70_FMwebp_.jpg");
		initializeRewardItem("Travel Neck Pillow",
				"Ensure a comfortable journey with this travel neck pillow. It provides excellent neck and head support, making long trips more relaxing.",
				10, "https://m.media-amazon.com/images/I/713a7pJ0u3L.__AC_SX300_SY300_QL70_FMwebp_.jpg");
		initializeRewardItem("Cooking Utensil Set",
				"Upgrade your kitchen with this versatile cooking utensil set. It includes essential tools like spatulas, spoons, and tongs for your culinary adventures.",
				25, "https://m.media-amazon.com/images/I/61SIeOHTpoL.__AC_SY300_SX300_QL70_FMwebp_.jpg");
		initializeRewardItem("Gourmet Chocolate Gift Box",
				"Indulge your sweet tooth with a gourmet chocolate gift box. It includes a variety of delicious chocolates and makes for a delightful treat or gift.",
				15, "https://m.media-amazon.com/images/I/71TFpYd19ML._SX679_.jpg");

	}

	private void initializeRewardItem(String name, String description, int cost, String imageUrl) {
		Optional<RewardItem> itemOpt = rewardItemRepository.findByName(name);
		RewardItem item;
		if (itemOpt.isPresent()) {
			item = itemOpt.get();
			item.setDescription(description);
			item.setCost(cost);
			item.setImageUrl(imageUrl);
		} else {
			item = new RewardItem(name, description, cost, imageUrl);
		}
		rewardItemRepository.save(item);
	}
}