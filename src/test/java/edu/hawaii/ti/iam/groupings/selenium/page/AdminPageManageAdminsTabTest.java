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
import java.util.concurrent.TimeUnit;

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
        $(by("id", "overlay")).shouldBe(disappear, Duration.ofSeconds(80));
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
            $(by("id", "overlay")).shouldBe(disappear, Duration.ofSeconds(80));
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
            $(by("id", "overlay")).shouldBe(disappear, Duration.ofSeconds(80));
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
            $(by("id", "overlay")).shouldBe(disappear, Duration.ofSeconds(80));
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
            $(by("id", "overlay")).shouldBe(disappear, Duration.ofSeconds(80));
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
            $(by("id", "overlay")).shouldBe(disappear, Duration.ofSeconds(80));
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
            $(by("id", "overlay")).shouldBe(disappear, Duration.ofSeconds(80));
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
        $x("//*[@id=\"manage-admins\"]/div[2]/table/tbody/tr[1]/td[2]").shouldBe(text(admin.uhuuid()));
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

    @Test
    public void addAdminAndAutologoutTest() {
        $("input[name=\"Add Admin\"]").setValue(user.username()).pressEnter();
        $(byText("Yes")).click();
        $(byText("Testf-iwt-b TestIAM-staff has been successfully added to the admins list.")).should(appear);
        $(byText("OK")).click();
        $x("//*[@id=\"overlay\"]/div").should(disappear, Duration.ofSeconds(80));

        SelenideDriver browser1 = new SelenideDriver(new SelenideConfig().browser("chrome").headless(false).baseUrl(property.value("app.url.home")));
        browser1.open(property.value("app.url.login"));
        loginWith(browser1.getWebDriver(), user);
        browser1.$x("/html/body/main/div[3]/div[1]/div/div/div[2]/h1/span").shouldBe(text(user.firstname()), Duration.ofSeconds(80));
        browser1.$x("/html/body/main/div[3]/div[1]/div/div/div[2]/div/h1/span/span").shouldBe(text("Admin"), Duration.ofSeconds(80));
        browser1.open("/admin");
        browser1.$x("//*[@id=\"overlay\"]/div").should(disappear, Duration.ofSeconds(80));
        browser1.$x("//*[@id=\"adminTab\"]/li[2]/a").click();

        browser1.$("input[title=\"Filter Admins\"]").setValue(user.username()).pressEnter();
        browser1.$("i[class=\"far fa-trash-alt pull-right clickable pt-1 ng-isolate-scope\"]").click();
        browser1.$(byText("Are you sure you want to remove")).shouldBe(visible);
        browser1.$(byText("Testf-iwt-b TestIAM-staff")).shouldBe(visible);
        browser1.$(byText("Yes")).click();
        browser1.$("body > main > div.container.mt-5.mb-5 > div > div.col-sm-7.d-inline-flex.align-items-center > div > div > form > button").shouldBe(visible);
        browser1.close();
    }
}
