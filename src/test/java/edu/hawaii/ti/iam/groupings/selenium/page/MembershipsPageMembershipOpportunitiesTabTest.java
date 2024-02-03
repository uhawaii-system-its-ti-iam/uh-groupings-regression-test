package edu.hawaii.ti.iam.groupings.selenium.page;

import com.codeborne.selenide.WebDriverRunner;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Condition.or;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.time.Duration;

import edu.hawaii.ti.iam.groupings.selenium.core.Property;
import edu.hawaii.ti.iam.groupings.selenium.core.User;

@SpringBootTest
public class MembershipsPageMembershipOpportunitiesTabTest extends AbstractTestBase {
    private WebDriver driver;
    private User user;

    // Constructor
    public MembershipsPageMembershipOpportunitiesTabTest(@Autowired Property property) {
        super(property);
        this.createUser();
    }

    private void createUser() {
        this.user = new User.Builder()
                .username(property.value("student.user.username"))
                .password(property.value("student.user.password"))
                .firstname(property.value("student.user.firstname"))
                .uhnumber(property.value("student.user.uhnumber"))
                .build();
    }

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
        assertThat(user.getUsername(), not(equalTo("SET-IN-OVERRIDES")));

        loginWith(driver, user);

        open(property.value("url.memberships"));
        $(by("id", "overlay")).shouldBe(disappear, Duration.ofSeconds(80));
        $("#memberTab > li:nth-child(2) > a").click();
    }

    @AfterEach
    public void afterEach() {
        driver.close();
    }

    @Test
    public void filterGroupings(){
        $x("//*[@id=\"membership-opportunities\"]/div[1]/div[2]/div/button").click();
        $x("//*[@id=\"membership-opportunities\"]/div[1]/div[2]/div/ul/li[3]/label").click();
        $x("//*[@id=\"optIn\"]").setValue("large");
        $x("//*[@id=\"membership-opportunities\"]/div[2]/div[1]/table/tbody/tr[1]").shouldBe(visible, Duration.ofSeconds(80));
        $(byText("No groupings are currently available.")).should(disappear, Duration.ofSeconds(80));
        $$x("//*[@id=\"membership-opportunities\"]/div[2]/div[1]/table/tbody/tr").asFixedIterable().forEach(row -> row.shouldHave(or("should have test" ,text("large"), value("large"))));
    }
    @Test
    public void optIn() {
        $x("//*[@id=\"optIn\"]").setValue("testiwta-many");
        $(byText("No groupings are currently available.")).should(disappear, Duration.ofSeconds(80));
        $("#membership-opportunities > div.ng-scope > div.table-responsive > table > tbody > tr > td.w-8.align-middle.text-center > button").click();
        $("#memberTab > li:nth-child(1) > a").click();
        $("#current-memberships > div.row.m-auto.pt-2.pb-2 > div.col-lg-3.col-md-4.col-12.p-0.pt-3.d-sm-flex > input").setValue("testiwta-many");
        $x("//*[@id=\"membership-opportunities\"]/div[2]/div[1]/table/tbody/tr").shouldNotBe(visible);
        $x("//*[@id=\"memberTab\"]/li[1]/a").click();
        $x("//*[@id=\"current-memberships\"]/div[1]/div[2]/input").setValue("testiwta-many");
        $x("//*[@id=\"current-memberships\"]/div[2]/div[1]/table/tbody/tr/td/div/button").click();
        $x("//*[@id=\"overlay\"]").should(disappear);
        $$x("//*[@id=\"current-memberships\"]/div[2]/div[1]/table/tbody/tr").asFixedIterable().forEach(row -> row.shouldNotHave(text("testiwta-many")));
    }
    @Test
    public void copyPathToClipboard() {
        $("#membership-opportunities > div.row.m-auto.pt-2.pb-2 > div.col-lg-3.col-md-4.col-12.p-0.pt-3.d-sm-flex > div > button").click();
        $x("//*[@id=\"membership-opportunities\"]/div[1]/div[2]/div/ul/li[3]/label").click();
        $x("//*[@id=\"optIn\"]").setValue("testiwta");
        $(byText("No groupings are currently available.")).should(disappear, Duration.ofSeconds(80));
        $("#groupingOptInPath-testiwta-many > form > div > div > button").click();
        assertEquals(getClipboardContent(), "tmp:testiwta:testiwta-many");
    }
}
