package edu.hawaii.ti.iam.groupings.selenium.page;

import static com.codeborne.selenide.Condition.and;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.clipboard;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.ArrayList;

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

import com.codeborne.selenide.WebDriverRunner;

import edu.hawaii.ti.iam.groupings.selenium.core.Property;
import edu.hawaii.ti.iam.groupings.selenium.core.User;

@SpringBootTest
public class AdminPageManageGroupingsTabTest extends AbstractTestBase {

    private WebDriver driver;

    // Constructor.
    public AdminPageManageGroupingsTabTest(@Autowired Property property) {
        super(property);
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

        User user = new User.Builder()
                .username(property.value("admin.user.username"))
                .password(property.value("admin.user.password"))
                .firstname(property.value("admin.user.firstname"))
                .uhnumber(property.value("admin.user.uhnumber"))
                .build();
        assertThat(user.getUsername(), not(equalTo("SET-IN-OVERRIDES")));

        loginWith(driver, user);

        open(property.value("url.admin"));
        $x("//*[@id=\"overlay\"]/div/div").shouldBe(disappear, Duration.ofSeconds(80));
    }

    @AfterEach
    public void afterEach() {
        driver.close();
    }

    @Test
    public void filterGroupingsTest() {
        $x("/html/body/main/div[2]/div[2]/div/div[1]/div[1]/div[2]/div/button").click();
        $x("/html/body/main/div[2]/div[2]/div/div[1]/div[1]/div[2]/div/ul/li[3]/label").click();

        $x("//*[@id=\"manage-groupings\"]/div[2]/table/thead/tr/th[1]").doubleClick();
        ArrayList<String> groupingsName = new ArrayList<>();
        while (true) {
            groupingsName.addAll(
                    $$("#manage-groupings > div.table-responsive > table > tbody > tr >td:nth-child(1)").texts());
            if ($$(byText("Next")).filterBy(visible).first().parent().parent().is(cssClass("disabled"))) {
                break;
            }
            $$(byText("Next")).filterBy(visible).first().parent().parent().click();
        }
        ArrayList<String> tempList = new ArrayList<String>(groupingsName);
        tempList.sort(String::compareToIgnoreCase);
        assertEquals(groupingsName, tempList);
        tempList.clear();

        ArrayList<String> groupingsDescription = new ArrayList<>();
        $x("//*[@id=\"manage-groupings\"]/div[3]/nav/ul/li[1]").click();
        $x("//*[@id=\"manage-groupings\"]/div[2]/table/thead/tr/th[2]").click();
        while (true) {
            groupingsDescription.addAll(
                    $$("#manage-groupings > div.table-responsive > table > tbody > tr >td:nth-child(2)").texts());
            if ($$(byText("Next")).filterBy(visible).first().parent().parent().is(cssClass("disabled"))) {
                break;
            }
            $$(byText("Next")).filterBy(visible).first().parent().parent().click();
        }
        tempList = new ArrayList<String>(groupingsDescription);
        tempList.sort(String::compareToIgnoreCase);
        assertEquals(groupingsDescription, tempList);
        tempList.clear();

        ArrayList<String> groupingsPath = new ArrayList<>();
        $x("//*[@id=\"manage-groupings\"]/div[3]/nav/ul/li[1]").click();
        $x("//*[@id=\"manage-groupings\"]/div[2]/table/thead/tr/th[3]").doubleClick();
        while (true) {
            groupingsPath.addAll(
                    $$("#manage-groupings > div.table-responsive > table > tbody > tr >td:nth-child(3)").texts());
            if ($$(byText("Next")).filterBy(visible).first().parent().parent().is(cssClass("disabled"))) {
                break;
            }
            $$(byText("Next")).filterBy(visible).first().parent().parent().click();
        }
        tempList = new ArrayList<String>(groupingsPath);
        tempList.sort(String::compareToIgnoreCase);
        assertEquals(groupingsPath, tempList);
        tempList.clear();
    }

    @Test
    public void groupingNameTest() {
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/input").val(property.value("test.grouping.name"));
        $x("//*[@id=\"manage-groupings\"]/div[2]/table/thead/tr/th[1]").shouldHave(text(" Grouping Name "));
        $x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr[1]/td[1]").shouldHave(
                text(property.value("test.grouping.name")));
    }

    @Test
    public void groupingDescriptionTest() {
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/input").val(property.value("test.grouping.name"));
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/button/i").click();
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/ul/li[1]/label").click();
        $x("//*[@id=\"manage-groupings\"]/div[2]/table/thead/tr/th[2]").shouldHave(text(" Description"));
        $x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr[1]/td[2]/div").shouldHave(
                text(property.value("test.grouping.description")));
    }

    @Test
    public void groupingSelectionTest() {
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/input").val(property.value("test.grouping.name"));
        $x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr[1]/td[1]").click();
        $x("//*[@id=\"groupNameCol\"]/h2").shouldHave(text(property.value("test.grouping.name")));
        $x("//*[@id=\"sel\"]/div/section[1]/div/div[2]/div/p").shouldHave(
                text("Path: " + property.value("test.grouping.path")));
        $x("//*[@id=\"sel\"]/div/section[1]/div/div[3]/div/div[1]/p").shouldHave(
                text("Description: " + property.value("test.grouping.description")));
    }

    @Test
    public void groupingPath() {
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/button/i").click();
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/ul/li[2]/label").click();
        $x("//*[@id=\"manage-groupings\"]/div[2]/table/thead/tr/th[3]").shouldHave(text(" Grouping Path "));
        String groupingName = property.value("admin.user.username") + "-aux";
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.pt-1.d-sm-flex > input").setValue(
                groupingName);
        String path = "#tmp\\:" + property.value("admin.user.username") + "\\:" + groupingName;
        $(path).shouldBe(visible);
    }

    @Test
    public void clipboardCopyTest() {
        System.setProperty("java.awt.headless", "false");
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/button/i").click();
        $(byText("Show Grouping Path")).click();
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/input").val(property.value("test.grouping.name"));
        $x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr/td[3]/form/div/div/button/i").click();
        assertEquals(property.value("test.grouping.path"), clipboard().getText());
    }

    @Test
    public void tableSettingTest() {
        $("#manage-groupings > div.table-responsive > table > thead > tr > th:nth-child(3)").shouldNotBe(visible);
        $("#manage-groupings > div.table-responsive > table > tbody > tr:nth-child(1) > td.mw-0.p-10.align-middle.d-none.d-sm-table-cell.col-auto > div").shouldBe(
                visible);
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.pt-1.d-sm-flex > div > button").click();
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.pt-1.d-sm-flex > div > ul").shouldBe(
                visible);
        $(byText("Show Grouping Path")).click();
        $("#manage-groupings > div.table-responsive > table > thead > tr > th:nth-child(3)").shouldBe(
                and("Visible and text", visible, text("Grouping Path")));
        $("#manage-groupings > div.table-responsive > table > thead > tr > th:nth-child(2)").shouldNotBe(visible);
        $("#hawaii\\.edu\\:custom\\:test\\:listserv-tests\\:JTTEST-L").shouldBe(visible);
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.pt-1.d-sm-flex > div > button").click();
        $(byText("Show All")).click();
        $("#manage-groupings > div.table-responsive > table > thead > tr > th:nth-child(2)").shouldBe(
                and("Is visible with text", visible, text("Description")));
        $("#manage-groupings > div.table-responsive > table > thead > tr > th:nth-child(3)").shouldBe(
                text("Grouping Path"));
        $("#manage-groupings > div.table-responsive > table > tbody > tr:nth-child(1) > td:nth-child(3)").should(
                visible);
        $("#manage-groupings > div.table-responsive > table > tbody > tr:nth-child(1) > td.mw-0.p-10.align-middle.d-none.d-sm-table-cell.w-35 > div").should(
                visible);
    }
}
