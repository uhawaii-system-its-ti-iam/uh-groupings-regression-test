package edu.hawaii.ti.iam.groupings.selenium.page;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.qameta.allure.selenide.AllureSelenide;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.logevents.SelenideLogger;

import edu.hawaii.ti.iam.groupings.selenium.core.Property;
import edu.hawaii.ti.iam.groupings.selenium.core.User;

@SpringBootTest
public class MainTest {

    private static final Log logger = LogFactory.getLog(MainTest.class);

    private static User admin;
    private static User user;

    @Value("${app.url}")
    private String appUrl;

    @Value("${cas.login.url}")
    private String loginUrl;

    @Autowired
    private Property property;

    @BeforeAll
    public static void beforeAll() {
        Configuration.fileDownload = FileDownloadMode.FOLDER;
        Configuration.downloadsFolder = System.getProperty("user.dir") + File.separator + "downloadFile";
        Configuration.browserSize = "1280x800";
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void afterAll() {
        closeWebDriver();
    }

    @BeforeEach
    public void setUp() {
        admin = new User.Builder()
                .username(property.value("admin.user.username"))
                .password(property.value("admin.user.password"))
                .firstname(property.value("admin.user.firstname"))
                .uhnumber(property.value("admin.user.uhnumber"))
                .create();

        open(appUrl);
    }

    @Test
    public void navBarInfo() {
        $x("//a[@href='/uhgroupings/info']").click();
        $x("/html/body/main/div[1]/div/div[1]/div[1]/h1").shouldHave(text("What is a UH Grouping?"));
        webdriver().shouldHave(url(appUrl + "info"));
    }

    @Disabled
    @Test
    public void learnMoreButton() {
        $x("//a[@role='button'][@href='https://uhawaii.atlassian.net/wiki/spaces/UHIAM/pages/13403213/UH+Groupings']").click();
        webdriver().shouldHave(url("https://uhawaii.atlassian.net/wiki/spaces/UHIAM/pages/13403213/UH+Groupings"));
    }

    @Disabled
    @Test
    public void navBarLogin() {
        $x("//a[@href='/uhgroupings/login']").click();
        $("#uh-seal").shouldBe(visible);
        webdriver().shouldHave(url(loginUrl + "?service=" + encodeUrl(appUrl)));
    }

    @Disabled
    @Test
    public void loginHereButton() {
        $x("//button[@class='btn btn-lg dark-teal-bg'][text()='Login Here']").click();

    }

    @Disabled
    @Test
    public void groupingsIcon() {
        $x("//img[@alt='UH Groupings Logo']").click();
        webdriver().shouldHave(url(appUrl));
        $x("//p[@class='lead'][text()='Manage your groupings in one place, use them in many.']").should(exist);
    }

    @Disabled
    @Test
    public void equalOpportunity() {
        $x("//a[text()='equal opportunity/affirmative action institution']").click();
        webdriver().shouldHave(url("https://www.hawaii.edu/offices/eeo/policies/?policy=antidisc"));
    }

    @Disabled
    @Test
    public void loggingIn() {
        navBarLogin();
        $("#username").val(admin.getUsername());
        $("#password").val(admin.getPassword());
        $x("//button[@name='submit']").click();
        switchTo().frame($("#duo_iframe"));
        $("#username").should(disappear);
        $("button").should(appear);
        $x("//button[text()='Send Me a Push ']").click();
        $x("//h2[text()='Choose an authentication method']").should(disappear);
        $x("//span[text()='treyyy']").shouldBe(visible);
        closeWebDriver();
    }

    @Disabled
    @Test
    public void usagePolicyTest() {
        try {
            File downloadedFile = $("#footer-top > div > div > div.col-md-8.col-sm-7 > p:nth-child(2) > a").download();
            String path = downloadedFile.getPath();
            InputStream downloadedPDF = Files.newInputStream(Paths.get(path));
            String downloadedHash = DigestUtils.sha1Hex(downloadedPDF);
            InputStream expectedPDF = Files.newInputStream(Paths.get(path));
            String expectedHash = DigestUtils.sha1Hex(expectedPDF);
            assertEquals(expectedHash, downloadedHash);
            FileUtils.deleteDirectory(new File(System.getProperty("user.dir") + File.separator + "downloadFile"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String encodeUrl(String value) {
        String result = null;
        try {
            result = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            logger.warn("Error encoding url; " + e);
        }
        return result;
    }
}
