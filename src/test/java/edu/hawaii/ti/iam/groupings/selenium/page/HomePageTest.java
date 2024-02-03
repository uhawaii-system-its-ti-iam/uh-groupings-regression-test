package edu.hawaii.ti.iam.groupings.selenium.page;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;
import static edu.hawaii.ti.iam.groupings.selenium.core.Util.encodeUrl;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeborne.selenide.WebDriverRunner;

@SpringBootTest
public class HomePageTest extends AbstractTestBase {

    private WebDriver driver;

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        WebDriverRunner.setWebDriver(new ChromeDriver());
    }

    @AfterAll
    public static void afterAll() {
        closeWebDriver();
    }

    @BeforeEach
    public void setUp() {
        open(property.value("app.url.home"));
        driver = WebDriverRunner.getWebDriver();
    }

    @AfterEach
    public void afterEach() {
        driver.close();
    }

    @Test
    public void navBarAbout() {
        final String urlFull = property.value("url.about");
        final String urlRel = property.value("url.relative.about");
        $x("//a[@href='/" + urlRel + "']").click();
        $x("/html/body/main/div[1]/div/div[1]/div[1]/h1").shouldHave(text("What is a UH Grouping?"));
        webdriver().shouldHave(url(urlFull));
    }

    @Test
    public void learnMoreButton() {
        String url = property.value("url.bwiki");
        $x("//a[@role='button'][@href='" + url + "']").click();
        webdriver().shouldHave(url("" + url + ""));
    }

    @Test
    public void clickNavBarLogin() {
        String urlRel = property.value("url.relative.login");
        $x("//a[@href='/" + urlRel + "']").click();
        $("#uh-seal").shouldBe(visible);
        String loginUrl = property.value("cas.login.url");
        String appUrl = property.value("app.url.home");
        String serviceUrl = appUrl + "/login/cas"; // Why the extra ending?
        String expectedUrl = loginUrl + "?service=" + encodeUrl(serviceUrl);
        webdriver().shouldHave(url(expectedUrl));
    }

    public void clickLoginOnHomePage() {
        String urlRel = property.value("url.relative.login");
        $x("//a[@href='/" + urlRel + "']").click();
        $("#uh-seal").shouldBe(visible);
    }

    @Test
    public void loginHereButton() {
        $x("//button[@class='btn btn-lg dark-teal-bg'][text()='Login Here']").click();
    }

    @Test
    public void groupingsIcon() {
        $x("//img[@alt='UH Groupings Logo']").click();
        webdriver().shouldHave(url(property.value("app.url.home") + "/")); // Inconsistent ending use.
        $x("//p[@class='lead'][text()='Manage your groupings in one place, use them in many.']").should(exist);
    }

    @Test
    public void equalOpportunity() {
        $x("//a[text()='equal opportunity/affirmative action institution']").click();
        webdriver().shouldHave(url(property.value("url.policy")));
    }

    @Test
    public void loggingInWithStudent() {
        clickLoginOnHomePage();
        loginWith(driver, createUser("student"));
    }

    @Test
    public void usagePolicyTest() throws Exception {
        File downloadedFile = $("a[href*='ep2.210.pdf']").download();
        Path path = Paths.get(downloadedFile.getPath());
        InputStream downloadedPDF = Files.newInputStream(path);
        String downloadedHash = DigestUtils.sha1Hex(downloadedPDF);
        InputStream expectedPDF = Files.newInputStream(path);
        String expectedHash = DigestUtils.sha1Hex(expectedPDF);
        assertThat(expectedHash, equalTo(downloadedHash));

        WebElement field = driver.findElement(By.linkText("Usage Policy"));
        assertThat(field.getText(), equalTo("Usage Policy"));
        assertThat(field.getAttribute("href"), endsWith("ep2.210.pdf"));
    }

}
