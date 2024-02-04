package edu.hawaii.ti.iam.groupings.selenium.page;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static edu.hawaii.ti.iam.groupings.selenium.page.ReceiveEmail.receiveEmail;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeborne.selenide.WebDriverRunner;

import edu.hawaii.ti.iam.groupings.selenium.core.Property;
import edu.hawaii.ti.iam.groupings.selenium.core.User;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest
public class FeedbackTest extends AbstractTestBase {

    private WebDriver driver;

    // Constructor.
    public FeedbackTest(@Autowired Property property) {
        super(property);
    }

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
        open(property.value("app.url.login"));
        driver = WebDriverRunner.getWebDriver();

        User user = new User.Builder()
                .username(property.value("student.user.username"))
                .password(property.value("student.user.password"))
                .firstname(property.value("student.user.firstname"))
                .uhnumber(property.value("student.user.uhnumber"))
                .build();
        assertThat(user.getUsername(), not(equalTo("SET-IN-OVERRIDES")));

        loginWith(driver, user);

        open(property.value("url.feedback"));
        $x("//*[@id=\"overlay\"]/div/div").shouldBe(disappear, Duration.ofSeconds(80));
    }

    @AfterEach
    public void afterEach() {
        driver.close();
    }

    @Test
    public void feedbackGeneral() {
        String content = "General feedback test";
        String subject = "general";
        $("option[value=general]").shouldBe(selected);
        String email = $(by("id", "input-email")).getAttribute("value");
        assertEquals(property.value("student.user.username") + "@hawaii.edu", email);
        $(by("id", "input-name")).val(property.value("student.user.username"));
        $(by("id", "input-feedback")).val(content);
        $(byText("Submit")).click();
        $(byText("Your feedback has successfully been submitted.")).should(exist);
        receiveEmail(property.value("mail.host"), property.value("mail.store.type"),
                property.value("admin.user.username") + "@hawaii.edu", property.value("admin.user.password"), subject,
                content, false);
    }

    @Test
    public void feedbackProblem() {
        String content = "Problem feedback test";
        String subject = "problem";
        $(by("id", "input-type")).click();
        $("option[value=problem]").click();
        String email = $(by("id", "input-email")).getAttribute("value");
        assertEquals(property.value("student.user.username") + "@hawaii.edu", email);
        $(by("id", "input-name")).val(property.value("student.user.username"));
        $(by("id", "input-feedback")).val(content);
        $(byText("Submit")).click();
        $(byText("Your feedback has successfully been submitted.")).should(exist);
        receiveEmail(property.value("mail.host"), property.value("mail.store.type"),
                property.value("admin.user.username") + "@hawaii.edu", property.value("admin.user.password"), subject,
                content, false);
    }

    @Test
    public void feedbackFeature() {
        String content = "Feature feedback test";
        String subject = "feature";
        $(by("id", "input-type")).click();
        $("option[value=feature]").click();
        String email = $(by("id", "input-email")).getAttribute("value");
        assertEquals(property.value("student.user.username") + "@hawaii.edu", email);
        $(by("id", "input-name")).val(property.value("student.user.username"));
        $(by("id", "input-feedback")).val(content);
        $(byText("Submit")).click();
        $(byText("Your feedback has successfully been submitted.")).should(exist);
        receiveEmail(property.value("mail.host"), property.value("mail.store.type"),
                property.value("admin.user.username") + "@hawaii.edu", property.value("admin.user.password"), subject,
                content,false);
    }

    @Test
    public void feedbackQuestion() {
        String content = "Question feedback test";
        String subject = "question";
        $(by("id", "input-type")).click();
        $("option[value=question]").click();
        String email = $(by("id", "input-email")).getAttribute("value");
        assertEquals(property.value("student.user.username") + "@hawaii.edu", email);
        $(by("id", "input-name")).val(property.value("student.user.username"));
        $(by("id", "input-feedback")).val(content);
        $(byText("Submit")).click();
        $(byText("Your feedback has successfully been submitted.")).should(exist);
        receiveEmail(property.value("mail.host"), property.value("mail.store.type"),
                property.value("admin.user.username") + "@hawaii.edu", property.value("admin.user.password"), subject,
                content, false);
    }

}
