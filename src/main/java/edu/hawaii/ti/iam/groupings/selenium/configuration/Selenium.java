package edu.hawaii.ti.iam.groupings.selenium.configuration;

import jakarta.annotation.PostConstruct;

import com.codeborne.selenide.Configuration;

@org.springframework.context.annotation.Configuration
public class Selenium {

    @PostConstruct
    public void init() {
        Configuration.browserSize = "1280x800";
        Configuration.downloadsFolder = "target/downloads";
        Configuration.timeout = 10000; // Ten seconds.
    }

}
