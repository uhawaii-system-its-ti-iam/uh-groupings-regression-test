package SeleniumTest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MembershipsPageMembershipOpportunitiesTabTest {
    static MainPage user = new MainPage();
    // WebDriver driver = new ChromeDriver();
//     Configuration config = new Configuration();
    @BeforeAll
    public static void setUpAll() {
        try {
            user.getUserCredentials();
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
        open(user.baseURL + "memberships");
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
//        $(byText("Membership opportunities")).click();
        $x("//*[@id=\"memberTab\"]/li[2]/a").click();
    }
    @Test
    public void filterGroupings(){
        $x("//*[@id=\"membership-opportunities\"]/div[1]/div[2]/div/button").click();
        $x("//*[@id=\"membership-opportunities\"]/div[1]/div[2]/div/ul/li[3]/label").click();
        $x("//*[@id=\"optIn\"]").setValue("large");
        $x("//*[@id=\"membership-opportunities\"]/div[2]/div[1]/table/tbody/tr[1]").shouldBe(visible, user.timeout);
        $(byText("No groupings are currently available.")).should(disappear, user.timeout);
        $$x("//*[@id=\"membership-opportunities\"]/div[2]/div[1]/table/tbody/tr").asFixedIterable().forEach(row -> row.shouldHave(or("should have test" ,text("large"), value("large"))));
    }
    @Test
    public void optIn() {
        $x("//*[@id=\"optIn\"]").setValue("testiwta-many");
        $(byText("No groupings are currently available.")).should(disappear, user.timeout);
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
        $x("//*[@id=\"optIn\"]").setValue(user.username);
        $(byText("No groupings are currently available.")).should(disappear, user.timeout);
        $("#groupingOptInPath-testiwta-many > form > div > div > button").click();
        assertEquals(clipboard().getText(), "tmp:testiwta:testiwta-many");
    }
}
