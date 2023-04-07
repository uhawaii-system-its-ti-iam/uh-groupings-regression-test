package SeleniumTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.SelenideWait;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.xml.datatype.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.byText;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManageGroupingsTest {
    static MainPage user = new MainPage();
    static MainPage admin = new MainPage();
    // WebDriver driver = new ChromeDriver();
//     Configuration config = new Configuration();
    @BeforeAll
    public static void setUpAll() {
        try {
            user.getUserCredentials();
            admin.getAdminCredentials();
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
        open(user.baseURL + "groupings");
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
    }

    @Test
    public void filterGroupings() {
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/input").setValue("empty");
        $$x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr").asFixedIterable().forEach(row -> row.shouldHave(text("empty")));
    }
//    @Test
    public void groupingName() {
    }
//    @Test
    public void groupingPath() {

    }
    @Test
    public void groupingSelection() {
        $x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr[1]/td[1]").click();
        $x("//*[@id=\"sel\"]/div/section[2]/div").shouldBe(visible);
        $x("//*[@id=\"manage-groupings\"]/div[2]").shouldNotBe(visible);
    }

    @Test
    public void autoLogout() {
        List<String> ownedGroupings = new ArrayList<>();
        List<String> ownedGroupingsAfterTest = new ArrayList<>();
        while (true) {
            ownedGroupings.addAll($$("#manage-groupings > div.table-responsive > table > tbody > tr > td.w-35.p-10.align-middle.ng-binding").texts());
            if ($("#manage-groupings > div:nth-child(3) > nav > ul > li:nth-child(4)").is(cssClass("disabled"))) {
                break;
            }
        }
        ownedGroupings.forEach(e -> {
            $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.d-sm-flex > input").setValue(
                    e);
            $("#manage-groupings > div.table-responsive > table > tbody > tr:nth-child(1) > td.w-35.p-10.align-middle.ng-binding").click();
            $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
            $("#group-pills > li:nth-child(5) > a").click();
            $("#owner-input").setValue(admin.username).pressEnter();
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div").shouldBe(visible);
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div").shouldBe(visible);
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button").click();
            $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
            $("#owners-display > div.row > div.col-md-4.py-2 > input").shouldBe(interactable);
            $("#owners-display > div.row > div.col-md-4.py-2 > input").setValue(admin.username);
            $("#owners-display > div.table-responsive > table > tbody > tr").shouldHave(text(admin.firstName));
            $("#owner-input").setValue(user.uhNumber);
            $("#owners-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memBtns > button.btn.btn-remove.add-margin").click();
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div").shouldBe(visible);
            $("#modal-body > table > tbody").shouldHave(text(user.username));
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
            $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
        });
        $("body > main > div.container.mt-5.mb-5 > div > div.col-sm-7.d-inline-flex.align-items-center > div > div > form > button").shouldBe(visible);

        closeWebDriver();
        admin.loggingIn();
        open(admin.baseURL + "groupings");

        ownedGroupings.forEach(e -> {
            $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.d-sm-flex > input").setValue(e);
            $("#manage-groupings > div.table-responsive > table > tbody > tr > td.w-35.p-10.align-middle.ng-binding").click();
            $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
            $("#group-pills > li:nth-child(5) > a").click();
            $("#owner-input").setValue(user.username).pressEnter();
            $("#modal-body > table > tbody").shouldHave(text(user.username));
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div").shouldBe(visible);
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button").click();
            $("#owner-input").setValue(admin.uhNumber);
            $("#owners-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memBtns > button.btn.btn-remove.add-margin").click();
            $("#modal-body > table > tbody").shouldHave(text(admin.username));
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
            $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
        });

        closeWebDriver();
        user.loggingInNoDuoAuth();
        open(user.baseURL + "groupings");
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
        while (true) {
            ownedGroupingsAfterTest.addAll($$("#manage-groupings > div.table-responsive > table > tbody > tr > td.w-35.p-10.align-middle.ng-binding").texts());
            if ($("#manage-groupings > div:nth-child(3) > nav > ul > li:nth-child(4)").is(cssClass("disabled"))) {
                break;
            }
        }
        assertEquals(ownedGroupings, ownedGroupingsAfterTest);
        System.out.println(ownedGroupings);
        System.out.println(ownedGroupingsAfterTest);
    }
    @Test
    public void clipboardCopy() {
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/button").click();
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/ul/li[3]/label").click();
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/input").setValue("aux");
        $x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr/td[3]/form/div/div/button").click();
        assertEquals(clipboard().getText(), "tmp:testiwta:testiwta-aux");
    }
    @Test
    public void sortGroupings() {
        int numOfGroupings = $$x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr/td[1]").size();
        for(int i = 1; i < numOfGroupings; i++) {
            assertTrue($x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr[" + i + "]/td[1]").getText().compareTo($x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr[" + (i + 1) + "]/td[1]").getText()) < 0);
        }
        $(byText("Grouping Name")).click();
        for(int i = 1; i < numOfGroupings; i++) {
            assertTrue($x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr[" + i + "]/td[1]").getText().compareTo($x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr[" + (i + 1) + "]/td[1]").getText()) > 0);
        }
    }

    @Test
    public void listingInfoTest() {
        Random random = new Random();
        ElementsCollection x = $$("#manage-groupings > div.table-responsive > table > tbody > tr > td.w-35.p-10.align-middle.ng-binding");
        SelenideElement randomGrouping = x.get(random.nextInt(x.size()));
        String groupingName = randomGrouping.parent().$$("td").first().getText();
        String groupingDescription = randomGrouping.parent().$$("td").get(1).getText();
        String groupingPath = randomGrouping.parent().$$("td").get(2).$("form").$("input").getValue();
        System.out.println(groupingName + groupingDescription + groupingPath);
        randomGrouping.click();
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
        $("#groupNameCol > h2").shouldHave(text(groupingName));
        $("#sel > div > section:nth-child(1) > div > div:nth-child(3) > div > div:nth-child(1) > p").shouldHave(text(groupingDescription));
        $("#sel > div > section:nth-child(1) > div > div:nth-child(2) > div > p").shouldHave(text(groupingPath));

    }
    @Test
    public void tableSettingsTest() {
        $("#manage-groupings > div.table-responsive > table > thead > tr > th:nth-child(3)").shouldNotBe(visible);
        $("#manage-groupings > div.table-responsive > table > tbody > tr:nth-child(1) > td.mw-0.p-10.align-middle.d-none.d-sm-table-cell.col-auto > div").shouldBe(visible);
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.d-sm-flex > div > button").click();
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.d-sm-flex > div > ul").shouldBe(visible);
        $(byText("Show Grouping Path")).click();
        $("#manage-groupings > div.table-responsive > table > thead > tr > th:nth-child(3)").shouldBe(and("Visible and text", visible, text("Grouping Path")));
        $("#manage-groupings > div.table-responsive > table > thead > tr > th:nth-child(2)").shouldNotBe(visible);
        $("#tmp\\:testiwta\\:testiwta-aux").shouldBe(visible);
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.d-sm-flex > div > button").click();
        $(byText("Show All")).click();
        $("#manage-groupings > div.table-responsive > table > thead > tr > th:nth-child(2)").shouldBe(and("Is visible with text", visible, text("Description")));
        $("#manage-groupings > div.table-responsive > table > thead > tr > th:nth-child(3)").shouldBe(text("Grouping Path"));
        $("#manage-groupings > div.table-responsive > table > tbody > tr:nth-child(1) > td:nth-child(3)").should(visible);
        $("#manage-groupings > div.table-responsive > table > tbody > tr:nth-child(1) > td.mw-0.p-10.align-middle.d-none.d-sm-table-cell.w-35 > div").should(visible);
    }
}
