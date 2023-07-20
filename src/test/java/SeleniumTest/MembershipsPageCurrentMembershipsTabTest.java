package SeleniumTest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MembershipsPageCurrentMembershipsTabTest {
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

    @BeforeEach
    public void setUp() {
        open(user.baseURL + "memberships");
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
    }
    @AfterAll
    public static void close() {
        closeWebDriver();
    }
    @Test
    public void filterGroupings(){
        $x("//*[@id=\"current-memberships\"]/div[1]/div[2]/input").setValue("test");
        $$x("//*[@id=\"current-memberships\"]/div[2]/div[1]/table/tbody/tr").asFixedIterable().forEach(row -> row.shouldHave(text("test")));
    }
    @Test
    public void optOut() {
        $(byText("JTTEST-L")).parent().find(byClassName("opt-button")).click();
        $$x("//*[@id=\"current-memberships\"]/div[2]/div[1]/table/tbody/tr").asFixedIterable().forEach(row -> row.shouldNotHave(exactText("JTTEST-L")));
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
        $(byText("Membership Opportunities")).click();
        $x("//*[@id=\"optIn\"]").setValue("JTTEST-L");
        $(byText("JTTEST-L")).shouldBe(visible, user.timeout);
        $("#membership-opportunities > div.ng-scope > div.table-responsive > table > tbody > tr > td.w-8.align-middle.text-center > button").click();
    }
    @Test
    public void copyPathToClipboard() {
        $x("//*[@id=\"current-memberships\"]/div[1]/div[2]/div/button").click();
        $x("//*[@id=\"current-memberships\"]/div[1]/div[2]/div/ul/li[3]/label/input").click();
        $(byText("Show All")).click();
        $x("//*[@id=\"current-memberships\"]/div[1]/div[2]/input").setValue(user.username);
        $("#current-memberships > div.ng-scope > div.table-responsive > table > tbody > tr:nth-child(2) > td:nth-child(3) > form > div > div > button").click();
       assertEquals(clipboard().getText(), "tmp:testiwta:testiwta-single");
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
