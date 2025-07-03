package edu.hawaii.ti.iam.groupings.selenium.page.safari;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeborne.selenide.WebDriverRunner;

import edu.hawaii.ti.iam.groupings.selenium.core.User;
import edu.hawaii.ti.iam.groupings.selenium.page.AbstractTestBase;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest
public class LoggedInDepartmentUserTestSafari extends AbstractTestBase {

    private WebDriver driver;
    private User user;

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.safaridriver().setup();

        SafariOptions options = new SafariOptions();


        WebDriverRunner.setWebDriver(new SafariDriver(options));
    }

    @AfterAll
    public static void afterAll() {
        closeWebDriver();
    }

    @AfterEach
    public void afterEach() {
        driver.close();
    }
    @Test
    public void reminderIcon(){
        open(property.value("app.url.login"));
        driver = WebDriverRunner.getWebDriver();
        user = createUser("department2");
        loginWith(driver, user);
        $x("//*[@id=\"overlay\"]/div/div").shouldBe(disappear, Duration.ofSeconds(30));

        driver.manage().window().maximize();

        Assertions.assertTrue($x("/html/body/div/nav/div/span/i").isDisplayed());
        $x("/html/body/div/nav/div/div/ul/li[1]/a").click();
        Assertions.assertTrue($x("/html/body/div/nav/div/span/i").isDisplayed());
        $x("/html/body/div/nav/div/div/ul/li[2]/a").click();
        Assertions.assertTrue($x("/html/body/div/nav/div/span/i").isDisplayed());
        $x("/html/body/div/nav/div/div/ul/li[3]/a").click();
        Assertions.assertTrue($x("/html/body/div/nav/div/span/i").isDisplayed());
        $x("/html/body/div/nav/div/div/ul/li[4]/a").click();
        Assertions.assertTrue($x("/html/body/div/nav/div/span/i").isDisplayed());
    }
//
    @Test
    public void wrongDepartmentAccount(){
        open(property.value("app.url.login"));
        driver = WebDriverRunner.getWebDriver();
        user = createUser("department");
        loginWith(driver, user);
        $x("//*[@id=\"overlay\"]/div/div").shouldBe(disappear, Duration.ofSeconds(30));

        $("body > div.container.error_page > p:nth-child(2) > span").shouldBe(visible);
        $("body > div.container.error_page > p:nth-child(3) > span").shouldBe(visible);
        $("#name").click();
        $("body > main > div.container.mt-5.mb-5 > div > div.col-sm-7.d-inline-flex.align-items-center > div > img").shouldBe(visible);
    }
}
