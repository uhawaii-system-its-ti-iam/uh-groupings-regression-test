package edu.hawaii.ti.iam.groupings.selenium.configuration;

import jakarta.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import com.codeborne.selenide.Configuration;

@org.springframework.context.annotation.Configuration
@ConfigurationProperties(prefix = "selenium")
public class Selenium {

    private static final Log logger = LogFactory.getLog(Selenium.class);

    private String browserSize;
    private String downloadsFolder;
    private long timeout;

    @PostConstruct
    public void init() {
        logger.info("Property browserSize: " + browserSize);
        logger.info("Property downloadsFolder: " + downloadsFolder);
        logger.info("Property timeout: " + timeout);

        Assert.notNull(browserSize, "Property 'browserSize' not set.");
        Assert.notNull(downloadsFolder, "Property 'downloadsFolder' not set.");
        Assert.isTrue(timeout > 0, "Property 'timeout' not set.");

        Configuration.browserSize = "1280x800";
        Configuration.downloadsFolder = getDownloadsFolder();
        Configuration.timeout = getTimeout();
    }

    public String getBrowserSize() {
        return browserSize;
    }

    public void setBrowserSize(String browserSize) {
        this.browserSize = browserSize;
    }

    public String getDownloadsFolder() {
        return downloadsFolder;
    }

    public void setDownloadsFolder(String downloadsFolder) {
        this.downloadsFolder = downloadsFolder;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

}
