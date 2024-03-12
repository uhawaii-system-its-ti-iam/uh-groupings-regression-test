package edu.hawaii.ti.iam.groupings.selenium.page;

import static com.codeborne.selenide.Condition.and;
import static com.codeborne.selenide.Condition.checked;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.or;
import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.ArrayList;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeborne.selenide.WebDriverRunner;

@SpringBootTest
public class AdminPageManagePersonTabTest extends AbstractTestBase {

    private WebDriver driver;

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.safaridriver().setup();
        WebDriverRunner.setWebDriver(new SafariDriver());
    }

    @AfterAll
    public static void afterAll() {
        closeWebDriver();
    }

    @BeforeEach
    public void setUp() {
        open(property.value("app.url.login"));
        driver = WebDriverRunner.getWebDriver();

        loginWith(driver, createUser("admin"));

        open(property.value("url.admin"));
        $x("//*[@id=\"overlay\"]/div/div").shouldBe(disappear, Duration.ofSeconds(30));
        $(by("id", "manage-person-tab")).click();
    }

    @AfterEach
    public void afterEach() {
        driver.close();
    }

    @Test
    public void searchAndFilterGroupings() {
        $x("/html/body/main/div[2]/div[2]/div/div[3]/div[1]/div[3]/form/div/input").setValue(
                property.value("admin.user.username"));
        $x("/html/body/main/div[2]/div[2]/div/div[3]/div[1]/div[3]/form/div/div/button").click();
        $x("//*[@id=\"overlay\"]/div/div").shouldBe(disappear, Duration.ofSeconds(30));
        $x("/html/body/main/div[2]/div[2]/div/div[3]/div[1]/div[2]/input").setValue(
                property.value("admin.user.username"));

        ArrayList<String> groupings = new ArrayList<>();
        while (true) {
            groupings.addAll($$(".manage-person > tbody:nth-child(2) > tr > td:nth-child(1)").texts());
            if ($$(byText("Next")).filterBy(visible).first().parent().parent().is(cssClass("disabled"))) {
                break;
            }
            $$(byText("Next")).filterBy(visible).first().parent().parent().click();
        }
        boolean pass = true;
        for (String s : groupings) {

            if (!s.contains(property.value("admin.user.username"))) {
                pass = false;
            }
        }
        assertTrue(pass);

    }

    @Test
    public void searchPerson() {
        $x("//input[contains(@name, 'Search Person')]").val(property.value("admin.user.username"));
        $x("//button[contains(text(), 'Search')]").click();
        $x("//div[contains(@class, 'spinner-border')]").should(disappear, Duration.ofSeconds(30));
        $x("//h3").shouldHave(text(property.value("admin.user.firstname")));
        $x("//h3").shouldHave(text(property.value("admin.user.username")));
        $x("//h3").shouldHave(text(property.value("admin.user.uhuuid")));
    }

    @Test
    public void CheckAll() {
        searchPerson();
        $x("/html/body/main/div[2]/div[2]/div/div[3]/div[1]/div[2]/input").setValue(
                property.value("admin.user.username"));
        $(byText("Check All")).click();
        $$x("//label/input[contains(@type, 'checkbox') and not(@data-toggle) and not(@disabled)]").forEach(
                box -> box.shouldBe(selected));
    }

    @Test
    /** Make sure that 'testiwta-aux' has only one owner, 'testiwta' before running this test **/
    public void removeFromGrouping() {
        // unable to remove the sole owner of any grouping such as aux grouping type of test account
        searchPerson();
        $x("/html/body/main/div[2]/div[2]/div/div[3]/div[1]/div[2]/input").setValue(
                property.value("admin.user.username") + "-" +
                        property.value("test.grouping.type"));
        $(".manage-person > tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(1)").shouldHave(
                text(property.value("admin.user.username") + "-" + property.value("test.grouping.type")));

        $x("//*[@id=\"manage-person\"]/div[2]/table/tbody/tr/td[6]/div/label/input").click();
        $x("//*[@id=\"manage-person\"]/div[1]/div[4]/form/div/div/button").click();
        $(byText("You are unable to remove this owner. There must be at least one owner remaining.")).shouldBe(visible);
        $(byText("Close")).click();

        //remove an owner form Grouping which has at least 2 owners .
        $x("/html/body/main/div[2]/div[2]/div/div[3]/div[1]/div[2]/input").setValue(
                property.value("test.grouping.name"));
        $(".manage-person > tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(1)").shouldHave(
                text(property.value("test.grouping.name")));
        $x("/html/body/main/div[2]/div[2]/div/div[3]/div[2]/table/tbody/tr/td[6]/div/label/input").click();
        $x("/html/body/main/div[2]/div[2]/div/div[3]/div[1]/div[4]/form/div/div/button").click();
        $(byText("Are you sure you want to remove")).parent().shouldHave(
                and("name of user and name of grouping", text(property.value("admin.user.firstname")),
                        text(property.value("test.grouping.name"))));
        $(byText("Yes")).click();
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, Duration.ofSeconds(30));

        searchPerson();
        $x("/html/body/main/div[2]/div[2]/div/div[3]/div[1]/div[2]/input").setValue(
                property.value("test.grouping.name"));
        $x("/html/body/main/div[2]/div[2]/div/div[3]/div[2]/table/tbody").lastChild().shouldNotBe(exist);

        // add the owner back to the grouping
        $(by("id", "manage-groupings-tab")).click();
        $(byText(property.value("test.grouping.name"))).click();
        $x("/html/body/main/div[2]/div[3]/div[2]/div/section[2]/div/div/div[1]/ul/li[5]/a").click();
        $("#owner-input").setValue(property.value("admin.user.username"));
        $("div.mt-2:nth-child(1) > form:nth-child(1) > div:nth-child(1) > div:nth-child(2) > button:nth-child(1)").click();
        $(byText("Are you sure you want to add")).parent().shouldHave(text(property.value("admin.user.firstname")));
        $(byText("Yes")).click();
        $(byText("OK")).click();
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, Duration.ofSeconds(30));
    }


    @Test
    public void GroupingName() {
        searchPerson();
        $x("/html/body/main/div[2]/div[2]/div/div[3]/div[1]/div[2]/input").setValue(
                property.value("test.grouping.name"));
        $(".manage-person > tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(1)").shouldHave(
                text(property.value("test.grouping.name")));
    }

    @Test
    public void OwnerStatus() {
        searchPerson();
        $$x("//*[@id=\"manage-person\"]/div[2]/table/tbody/tr/td/div[contains(@ng-if, 'l.inOwner')]/p").forEach(row -> row.shouldBe(or("Yes or No", text("Yes"), text("No"))));

    }

    @Test
    public void basisStatus() {
        searchPerson();

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
    public void remove() {
        searchPerson();
        $$x("//*[@id=\"manage-person\"]/div[2]/table/tbody/tr/td/div[contains(@ng-if, '!l.remove')]/p").forEach(row -> row.shouldNotBe(checked));
        CheckAll();
        $$x("//*[@id=\"manage-person\"]/div[2]/table/tbody/tr/td/div[contains(@ng-if, '!l.remove')]/p").forEach(row -> row.shouldBe(checked));

    }

    @Test
    public void cancelFromWithinRemoveModal() {
        searchPerson();
        $x("/html/body/main/div[2]/div[2]/div/div[3]/div[1]/div[2]/input").setValue(
                property.value("test.grouping.name"));
        $(".manage-person > tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(1)").shouldHave(
                text(property.value("test.grouping.name")));
        $x("/html/body/main/div[2]/div[2]/div/div[3]/div[2]/table/tbody/tr/td[6]/div/label/input").click();
        $x("/html/body/main/div[2]/div[2]/div/div[3]/div[1]/div[4]/form/div/div/button").click();
        $(byText("Are you sure you want to remove")).parent().shouldHave(
                and("name of user and name of grouping", text(property.value("admin.user.firstname")),
                        text(property.value("test.grouping.name"))));
        $(byText("Cancel")).click();

        // check if the groupings exist
        GroupingName();
    }

}
