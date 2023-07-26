package edu.hawaii.ti.iam.groupings.selenium.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Selenium {

    @PostConstruct
    public void init() {
        com.codeborne.selenide.Configuration.browserSize = "1280x800";
        com.codeborne.selenide.Configuration.downloadsFolder = "target/downloads";
    }

}
