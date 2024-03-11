package edu.hawaii.ti.iam.groupings.selenium.page;

import static com.codeborne.selenide.Condition.and;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.interactable;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
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

import edu.hawaii.ti.iam.groupings.selenium.core.User;

@SpringBootTest
public class ManageGroupingsTest extends AbstractTestBase {

    private WebDriver driver;
    private User admin;
    private User user;

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

        admin = createUser("admin");
        user = createUser("student");
        loginWith(driver, user);

        open(property.value("url.groupings"));
        $(by("id", "overlay")).shouldBe(disappear, Duration.ofSeconds(80));
    }

    @AfterEach
    public void afterEach() {
        driver.close();
    }

    @Test
    public void filterGroupings() {
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.d-sm-flex > input").setValue("empty");
        $$x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr").asFixedIterable()
                .forEach(row -> row.shouldHave(text("empty")));
    }

    @Test
    public void groupingName() {
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/input").setValue("testiwtb-store-empty");
        $("#manage-groupings > div.ng-scope > div.table-responsive > table > tbody > tr:nth-child(1) > td.w-35.p-10.align-middle.ng-binding").shouldHave(
                text("testiwtb-store-empty"));
    }

    @Test
    public void groupingPath() {
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/input").setValue("testiwtb-store-empty");
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/button").click();
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/ul/li[3]/label").click();
        $("#tmp\\:testiwtb\\:testiwtb-store-empty").shouldBe(visible);
    }

    @Test
    public void groupingSelection() {
        $x("//*[@id=\"manage-groupings\"]/div[2]/div[1]/table/tbody/tr[1]/td[1]").click();
        $x("//*[@id=\"sel\"]/div/section[2]/div").shouldBe(visible);
        $x("//*[@id=\"manage-groupings\"]/div[2]").shouldNotBe(visible);
    }

    @Disabled("broken")
    @Test
    public void autoLogout() {
        List<String> ownedGroupings = new ArrayList<>();
        List<String> ownedGroupingsAfterTest = new ArrayList<>();
        int i = 1;
        while (true) {
            String selector =
                    "#manage-groupings > div.ng-scope > div.table-responsive > table > tbody > tr:nth-child(" + i
                            + ") > td:nth-child(1)";

            if (!$(selector).exists()) {
                if ($("#manage-groupings > div.ng-scope > div:nth-child(2) > nav > ul > li:nth-child(4)").is(
                        cssClass("disabled"))) {
                    break;
                } else {
                    $("#manage-groupings > div.ng-scope > div:nth-child(2) > nav > ul > li:nth-child(4) > button").click();
                    i = 1;
                    selector =
                            "#manage-groupings > div.ng-scope > div.table-responsive > table > tbody > tr:nth-child("
                                    + i
                                    + ") > td:nth-child(1)";

                }
            }

            String name = $(selector).getText();
            ownedGroupings.add(name);
            i++;

        }
        // Adds admin user as owner to each of test groupings. Removes test account from owners for each test grouping.
        ownedGroupings.forEach(e -> {
            $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.d-sm-flex > input").setValue(
                    e);
            $("#manage-groupings > div.ng-scope > div.table-responsive > table > tbody > tr > td.w-35.p-10.align-middle.ng-binding").click();
            $("#group-pills > li:nth-child(5) > a").click();
            $("#owner-input").setValue(admin.username()).pressEnter();
            $("#modal-body > table > tbody").shouldHave(text(admin.username()));
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div").shouldBe(visible);
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button").click();
            $x("//*[@id=\"overlay\"]/div/div").should(disappear, Duration.ofSeconds(80));
            $("#owners-display > div.row > div.col-md-4.py-2 > input").setValue(admin.username());
            $("#owners-display > div.table-responsive > table > tbody > tr").shouldHave(text(admin.firstname()));
            $("#owners-display > div.row > div.col-md-4.py-2 > input").shouldBe(interactable);
            $("#owners-display > div.row > div.col-md-4.py-2 > input").setValue(user.username());
            $("#owners-display > div.table-responsive > table > tbody > tr > td:nth-child(1) > span > span").click();
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div").shouldBe(visible);
            $("#modal-body > table > tbody").shouldHave(text(user.username()));
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
            $x("//*[@id=\"overlay\"]/div/div").should(disappear, Duration.ofSeconds(80));
        });
        $("body > main > div.container.mt-5.mb-5 > div > div.col-sm-7.d-inline-flex.align-items-center > div > div > form > button").shouldBe(visible);
        closeWebDriver();
        loginWith(driver, admin);
        open(property.value("url.relative.groupings"));
        // Cleanup: Adds test user back to testgroupings and removes admin
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, Duration.ofSeconds(80));
        ownedGroupings.forEach(e -> {
            $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.d-sm-flex > input").setValue(
                    e);
            $("#manage-groupings > div.ng-scope > div.table-responsive > table > tbody > tr > td.w-35.p-10.align-middle.ng-binding").click();
            $("#group-pills > li:nth-child(5) > a > i").click();
            $("#owner-input").setValue(user.username()).pressEnter();
            $("#modal-body > table > tbody").shouldHave(text(user.username()));
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div").shouldBe(visible);
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button").click();
            $x("//*[@id=\"overlay\"]/div/div").should(disappear, Duration.ofSeconds(80));
            $("#owners-display > div.row > div.col-md-4.py-2 > input").setValue(user.username());
            $("#owners-display > div.table-responsive > table > tbody > tr").shouldHave(text(user.firstname()));
            $("#owners-display > div.row > div.col-md-4.py-2 > input").shouldBe(interactable);
            $("#owners-display > div.row > div.col-md-4.py-2 > input").setValue(admin.username());
            $("#owners-display > div.table-responsive > table > tbody > tr > td:nth-child(1) > span > span").click();
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div").shouldBe(visible);
            $("#modal-body > table > tbody").shouldHave(text(admin.username()));
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
            $x("//*[@id=\"overlay\"]/div/div").should(disappear, Duration.ofSeconds(80));
        });
        closeWebDriver();
        loginWith(driver, user);
        open(property.value("url.relative.groupings"));
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, Duration.ofSeconds(80));
        i = 1;
        while (true) {
            String selector =
                    "#manage-groupings > div.ng-scope > div.table-responsive > table > tbody > tr:nth-child(" + i
                            + ") > td:nth-child(1)";

            if (!$(selector).exists()) {
                if ($("#manage-groupings > div.ng-scope > div:nth-child(2) > nav > ul > li:nth-child(4)").is(
                        cssClass("disabled"))) {
                    break;
                } else {
                    $("#manage-groupings > div.ng-scope > div:nth-child(2) > nav > ul > li:nth-child(4) > button").click();
                    i = 1;
                    selector =
                            "#manage-groupings > div.ng-scope > div.table-responsive > table > tbody > tr:nth-child("
                                    + i
                                    + ") > td:nth-child(1)";

                }
            }

            String name = $(selector).getText();
            ownedGroupingsAfterTest.add(name);
            i++;

        }
        assertEquals(ownedGroupings, ownedGroupingsAfterTest);
        logger.info("autoLogout; ownedGroupings: " + ownedGroupings);
        logger.info("autoLogout; ownedGroupingsAfterTest: " + ownedGroupingsAfterTest);
    }

    @Test
    public void clipboardCopy() {
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/button").click();
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/ul/li[3]/label").click();
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/input").setValue("aux");
        $x("//*[@id=\"manage-groupings\"]/div[2]/div[1]/table/tbody/tr/td[3]/form/div/div/button/i").click();
        assertEquals(getClipboardContent(), "tmp:testiwtb:testiwtb-aux");
    }

    @Test
    public void sortGroupings() {
        int numOfGroupings = $$x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr/td[1]").size();
        for (int i = 1; i < numOfGroupings; i++) {
            assertTrue($x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr[" + i + "]/td[1]").getText().compareTo(
                    $x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr[" + (i + 1) + "]/td[1]").getText()) < 0);
        }
        $(byText("Grouping Name")).click();
        for (int i = 1; i < numOfGroupings; i++) {
            assertTrue($x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr[" + i + "]/td[1]").getText().compareTo(
                    $x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr[" + (i + 1) + "]/td[1]").getText()) > 0);
        }
    }

    @Test
    public void listingInfoTest() {
        Random random = new Random();
        ElementsCollection x =
                $$("#manage-groupings > div.ng-scope > div.table-responsive > table > tbody > tr > td.w-35.p-10.align-middle.ng-binding");
        SelenideElement randomGrouping = x.get(random.nextInt(x.size()));
        String groupingName = randomGrouping.parent().$$("td").first().getText();
        String groupingDescription = randomGrouping.parent().$$("td").get(1).getText();
        String groupingPath = randomGrouping.parent().$$("td").get(2).$("form").$("input").getValue();
        logger.info("listingInfoTest; " + groupingName + groupingDescription + groupingPath);
        randomGrouping.click();
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, Duration.ofSeconds(80));
        $("#groupNameCol > h2").shouldHave(text(groupingName));
        $("#sel > div > section:nth-child(1) > div > div:nth-child(3) > div > div:nth-child(1) > p").shouldHave(
                text(groupingDescription));
        $("#sel > div > section:nth-child(1) > div > div:nth-child(2) > div > p").shouldHave(text(groupingPath));

    }

    @Test
    public void tableSettingsTest() {
        $("#manage-groupings > div.ng-scope > div.table-responsive > table > thead > tr").shouldBe(visible);
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.d-sm-flex > div > button").click();

        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.d-sm-flex > div > ul > li").shouldBe(
                visible);
        $(byText("Show Description")).click();
        $("#manage-groupings > div.ng-scope > div.table-responsive > table > thead > tr > th:nth-child(1)").shouldBe(
                and("Visible and text", visible, text("Grouping Name")));
        $("#manage-groupings > div.ng-scope > div.table-responsive > table > thead > tr > th:nth-child(2)").shouldBe(
                and("Visible and text", visible, text("Description ")));
        $("#manage-groupings > div.ng-scope > div.table-responsive > table > thead > tr > th:nth-child(3)").shouldNotBe(visible);

        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.d-sm-flex > div > button").click();
        $(byText("Show Grouping Path")).click();
        $("#manage-groupings > div.ng-scope > div.table-responsive > table > thead > tr > th:nth-child(1)").shouldBe(
                and("Visible and text", visible, text("Grouping Name")));
        $("#manage-groupings > div.ng-scope > div.table-responsive > table > thead > tr > th:nth-child(2)").shouldNotBe(
                visible);
        $("#manage-groupings > div.ng-scope > div.table-responsive > table > thead > tr > th:nth-child(3)").shouldBe(
                and("Visible and text", visible, text("Grouping Path")));

        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.d-sm-flex > div > button").click();
        $(byText("Show All")).click();
        $("#manage-groupings > div.ng-scope > div.table-responsive > table > thead > tr > th:nth-child(1)").shouldBe(
                and("Visible and text", visible, text("Grouping Name")));
        $("#manage-groupings > div.ng-scope > div.table-responsive > table > thead > tr > th:nth-child(2)").shouldBe(
                and("Visible and text", visible, text("Description ")));
        $("#manage-groupings > div.ng-scope > div.table-responsive > table > thead > tr > th:nth-child(3)").shouldBe(
                and("Visible and text", visible, text("Grouping Path")));
    }
}
