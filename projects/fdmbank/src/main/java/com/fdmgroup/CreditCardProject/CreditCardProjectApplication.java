package com.fdmgroup.CreditCardProject;

import com.fdmgroup.CreditCardProject.config.InitialStartupConfig;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CreditCardProjectApplication {

	@Autowired
	private InitialStartupConfig initialStartupConfig;
	static final Logger log = LogManager.getLogger(CreditCardProjectApplication.class);	
	
	public static void main(String[] args) {
		System.setProperty("log4j.configurationFile","classpath:log4j2.xml");
		log.info("Booting Up Credit Card Project Application...");
		SpringApplication.run(CreditCardProjectApplication.class, args);

	}
	@PostConstruct
	public void init() {
		initialStartupConfig.init();
	}
}
