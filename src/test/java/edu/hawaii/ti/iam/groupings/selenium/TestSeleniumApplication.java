package edu.hawaii.ti.iam.groupings.selenium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestSeleniumApplication {

	public static void main(String[] args) {
		SpringApplication.from(SeleniumApplication::main).with(TestSeleniumApplication.class).run(args);
	}

}
