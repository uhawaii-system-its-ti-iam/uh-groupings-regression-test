package SeleniumTest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.byText;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AdminPageManagePersonTabTest {
    static MainPage admin = new MainPage();
    static MainPage user = new MainPage();
    // WebDriver driver = new ChromeDriver();
    // Configuration config = new Configuration();
    @BeforeAll
    public static void setUpAll() {
        try {
            admin.getAdminCredentials();
            user.getUserCredentials();
        } catch(Exception e) {
            System.out.println("not found");
        }
        Configuration.browserSize = "1280x800";
        Configuration.holdBrowserOpen = false;
//        Configuration.browser = "firefox";
        SelenideLogger.addListener("allure", new AllureSelenide());
        admin.loggingIn();
    }

    @BeforeEach
    public void setUp() {
        open(admin.baseURL + "admin");
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, admin.timeout);
        $x("//*[@id=\"adminTab\"]/li[3]/a").click();
    }
    @AfterAll
    public static void close() {
        closeWebDriver();
    }

    @Test
    public void searchPerson() {
        $x("//input[contains(@name, 'Search Person')]").val(user.username);
        $x("//button[contains(text(), 'Search')]").click();
        $x("//div[contains(@class, 'spinner-border')]").should(disappear, admin.timeout);
        $x("//h3").shouldHave(text(user.firstName));
        $x("//h3").shouldHave(text(user.username));
        $x("//h3").shouldHave(text(user.uhNumber));
    }

    @Test
    public void filterGroupings() {
        searchPerson();
        $x("//*[@id=\"manage-person\"]/div[1]/div[2]/input").val(user.username);

        int numTableRows = $x("//div[contains(@id, 'manage-person')]").find("tbody").findAll("tr").size();
//        System.out.println($("tbody").findAll("tr"));
        $$x("//*[@id=\"manage-person\"]/div[2]/table/tbody/tr/td[contains(@class, 'ng-binding')]").forEach(row -> row.shouldHave(text(user.username)));
        $("tr").find("td");
        System.out.println(numTableRows);
        for(int i = 1; i <= numTableRows; i++) {
            $x("//*[@id=\"manage-person\"]/div[2]/table/tbody/tr[" + i +"]/td[1]").shouldHave(text(user.username));
        }
    }

    @Test
    public void checkAll() {
        searchPerson();
        $x("//input[contains(@ng-model, 'checkAll')]").click();
        $$x("//label/input[contains(@type, 'checkbox') and not(@data-toggle) and not(@disabled)]").forEach(box -> box.shouldBe(selected));
        $("#manage-person > div.row.m-auto.pt-3.pb-3.d-flex > div:nth-child(2) > input").setValue("aux");
        $$x("//label/input[contains(@type, 'checkbox') and not(@data-toggle) and not(@disabled)]").forEach(box -> box.shouldBe(selected));
        $("#manage-person > div.row.m-auto.pt-3.pb-3.d-flex > div:nth-child(2) > input").setValue("");
        $$x("//label/input[contains(@type, 'checkbox') and not(@data-toggle) and not(@disabled)]").forEach(box -> box.shouldBe(selected));
    }

    @Test
    public void removeFromGroupingsTest() {
        searchPerson();
        $$("input[title=\"Filter Groupings\"]").filterBy(visible).first().setValue("JTTEST-L");
        $("#manage-person > div.table-responsive-sm > table > tbody > tr > td:nth-child(6) > div > label > input").click();
        $("#manage-person > div.row.m-auto.pt-3.pb-3.d-flex > div:nth-child(4) > form > div > div > button").click();
        $(byText("Are you sure you want to remove")).parent().shouldHave(and("name of user and name of grouping", text(user.firstName), text("JTTEST-L")));
        $(byText("Yes")).click();
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, admin.timeout);
        SelenideDriver browser1 = new SelenideDriver(new SelenideConfig().headless(false).baseUrl(admin.baseURL));
        browser1.open("");
        user.loggingInNoDuoAuth(browser1);
        browser1.open("memberships");
        browser1.$x("//*[@id=\"overlay\"]/div/div").should(disappear, admin.timeout);
        browser1.$("#current-memberships > div.row.m-auto.pt-2.pb-2 > div.col-lg-3.col-md-4.col-12.p-0.pt-3.d-sm-flex > input").setValue("JTTEST-L");
        browser1.$("#current-memberships > div.ng-scope > div.table-responsive > table > tbody").lastChild().shouldNot(exist);
        browser1.$("#memberTab > li:nth-child(2) > a").click();
        browser1.$$("input[title=\"Filter Groupings\"]").filterBy(visible).first().setValue("JTTEST-L");
        browser1.$("#membership-opportunities > div.no-memberships-text.ng-scope > p").should(disappear, admin.timeout);
        browser1.$("#membership-opportunities > div.ng-scope > div.table-responsive > table > tbody > tr > td.w-8.align-middle.text-center > button").should(exist, admin.timeout);
        browser1.$("#membership-opportunities > div.ng-scope > div.table-responsive > table > tbody > tr > td.w-8.align-middle.text-center > button").click();
        browser1.$("#memberTab > li:nth-child(1) > a").click();
        browser1.$$("input[title=\"Filter Groupings\"]").filterBy(visible).first().setValue("JTTEST-L");
        browser1.$("#current-memberships > div.ng-scope > div.table-responsive > table > tbody > tr").shouldHave(text("JTTEST-L"));
        browser1.close();
    }

    @Test
    public void ownerStatus() {
        searchPerson();
//        filterGroupings();
        $$x("//*[@id=\"manage-person\"]/div[2]/table/tbody/tr/td/div[contains(@ng-if, 'l.inOwner')]/p").forEach(row -> row.shouldBe(or("Yes or No", text("Yes"), text("No"))));
    }
    @Test
    public void basisStatus() {
        $$x("//*[@id=\"manage-person\"]/div[2]/table/tbody/tr/td/div[contains(@ng-if, 'l.inBasis')]/p").forEach(row -> row.shouldBe(or("Yes or No", text("Yes"), text("No"))));
    }
    @Test
    public void includeStatus() {
        searchPerson();
        $$x("//div[@id=\"manage-person\"]/div[2]/table/tbody/tr/td/div[contains(@ng-if, '!l.inInclude')]/p").forEach(row -> row.shouldBe(or("Yes or No", text("Yes"), text("No"))));
    }
    @Test
    public void excludeStatus() {
        searchPerson();
        $$x("//*[@id=\"manage-person\"]/div[2]/table/tbody/tr/td/div[contains(@ng-if, '!l.inExclude')]/p").forEach(row -> row.shouldHave(or("Yes or No", text("Yes"), text("No"))));
    }
    @Test
    public void removeCheckbox() { //Cancel from within the Remove modal
        searchPerson();
        $$x("//*[@id=\"manage-person\"]/div[2]/table/tbody/tr/td/div/label/input").forEach(checkbox -> checkbox.shouldBe(visible));
        $("#manage-person > div.row.m-auto.pt-3.pb-3.d-flex > div:nth-child(2) > input").setValue("JTTEST-L");
        $x("//*[@id=\"manage-person\"]/div[2]/table/tbody/tr[1]/td[6]/div/label/input").click();
        $("#manage-person > div.row.m-auto.pt-3.pb-3.d-flex > div:nth-child(4) > form > div > div > button").click();
        $(byText("Are you sure you want to remove")).parent().shouldHave(and("name of user and name of grouping", text(user.firstName), text("JTTEST-L")));
        $(byText("Cancel")).click();
        $("#manage-person > div.table-responsive-sm > table > tbody > tr:nth-child(1) > td.p-10.ng-binding").shouldHave(text("JTTEST-L"));
        $("#manage-person > div.table-responsive-sm > table > tbody > tr:nth-child(1) > td:nth-child(6) > div > label > input").shouldBe(not(selected));
    }

    //@Test
    public void filterAdminsSort(){
        String rowOneAdminName = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[1]/td[1]";
        String rowOneUHNumber = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[1]/td[2]";
        String rowOneUHUsername = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[1]/td[3]";
        String sortByAdminName = "//*[@id=\"manage-admins\"]/div[2]/table/thead/tr/th[1]";
        String sortByUHNumber = "//*[@id=\"manage-admins\"]/div[2]/table/thead/tr/th[2]";
        String sortByUHUsername = "//*[@id=\"manage-admins\"]/div[2]/table/thead/tr/th[3]";

        int numTableRows = ($("tbody").findAll("tr").size());

        //$x(sortByAdminName).click();
//        $x(rowOneUHUsername).shouldBe(text("N/A"));
//        $x(rowOneAdminName).shouldBe(empty);
//        $x(rowOneUHNumber).shouldBe(text("26526604"));
        for(int i = 1; i <= numTableRows - 1; i++) {
            String xPath = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[" + i + "]/td[1]";
            String xPath2 = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[" + (i + 1) + "]/td[1]";
            assertTrue($x(xPath).getText().compareTo($x(xPath2).getText()) <= 0);
        }

        $x(sortByAdminName).click();
        $x(rowOneAdminName).shouldBe(text("Zachary T Gilbert"));
        $x(rowOneUHNumber).shouldBe(text("17118183"));
        $x(rowOneUHUsername).shouldBe(text("gilbertz"));
        for(int i = 1; i <= 19; i++) {
            String xPath = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[" + i + "]/td[1]";
            String xPath2 = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[" + (i + 1) + "]/td[1]";
            System.out.println($x(xPath).getText());
            System.out.println($x(xPath2).getText());
            assertTrue($x(xPath).getText().compareTo($x(xPath2).getText()) >= 0);
        }

        $x(sortByUHNumber).click();
        $x(rowOneAdminName).shouldBe(text("Mitchell K Ochi"));
        $x(rowOneUHNumber).shouldBe(text("10190024"));
        $x(rowOneUHUsername).shouldBe(text("ochi"));
        for(int i = 1; i <= 19; i++) {
            String xPath = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[" + i + "]/td[2]";
            String xPath2 = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[" + (i + 1) + "]/td[2]";
            assertTrue($x(xPath).getText().compareTo($x(xPath2).getText()) <= 0);
        }
        $x(sortByUHNumber).click();
        $x(rowOneAdminName).shouldBe(text("Luke F Pagtulingan"));
        $x(rowOneUHNumber).shouldBe(text("27789205"));
        $x(rowOneUHUsername).shouldBe(text("lukepag"));
        for(int i = 1; i <= 19; i++) {
            String xPath = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[" + i + "]/td[2]";
            String xPath2 = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[" + (i + 1) + "]/td[2]";
            assertTrue($x(xPath).getText().compareTo($x(xPath2).getText()) >= 0);
        }

        $x(sortByUHUsername).click();
//        $x(rowOneAdminName).shouldBe(empty);
//        $x(rowOneUHNumber).shouldBe(text("26526604"));
//        $x(rowOneUHUsername).shouldBe(text("N/A"));
        for(int i = 1; i <= 19; i++) {
            String xPath = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[" + i + "]/td[3]";
            String xPath2 = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[" + (i + 1) + "]/td[3]";
            assertTrue($x(xPath).getText().compareTo($x(xPath2).getText()) <= 0);
        }
        $x(sortByUHUsername).click();
        $x(rowOneAdminName).shouldBe(text("Victor H Lee"));
        $x(rowOneUHNumber).shouldBe(text("10222954"));
        $x(rowOneUHUsername).shouldBe(text("vhlee"));
        for(int i = 1; i <= 19; i++) {
            String xPath = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[" + i + "]/td[3]";
            String xPath2 = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[" + (i + 1) + "]/td[3]";
            assertTrue($x(xPath).getText().compareTo($x(xPath2).getText()) >= 0);
        }
//        $x(rowOneDescription).shouldBe(text("West Oahu employees able to view restricted HR content via VPA Intranet"));
//        $x(rowOneGroupingName).shouldBe(text("hr-content-authorized-uhwo"));
//
//
//        $x(sortByGroupingName).click();
//        $x(rowOneDescription).shouldBe(text("Aaron's gone, but we still can continue what he left behind..."));
//        $x(rowOneGroupingName).shouldBe(text("aaronvil-test"));
//        $x(sortByGroupingName).click();
//        $x(rowOneDescription).shouldBe(text("Test v2 of groupings with truly empty basis"));
//        $x(rowOneGroupingName).shouldBe(text("zknoebel-v2-empty-basis"));
//
//
//
//        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/input").val("awy-test");
//        $x(rowOneDescription).shouldBe(text("UH Grouping developer test grouping"));
//        $x(rowOneGroupingName).shouldBe(text("awy-test"));
    }

    //@Test
    public void filterAdminsSearch() {
        String rowOneAdminName = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[1]/td[1]";
        String rowOneUHNumber = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[1]/td[2]";
        String rowOneUHUsername = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[1]/td[3]";

        $x("//*[@id=\"manage-admins\"]/div[1]/div[2]/input").val(admin.username);
        $x(rowOneAdminName).shouldHave(text(admin.firstName));
        $x(rowOneUHNumber).shouldBe(text(admin.uhNumber));
        $x(rowOneUHUsername).shouldBe(text(admin.username));
    }
}
