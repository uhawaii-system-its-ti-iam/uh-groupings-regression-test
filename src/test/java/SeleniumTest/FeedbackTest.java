package SeleniumTest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static SeleniumTest.ReceiveEmail.receiveEmail;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FeedbackTest {
    static MainPage user = new MainPage();
    static MainPage admin = new MainPage();
    // WebDriver driver = new ChromeDriver();
//     Configuration config = new Configuration();
    @BeforeAll
    public static void setUpAll() {
        try {
            user.getUserCredentials();
            admin.getAdminCredentials();
        } catch(Exception e) {
            System.out.println("not found");
        }
        Configuration.browserSize = "1280x800";
        Configuration.headless = false;
        SelenideLogger.addListener("allure", new AllureSelenide());
        user.loggingInNoDuoAuth();
    }

    @AfterAll
    public static void close() {
        closeWebDriver();
    }

    @BeforeEach
    public void setUp() {
        open(user.baseURL + "feedback");
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
    }

    @Test
    public void feedbackGeneral() {
        String content = "General feedback test";
        String subject = "general";
        $("option[value=general]").shouldBe(selected);
        String email = $("input[id=input-email]").getAttribute("value");
        assertEquals(user.username + "@hawaii.edu", email);
        $("#input-name").setValue(user.username);
        $("textarea[id=input-feedback]").setValue(content);
        $(byText("Submit")).click();
        $(byText("Your feedback has successfully been submitted.")).should(exist);
        receiveEmail(admin.emailHost, admin.mailStoreType, admin.username + "@hawaii.edu", admin.password, subject, content);
    }
    @Test
    public void feedbackProblem() {
        String content = "Problem feedback test";
        String subject = "problem";
        $("select[id=input-type]").click();
        $("option[value=problem]").click();
        $("option[value=problem]").shouldBe(selected);
        String email = $("input[id=input-email]").getAttribute("value");
        assertEquals(user.username + "@hawaii.edu", email);
        $("textarea[id=input-feedback]").setValue(content);
        $(byText("Submit")).click();
        $(byText("Your feedback has successfully been submitted.")).should(exist);
        receiveEmail(admin.emailHost, admin.mailStoreType, admin.username + "@hawaii.edu", admin.password, subject, content);
    }
    @Test
    public void feedbackFeature() {
        String content = "Feature feedback test";
        String subject = "feature";
        $("select[id=input-type]").click();
        $("option[value=feature]").click();
        $("option[value=feature]").shouldBe(selected);
        String email = $("input[id=input-email]").getAttribute("value");
        assertEquals(user.username + "@hawaii.edu", email);
        $("#input-name").setValue(user.username);
        $("textarea[id=input-feedback]").setValue("Feature feedback test");
        $(byText("Submit")).click();
        $(byText("Your feedback has successfully been submitted.")).should(exist);
        receiveEmail(admin.emailHost, admin.mailStoreType, admin.username + "@hawaii.edu", admin.password, subject, content);
    }
    @Test
    public void feedbackQuestion() {
        String content = "Question feedback test";
        String subject = "question";
        $("select[id=input-type]").click();
        $("option[value=question]").click();
        $("option[value=question]").shouldBe(selected);
        String email = $("input[id=input-email]").getAttribute("value");
        assertEquals(user.username + "@hawaii.edu", email);
        $("#input-name").setValue(user.username);
        $("textarea[id=input-feedback]").setValue(content);
        $(byText("Submit")).click();
        $(byText("Your feedback has successfully been submitted.")).should(exist);
        receiveEmail(admin.emailHost, admin.mailStoreType, admin.username + "@hawaii.edu", admin.password, subject, content);
    }

}
