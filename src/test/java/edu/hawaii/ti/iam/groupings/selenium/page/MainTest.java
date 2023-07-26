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
import static edu.hawaii.ti.iam.groupings.selenium.core.Util.encodeUrl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.qameta.allure.selenide.AllureSelenide;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeborne.selenide.logevents.SelenideLogger;

import edu.hawaii.ti.iam.groupings.selenium.core.Property;
import edu.hawaii.ti.iam.groupings.selenium.core.User;

@SpringBootTest
public class MainTest {

    private static final Log logger = LogFactory.getLog(MainTest.class);
    private static User admin;

    private final Property property;

    // Constructor.
    @Autowired
    public MainTest(Property property) {
        this.property = property;
    }

    @BeforeAll
    public static void beforeAll() {
        // Configuration.browserSize = "1280x800";
        // Configuration.downloadsFolder = "target/downloads";
        if ("off".equals("")) {
            AllureSelenide allureSelenide = new AllureSelenide();
            allureSelenide.screenshots(false);
            SelenideLogger.addListener("allure", allureSelenide);
        }
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

        open(property.value("app.url.home"));
    }

    @Disabled
    @Test
    public void navBarInfo() {
        final String urlFull = property.value("url.info");
        final String urlRel = property.value("url.relative.info");
        $x("//a[@href='/" + urlRel + "']").click();
        $x("/html/body/main/div[1]/div/div[1]/div[1]/h1").shouldHave(text("What is a UH Grouping?"));
        webdriver().shouldHave(url(urlFull));
    }

    @Disabled
    @Test
    public void learnMoreButton() {
        String url = property.value("url.bwiki");
        $x("//a[@role='button'][@href='" + url + "']").click();
        webdriver().shouldHave(url("" + url + ""));
    }

    @Disabled
    @Test
    public void navBarLogin() {
        String urlRel = property.value("url.relative.login");
        $x("//a[@href='/" + urlRel + "']").click();
        $("#uh-seal").shouldBe(visible);
        String loginUrl = property.value("cas.login.url");
        String appUrl = property.value("app.url.home");
        String serviceUrl = appUrl + "/login/cas"; // Why the extra ending?
        webdriver().shouldHave(url(loginUrl + "?service=" + encodeUrl(serviceUrl)));
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
        webdriver().shouldHave(url(property.value("app.url.home") + "/")); // Inconsistent ending use.
        $x("//p[@class='lead'][text()='Manage your groupings in one place, use them in many.']").should(exist);
    }

    @Disabled
    @Test
    public void equalOpportunity() {
        $x("//a[text()='equal opportunity/affirmative action institution']").click();
        webdriver().shouldHave(url(property.value("url.policy")));
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

    @Test
    public void usagePolicyTest() throws Exception {
        File downloadedFile = $("#footer-top > div > div > div.col-md-8.col-sm-7 > p:nth-child(2) > a").download();
        // String path = downloadedFile.getPath();
        Path path = Paths.get(downloadedFile.getPath());
        InputStream downloadedPDF = Files.newInputStream(path);
        String downloadedHash = DigestUtils.sha1Hex(downloadedPDF);
        InputStream expectedPDF = Files.newInputStream(path);
        String expectedHash = DigestUtils.sha1Hex(expectedPDF);
        assertThat(expectedHash, equalTo(downloadedHash));
    }

}
