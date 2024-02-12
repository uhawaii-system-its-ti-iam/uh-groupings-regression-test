package edu.hawaii.ti.iam.groupings.selenium.page;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.time.Duration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeborne.selenide.WebDriverRunner;

import edu.hawaii.ti.iam.groupings.selenium.core.User;

@SpringBootTest
public class MembershipsPageCurrentMembershipsTabTest extends AbstractTestBase {

    private WebDriver driver;
    private User user;

    public String getClipboardContent() {
        String clipboardContent = null;
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboardContent = (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            logger.error("Failed", e);
        }

        return clipboardContent;
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

        user = createUser("student");
        loginWith(driver, user);

        open(property.value("url.memberships"));
        $(by("id", "overlay")).shouldBe(disappear, Duration.ofSeconds(80));
    }

    @AfterEach
    public void afterEach() {
        driver.close();
    }

    @Test
    public void filterGroupings() {
        $x("//*[@id=\"current-memberships\"]/div[1]/div[2]/input").setValue(user.username());
        $$x("//*[@id=\"current-memberships\"]/div[2]/div[1]/table/tbody/tr").asFixedIterable().forEach(row -> row.shouldHave(text(user.username())));
    }

    @Test
    public void optOut() {
        $(byText("acer-cc-ics")).parent().find(byClassName("opt-button")).click();
        $$x("//*[@id=\"current-memberships\"]/div[2]/div[1]/table/tbody/tr").asFixedIterable().forEach(row -> row.shouldNotHave(exactText("acer-cc-ics")));
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, Duration.ofSeconds(80));
        $(byText("Membership Opportunities")).click();
        $(by("id", "optIn")).setValue("acer-cc-ics");
        $(byText("acer-cc-ics")).shouldBe(visible, Duration.ofSeconds(80));
        $("#membership-opportunities > div.ng-scope > div.table-responsive > table > tbody > tr > td.w-8.align-middle.text-center > button").click();
    }

    @Test
    public void copyPathToClipboard() {
        $x("//*[@id=\"current-memberships\"]/div[1]/div[2]/div/button").click();
        $x("//*[@id=\"current-memberships\"]/div[1]/div[2]/div/ul/li[3]/label/input").click();
        $(byText("Show All")).click();
        $x("//*[@id=\"current-memberships\"]/div[1]/div[2]/input").setValue("acer-cc-ics");
        $("#current-memberships > div:nth-child(2) > div:nth-child(1) > table:nth-child(1) > tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(3) > form:nth-child(1) > div:nth-child(2) > div:nth-child(1) > button:nth-child(1) > i:nth-child(1)").click();
        assertThat(getClipboardContent(), equalTo("hawaii.edu:custom:test:julio:acer-cc-ics"));
    }

    @Test
    public void tableSettings() {
        $("#current-memberships > div.row.m-auto.pt-2.pb-2 > div.col-lg-3.col-md-4.col-12.p-0.pt-3.d-sm-flex > div > button").click();
        $("#current-memberships > div.row.m-auto.pt-2.pb-2 > div.col-lg-3.col-md-4.col-12.p-0.pt-3.d-sm-flex > div > ul").shouldBe(visible);
        $("#current-memberships > div.row.m-auto.pt-2.pb-2 > div.col-lg-3.col-md-4.col-12.p-0.pt-3.d-sm-flex > div > ul > li:nth-child(2) > label").click();
        $("#current-memberships > div.ng-scope > div.table-responsive > table > thead > tr > th:nth-child(2)").shouldNotBe(visible);
        $("#current-memberships > div.ng-scope > div.table-responsive > table > thead > tr > th:nth-child(3)").shouldBe(visible);
        $("#current-memberships > div.row.m-auto.pt-2.pb-2 > div.col-lg-3.col-md-4.col-12.p-0.pt-3.d-sm-flex > div > button").click();
        $("#current-memberships > div.row.m-auto.pt-2.pb-2 > div.col-lg-3.col-md-4.col-12.p-0.pt-3.d-sm-flex > div > ul > li:nth-child(3) > label").click();
        $("#current-memberships > div.ng-scope > div.table-responsive > table > thead > tr > th:nth-child(2)").shouldBe(visible);
        $("#current-memberships > div.ng-scope > div.table-responsive > table > thead > tr > th:nth-child(3)").shouldBe(visible);
        $("#current-memberships > div.row.m-auto.pt-2.pb-2 > div.col-lg-3.col-md-4.col-12.p-0.pt-3.d-sm-flex > div > button").click();
        $("#current-memberships > div.row.m-auto.pt-2.pb-2 > div.col-lg-3.col-md-4.col-12.p-0.pt-3.d-sm-flex > div > ul > li:nth-child(1) > label").click();
        $("#current-memberships > div.ng-scope > div.table-responsive > table > thead > tr > th:nth-child(3)").shouldNotBe(visible);
    }

}
