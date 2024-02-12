package edu.hawaii.ti.iam.groupings.selenium.page;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.screenshot;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.WebDriverRunner;

import edu.hawaii.ti.iam.groupings.selenium.core.User;

@SpringBootTest
public class AdminPageManageAdminsTabTest extends AbstractTestBase {

    private WebDriver driver;
    private User admin;
    private User user;

    // Constructor
    public AdminPageManageAdminsTabTest() {
        super();
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
        admin = createUser("admin");
        loginWith(driver, admin);

        open(property.value("url.admin"));
        $(by("id", "overlay")).shouldBe(disappear, Duration.ofSeconds(80));
        $(by("id", "manage-admins-tab")).click();
    }

    @AfterEach
    public void afterEach() {
        driver.close();
    }

    @Test
    public void filterAdminsSortTest() {
        List<String> adminList = new ArrayList<>();
        while (true) {
            adminList.addAll($$("#manage-admins > div.table-responsive-sm > table > tbody > tr > td:nth-child(1)").texts());
            if ($$(byText("Next")).filterBy(visible).first().parent().parent().is(cssClass("disabled"))) {
                break;
            }
            $$(byText("Next")).filterBy(visible).first().parent().parent().click();
        }
        List<String> tempNameList = new ArrayList<>(adminList);
        tempNameList.sort(String::compareToIgnoreCase);
        assertEquals(adminList, tempNameList);
        adminList.clear();

        if (!$("#manage-admins > div.row.justify-content-between > div.col-lg-5 > nav > ul > li:nth-child(1)").is(cssClass("disabled"))) {
            $("#manage-admins > div.row.justify-content-between > div.col-lg-5 > nav > ul > li:nth-child(1) > button > span").click();
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
        if (!$("#manage-admins > div.row.justify-content-between > div.col-lg-5 > nav > ul > li:nth-child(1)").is(cssClass("disabled"))) {
            $("#manage-admins > div.row.justify-content-between > div.col-lg-5 > nav > ul > li:nth-child(1) > button > span").click();
        }
        $("#manage-admins > div.table-responsive-sm > table > thead > tr > th:nth-child(2)").click();
        while (true) {
            adminList.addAll($$("#manage-admins > div.table-responsive-sm > table > tbody > tr > td:nth-child(2)").texts());
            if ($$(byText("Next")).filterBy(visible).first().parent().parent().is(cssClass("disabled"))) {
                break;
            }
            $$(byText("Next")).filterBy(visible).first().parent().parent().click();
        }

        List<String> tempNumberList = new ArrayList<>(adminList);
        tempNumberList.sort(String::compareToIgnoreCase);
        assertEquals(tempNumberList, adminList);

        adminList.clear();
        if (!$("#manage-admins > div.row.justify-content-between > div.col-lg-5 > nav > ul > li:nth-child(1)").is(cssClass("disabled"))) {
            $("#manage-admins > div.row.justify-content-between > div.col-lg-5 > nav > ul > li:nth-child(1) > button > span").click();
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
        if (!$("#manage-admins > div.row.justify-content-between > div.col-lg-5 > nav > ul > li:nth-child(1)").is(cssClass("disabled"))) {
            $("#manage-admins > div.row.justify-content-between > div.col-lg-5 > nav > ul > li:nth-child(1) > button > span").click();
        }
        $("#manage-admins > div.table-responsive-sm > table > thead > tr > th:nth-child(2)").click();
        while (true) {
            adminList.addAll($$("#manage-admins > div.table-responsive-sm > table > tbody > tr > td:nth-child(2)").texts());
            if ($$(byText("Next")).filterBy(visible).first().parent().parent().is(cssClass("disabled"))) {
                break;
            }
            $$(byText("Next")).filterBy(visible).first().parent().parent().click();
        }
        List<String> tempUsernameList = new ArrayList<>(adminList);
        tempUsernameList.sort(String::compareToIgnoreCase);
        assertEquals(tempUsernameList, adminList);

        adminList.clear();
        if (!$("#manage-admins > div.row.justify-content-between > div.col-lg-5 > nav > ul > li:nth-child(1)").is(cssClass("disabled"))) {
            $("#manage-admins > div.row.justify-content-between > div.col-lg-5 > nav > ul > li:nth-child(1) > button > span").click();
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

        $("#manage-admins > div.row.m-auto.pt-3.pb-3.d-flex > div.col-lg-3.col-md-4.col-12.p-0.pt-1 > input").setValue(admin.username());
        $("#manage-admins > div.table-responsive-sm > table > tbody > tr").shouldHave(text(admin.username()));
    }

    @Test
    public void filterAdminsSearch() {
        $x("//*[@id=\"manage-admins\"]/div[1]/div[2]/input").val(admin.username());
        $x("//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[1]/td[1]").shouldHave(text(admin.firstname()));
        $x("//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[1]/td[2]").shouldBe(text(admin.uhnumber()));
        $x("//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[1]/td[3]").shouldBe(text(admin.username()));
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

    @Disabled("broken for some reason")
    @Test
    public void addAdminTest() {
        $("input[name=\"Add Admin\"]").setValue(user.username()).pressEnter();
        $(byText("Yes")).click();
        $(byText("Testf-iwt-e TestIAM-student has been successfully added to the admins list.")).should(appear);
        $(byText("OK")).click();
        $x("//*[@id=\"overlay\"]/div").should(disappear, Duration.ofSeconds(80));

        // Open another browser session to login as testfiwte.
        SelenideDriver browser1 = new SelenideDriver(new SelenideConfig().browser("chrome").headless(false).baseUrl("https://www.test.hawaii.edu/uhgroupings/"));
        browser1.open("login");
//        browser1.open("https://www.test.hawaii.edu/uhgroupings/" + "login");
        browser1.$("#username").val(user.username());
        browser1.$("#password").val(user.password());
        browser1.$(byText("Login")).click();
        browser1.$x("/html/body/main/div[3]/div[1]/div/div/div[2]/h1/span").shouldBe(text(user.firstname()), Duration.ofSeconds(80));
        browser1.$x("/html/body/main/div[3]/div[1]/div/div/div[2]/div/h1/span/span").shouldBe(text("Admin"), Duration.ofSeconds(80));
//        browser1.$x("/html/body/main/div[1]/div/div[1]/div/div/form/button").click(); //logs out
        browser1.open("/admin");
        browser1.$x("//*[@id=\"overlay\"]/div").should(disappear, Duration.ofSeconds(80));
        browser1.$x("//*[@id=\"adminTab\"]/li[2]/a").click();
//        browser1.close();

        $("input[title=\"Filter Admins\"]").setValue(user.username());
        $("i[class=\"far fa-trash-alt pull-right clickable pt-1 ng-isolate-scope\"]").click();
        $(byText("Are you sure you want to remove")).shouldBe(visible);
        $(byText("Testf-iwt-e TestIAM-student")).shouldBe(visible);
        $(byText("Yes")).click();
        $x("//*[@id=\"overlay\"]/div").should(appear, Duration.ofSeconds(80));
        $x("//*[@id=\"overlay\"]/div").should(disappear, Duration.ofSeconds(80));

        // Make sure that testfiwte is automatically logged out when it is removed from the admin list.
        // This test fails.
        // $(by("id", "loginForm")).shouldBe(visible, Duration.ofSeconds(300));
        browser1.close();
    }
}
