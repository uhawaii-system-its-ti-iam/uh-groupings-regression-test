package edu.hawaii.ti.iam.groupings.selenium.page.safari;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

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
public class FeedbackTestSafari extends AbstractTestBase {

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

        loginWith(driver, createUser("student"));

        open(property.value("url.feedback"));
        $(by("id", "overlay")).shouldBe(disappear, Duration.ofSeconds(80));
    }

    @AfterEach
    public void afterEach() {
        driver.close();
    }

    @Test
    public void feedbackGeneral() throws InterruptedException {
        String content = "General feedback test";
        String subject = "general";
        $("option[value=general]").shouldBe(selected);
        $(by("id", "input-name")).val(property.value("student.user.username"));
        String email = $(by("id", "input-email")).getAttribute("value");
        //        Thread.sleep(10000);
        //        assertEquals(property.value("student.user.username") + "@hawaii.edu", email);
        $("#input-email").setValue("testiwta@hawaii.edu");
        $(by("id", "input-feedback")).setValue(content);
        $(byText("Submit")).click();
        $(byText("Your feedback has successfully been submitted.")).should(exist);

    }

    @Test
    public void feedbackProblem() {
        String content = "Problem feedback test";
        String subject = "problem";
        $(by("id", "input-type")).click();
        $("option[value=problem]").click();
        $(by("id", "input-name")).val(property.value("student.user.username"));
        String email = $(by("id", "input-email")).getAttribute("value");
        //        assertEquals(property.value("student.user.username") + "@hawaii.edu", email);
        $("#input-email").setValue("testiwta@hawaii.edu");
        $(by("id", "input-feedback")).setValue(content);
        $(byText("Submit")).click();
        $(byText("Your feedback has successfully been submitted.")).should(exist);

    }

    @Test
    public void feedbackFeature() {
        String content = "Feature feedback test";
        String subject = "feature";
        $(by("id", "input-type")).click();
        $("option[value=feature]").click();
        $(by("id", "input-name")).val(property.value("student.user.username"));
        String email = $(by("id", "input-email")).getAttribute("value");
        //        assertEquals(property.value("student.user.username") + "@hawaii.edu", email);
        $("#input-email").setValue("testiwta@hawaii.edu");
        $(by("id", "input-feedback")).setValue(content);
        $(byText("Submit")).click();
        $(byText("Your feedback has successfully been submitted.")).should(exist);
    }

    @Test
    public void feedbackQuestion() {
        String content = "Question feedback test";
        String subject = "question";
        $(by("id", "input-type")).click();
        $("option[value=question]").click();
        $(by("id", "input-name")).val(property.value("student.user.username"));
        String email = $(by("id", "input-email")).getAttribute("value");
        //        assertEquals(property.value("student.user.username") + "@hawaii.edu", email);
        $("#input-email").setValue("testiwta@hawaii.edu");
        $(by("id", "input-feedback")).setValue(content);
        $(byText("Submit")).click();
        $(byText("Your feedback has successfully been submitted.")).should(exist);

    }
}
