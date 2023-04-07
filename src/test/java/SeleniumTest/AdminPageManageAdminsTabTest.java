package SeleniumTest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.byText;

public class AdminPageManageAdminsTabTest {
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
//        Configuration.browser = "safari";
        SelenideLogger.addListener("allure", new AllureSelenide());
        admin.loggingIn();
    }

    @BeforeEach
    public void setUp() {
        open(admin.baseURL + "admin");
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, admin.timeout);
        $x("//*[@id=\"adminTab\"]/li[2]/a").click();
    }
    @AfterAll
    public static void close() {
        closeWebDriver();
    }
    @RegisterExtension
    public static ScreenShooterExtension screenShooterExtension = new ScreenShooterExtension().to("target/screenshots");
    @Test
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
        $("input[title=\"Filter Admins\"]").setValue(admin.username);
        $("#manage-admins > div.table-responsive-sm > table > tbody > tr").shouldHave(and("Row should contain username uh number and name", text(admin.username), text(admin.firstName)));
    }

    @Test
    public void filterAdminsSearch() {
        String rowOneAdminName = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[1]/td[1]";
        String rowOneUHNumber = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[1]/td[2]";
        String rowOneUHUsername = "//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[1]/td[3]";

        $x("//*[@id=\"manage-admins\"]/div[1]/div[2]/input").val(admin.username);
        $x(rowOneAdminName).shouldHave(text(admin.firstName));
        $x(rowOneUHNumber).shouldBe(text(admin.uhNumber));
        $x(rowOneUHUsername).shouldBe(text(admin.username));
    }

    @Test
    public void adminNameTest() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        $("#manage-admins > div.table-responsive-sm > table > thead").scrollIntoView(true);
        String ss = screenshot("admin_table");
    }

    @Test
    public void addAdminTest() {
        $("input[name=\"Add Admin\"]").setValue(user.username).pressEnter();
        $(byText("Yes")).click();
        $(byText("Testf-iwt-a TestIAM-staff has been successfully added to the admins list.")).should(appear);
        $(byText("OK")).click();
        $x("//*[@id=\"overlay\"]/div").should(disappear, admin.timeout);
        SelenideDriver browser1 = new SelenideDriver(new SelenideConfig().browser("chrome").headless(false).baseUrl(admin.baseURL));
        browser1.open("/");
        user.loggingInNoDuoAuth(browser1);
        browser1.open("/admin");
        browser1.$x("//*[@id=\"overlay\"]/div").should(disappear, admin.timeout);
        browser1.$x("//*[@id=\"adminTab\"]/li[2]/a").click();
        $("input[title=\"Filter Admins\"]").setValue(user.username);
        $("i[class=\"far fa-trash-alt pull-right clickable pt-1 ng-isolate-scope\"]").click();
        $(byText("Are you sure you want to remove")).shouldBe(visible);
        $(byText("Testf-iwt-a TestIAM-staff")).shouldBe(visible);
        $(byText("Yes")).click();
        $x("//*[@id=\"overlay\"]/div").should(appear, admin.timeout);
        $x("//*[@id=\"overlay\"]/div").should(disappear, admin.timeout);
        browser1.close();
    }

    @Test
    public void autoLogoutTest() {
        $("input[name=\"Add Admin\"]").setValue(user.username).pressEnter();
        $(byText("Yes")).click();
        $(byText("Testf-iwt-a TestIAM-staff has been successfully added to the admins list.")).should(appear);
        $(byText("OK")).click();
        $x("//*[@id=\"overlay\"]/div").should(disappear, admin.timeout);
        SelenideDriver browser1 = new SelenideDriver(new SelenideConfig().browser("chrome").headless(false).baseUrl(user.baseURL));
        browser1.open("/");
        user.loggingInNoDuoAuth(browser1);
        browser1.open("/admin");
        browser1.$x("//*[@id=\"overlay\"]/div").should(disappear, admin.timeout);
        browser1.$(byText("Manage Admins")).click();
        browser1.$("input[title=\"Filter Admins\"]").setValue(user.username);
        browser1.$("i[class=\"far fa-trash-alt pull-right clickable pt-1 ng-isolate-scope\"]").click();
        browser1.$(byText("Are you sure you want to remove")).shouldBe(visible);
        browser1.$(byText("Testf-iwt-a TestIAM-staff")).shouldBe(visible);
        browser1.$(byText("Yes")).click();
        browser1.$x("//*[@id=\"overlay\"]/div").should(disappear, admin.timeout);
        browser1.$(byText("Login Here")).should(exist);
        browser1.close();
    }
}
