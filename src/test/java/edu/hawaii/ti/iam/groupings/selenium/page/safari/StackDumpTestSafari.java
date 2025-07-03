package edu.hawaii.ti.iam.groupings.selenium.page.safari;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeborne.selenide.WebDriverRunner;

import edu.hawaii.ti.iam.groupings.selenium.page.AbstractTestBase;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest
public class StackDumpTestSafari extends AbstractTestBase {

    private WebDriver driver;

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.safaridriver().setup();
        WebDriverRunner.setWebDriver(new SafariDriver());
    }

    @AfterAll
    public static void afterAll() {
        closeWebDriver();
    }

    @BeforeEach
    public void setUp() {
        open(property.value("app.url.login"));
        driver = WebDriverRunner.getWebDriver();
        loginWith(driver, createUser("admin"));
        open(property.value("url.admin"));
        $x("//*[@id=\"overlay\"]/div/div").shouldBe(disappear, Duration.ofSeconds(80));
    }

    @AfterEach
    public void afterEach() {
        driver.close();
    }

    @Test
    public void forTestingPurposes() {
        $(byText("For Testing Purposes")).click();
        $(byText("This page is meant for testing purposes and should only be visible on test and localhost environments.")).shouldBe(visible);
        $(byText("Throw Exception")).click();
        $(byText("Feedback")).click();
        webdriver().shouldHave(url(property.value("url.feedback")));
    }

}
