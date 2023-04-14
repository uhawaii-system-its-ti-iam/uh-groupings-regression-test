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

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void filterAdminsSort() {
        ArrayList<String> adminList = new ArrayList<>();
        $("#manage-admins > div.table-responsive-sm > table > thead > tr > th:nth-child(1) > i").doubleClick();
        while (true) {
            adminList.addAll($$("#manage-admins > div.table-responsive-sm > table > tbody > tr > td:nth-child(1)").texts());
            if ($$(byText("Next")).filterBy(visible).first().parent().parent().is(cssClass("disabled"))) {
                break;
            }
            $$(byText("Next")).filterBy(visible).first().parent().parent().click();
        }
        ArrayList<String> tempNameList = new ArrayList<String>(adminList);
        tempNameList.sort(String::compareToIgnoreCase);
        assertEquals(adminList, tempNameList);
        adminList.clear();

        if (!$("#manage-admins > div.row.justify-content-between > div.col > nav > ul > li:nth-child(1)").is(cssClass("disabled"))) {
            $("#manage-admins > div.row.justify-content-between > div.col > nav > ul > li:nth-child(1)").click();
        }
        $("#manage-admins > div.table-responsive-sm > table > thead > tr > th:nth-child(1)").click();
        while (true) {
            adminList.addAll($$("#manage-admins > div.table-responsive-sm > table > tbody > tr > td:nth-child(1)").texts());
            if ($$(byText("Next")).filterBy(visible).first().parent().parent().is(cssClass("disabled"))) {
                break;
            }
            $$(byText("Next")).filterBy(visible).first().parent().parent().click();
        }
        Collections.reverse(tempNameList);
        System.out.println(tempNameList);
        assertEquals(tempNameList, adminList);

        adminList.clear();
        if (!$("#manage-admins > div.row.justify-content-between > div.col > nav > ul > li:nth-child(1)").is(cssClass("disabled"))) {
            $("#manage-admins > div.row.justify-content-between > div.col > nav > ul > li:nth-child(1)").click();
        }
        $("#manage-admins > div.table-responsive-sm > table > thead > tr > th:nth-child(2)").click();
        while (true) {
            adminList.addAll($$("#manage-admins > div.table-responsive-sm > table > tbody > tr > td:nth-child(2)").texts());
            if ($$(byText("Next")).filterBy(visible).first().parent().parent().is(cssClass("disabled"))) {
                break;
            }
            $$(byText("Next")).filterBy(visible).first().parent().parent().click();
        }
        ArrayList<String> tempNumberList = new ArrayList<String>(adminList);
        tempNumberList.sort(String::compareToIgnoreCase);
        assertEquals(tempNumberList, adminList);

        adminList.clear();
        if (!$("#manage-admins > div.row.justify-content-between > div.col > nav > ul > li:nth-child(1)").is(cssClass("disabled"))) {
            $("#manage-admins > div.row.justify-content-between > div.col > nav > ul > li:nth-child(1)").click();
        }

        $("#manage-admins > div.table-responsive-sm > table > thead > tr > th:nth-child(2)").click();
        while (true) {
            adminList.addAll($$("#manage-admins > div.table-responsive-sm > table > tbody > tr > td:nth-child(2)").texts());
            if ($$(byText("Next")).filterBy(visible).first().parent().parent().is(cssClass("disabled"))) {
                break;
            }
            $$(byText("Next")).filterBy(visible).first().parent().parent().click();
        }
        Collections.reverse(tempNumberList);
        assertEquals(tempNumberList, adminList);


        adminList.clear();
        if (!$("#manage-admins > div.row.justify-content-between > div.col > nav > ul > li:nth-child(1)").is(cssClass("disabled"))) {
            $("#manage-admins > div.row.justify-content-between > div.col > nav > ul > li:nth-child(1)").click();
        }
        $("#manage-admins > div.table-responsive-sm > table > thead > tr > th:nth-child(2)").click();
        while (true) {
            adminList.addAll($$("#manage-admins > div.table-responsive-sm > table > tbody > tr > td:nth-child(2)").texts());
            if ($$(byText("Next")).filterBy(visible).first().parent().parent().is(cssClass("disabled"))) {
                break;
            }
            $$(byText("Next")).filterBy(visible).first().parent().parent().click();
        }
        ArrayList<String> tempUsernameList = new ArrayList<String>(adminList);
        tempUsernameList.sort(String::compareToIgnoreCase);
        assertEquals(tempUsernameList, adminList);

        adminList.clear();
        if (!$("#manage-admins > div.row.justify-content-between > div.col > nav > ul > li:nth-child(1)").is(cssClass("disabled"))) {
            $("#manage-admins > div.row.justify-content-between > div.col > nav > ul > li:nth-child(1)").click();
        }

        $("#manage-admins > div.table-responsive-sm > table > thead > tr > th:nth-child(2)").click();
        while (true) {
            adminList.addAll($$("#manage-admins > div.table-responsive-sm > table > tbody > tr > td:nth-child(2)").texts());
            if ($$(byText("Next")).filterBy(visible).first().parent().parent().is(cssClass("disabled"))) {
                break;
            }
            $$(byText("Next")).filterBy(visible).first().parent().parent().click();
        }
        Collections.reverse(tempUsernameList);
        assertEquals(tempUsernameList, adminList);

        System.out.println(adminList);

        $("#manage-admins > div.row.m-auto.pt-3.pb-3.d-flex > div.col-lg-3.col-md-4.col-12.p-0.pt-1 > input").setValue(admin.username);
        $("#manage-admins > div.table-responsive-sm > table > tbody > tr").shouldHave(text(admin.username));
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
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div").should(appear);
        $("#modal-body > table > tbody").shouldHave(text(user.username));
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div").should(appear);
        $("#modal-body").shouldHave(text(user.firstName));
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button").click();
        $x("//*[@id=\"overlay\"]/div").should(disappear, admin.timeout);
        SelenideDriver browser1 = new SelenideDriver(new SelenideConfig().headless(false).baseUrl(user.baseURL));
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
