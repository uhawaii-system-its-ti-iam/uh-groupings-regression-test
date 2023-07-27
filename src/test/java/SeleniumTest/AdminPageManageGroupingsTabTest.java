package SeleniumTest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.byText;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdminPageManageGroupingsTabTest {
    static MainPage admin = new MainPage();
    // WebDriver driver = new ChromeDriver();
    // Configuration config = new Configuration();
    @BeforeAll
    public static void setUpAll() {
        try {
            admin.getAdminCredentials();
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
    }
    @AfterAll
    public static void close() {
        closeWebDriver();
    }
    @Test
    public void filterGroupings(){
        String rowOneDescription = "//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr[1]/td[2]/div";
        String rowOneGroupingName = "//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr[1]/td[1]";
        String sortByDescription = "//*[@id=\"manage-groupings\"]/div[2]/table/thead/tr/th[2]";
        String sortByGroupingName = "//*[@id=\"manage-groupings\"]/div[2]/table/thead/tr/th[1]";


        $x(sortByDescription).click();
        $x(rowOneDescription).shouldBe(text("Hello"));
        $x(rowOneGroupingName).shouldBe(text("win-single"));
        $x(sortByDescription).click();
        $x(rowOneDescription).shouldBe(text("Windows users test groupings 0000000000 1111111111 2222222222 3333333333 4444444444 maxlen testing"));
        $x(rowOneGroupingName).shouldBe(text("win-aux"));


        $x(sortByGroupingName).click();
        $x(rowOneDescription).shouldBe(text("Aaron's gone, but we still can continue what he left behind..."));
        $x(rowOneGroupingName).shouldBe(text("aaronvil-test"));
        $x(sortByGroupingName).click();
        $x(rowOneDescription).shouldBe(text("Test v2 of groupings with truly empty basis"));
        $x(rowOneGroupingName).shouldBe(text("zknoebel-v2-empty-basis"));



        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/input").val("awy-test");
        $x(rowOneDescription).shouldBe(text("UH Grouping developer test grouping"));
        $x(rowOneGroupingName).shouldBe(text("awy-test"));
    }

    @Test
    public void groupingNameTest() {
        String nameInTable = $("#manage-groupings > div.table-responsive > table > tbody > tr:nth-child(1) > td.w-35.p-10.align-middle.ng-binding").getText().trim();
        $("#manage-groupings > div.table-responsive > table > tbody > tr:nth-child(1) > td.w-35.p-10.align-middle.ng-binding").click();
        assertEquals(0, nameInTable.compareTo($("#groupNameCol > h2").getText().trim()));
    }

    @Test
    public void updateDescriptionTest() {
        closeWebDriver();
        admin.loggingIn();
        open(admin.baseURL + "/admin");
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, admin.timeout);
        String newDesc = admin.username + ": Test Description";
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.pt-1.d-sm-flex > input").setValue(admin.username + "-aux");
        String oldDesc = $("#manage-groupings > div.table-responsive > table > tbody > tr > td.mw-0.p-10.align-middle.d-none.d-sm-table-cell.col-auto > div").getText();

        $("#manage-groupings > div.table-responsive > table > tbody > tr > td.w-35.p-10.clickable.align-middle.ng-binding").click();
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, admin.timeout);
        $("#sel > div").shouldBe(visible);
        $("#sel > div > section:nth-child(1) > div > div:nth-child(3) > div > div:nth-child(1) > p > button").click();
        $("#sel > div > section:nth-child(1) > div > div:nth-child(3) > div > div.grouping-description-form > form > input").setValue(newDesc);
        $("#sel > div > section:nth-child(1) > div > div:nth-child(3) > div > div.grouping-description-form > form > span:nth-child(2) > button > i").click();
        $("#sel > div > section:nth-child(1) > div > div:nth-child(3) > div > div:nth-child(1) > p").shouldHave(text(newDesc));

        open(admin.baseURL + "admin");
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, admin.timeout);
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.pt-1.d-sm-flex > input").setValue(admin.username + "-aux");
        $("#manage-groupings > div.table-responsive > table > tbody > tr > td.mw-0.p-10.align-middle.d-none.d-sm-table-cell.col-auto > div").shouldHave(text(newDesc));

        $("#manage-groupings > div.table-responsive > table > tbody > tr > td.w-35.p-10.clickable.align-middle.ng-binding").click();
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, admin.timeout);
        $("#sel > div").shouldBe(visible);
        $("#sel > div > section:nth-child(1) > div > div:nth-child(3) > div > div:nth-child(1) > p > button").click();
        $("#sel > div > section:nth-child(1) > div > div:nth-child(3) > div > div.grouping-description-form > form > input").setValue(oldDesc);
        $("#sel > div > section:nth-child(1) > div > div:nth-child(3) > div > div.grouping-description-form > form > span:nth-child(2) > button > i").click();
        $("#sel > div > section:nth-child(1) > div > div:nth-child(3) > div > div:nth-child(1) > p").shouldHave(text(oldDesc));
    }
    @Test
    public void groupingPath() {
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.pt-1.d-sm-flex > div > button > i").click();
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.pt-1.d-sm-flex > div > ul").shouldBe(visible);
        $(byText("Show Grouping Path")).click();
        String groupingName = admin.username + "-aux";
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.pt-1.d-sm-flex > input").setValue(groupingName);
        String path = "#tmp\\:" + admin.username + "\\:" + groupingName;
        $(path).shouldBe(visible);
    }
    @Test
    public void clipboardCopy() {

        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/button").click();
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/ul").shouldBe(visible);
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/ul/li[3]/label").click();

        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/input").val("awy-test");
        $x("//*[@id=\"hawaii.edu:custom:test:awy:awy-test\"]").shouldBe(visible);

        $x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr/td[3]/form/div/div/button/i").click();

        System.out.println(clipboard().getText());
        assertEquals("hawaii.edu:custom:test:awy:awy-test", clipboard().getText());
    }
    @Test
    public void groupingSelection() {
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/button").click();
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/ul").shouldBe(visible);
        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/div/ul/li[3]/label").click();

        $x("//*[@id=\"manage-groupings\"]/div[1]/div[2]/input").val("awy-test");
        $x("//*[@id=\"hawaii.edu:custom:test:awy:awy-test\"]").shouldBe(visible);

        $x("//*[@id=\"manage-groupings\"]/div[2]/table/tbody/tr/td[1]").click();
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, admin.timeout);
        $x("//*[@id=\"groupNameCol\"]/h2").shouldBe(text(" awy-test"));
        $x("//*[@id=\"sel\"]/div/section[1]/div/div[3]/div/div[1]/p").shouldBe(text("UH Grouping developer test grouping"));
    }

    @Test
    public void tableSettingsTest() {
        closeWebDriver();
        admin.loggingIn();
        open(admin.baseURL + "/admin");
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, admin.timeout);
        $("#manage-groupings > div.table-responsive > table > thead > tr > th:nth-child(3)").shouldNotBe(visible);
        $("#manage-groupings > div.table-responsive > table > tbody > tr:nth-child(1) > td.mw-0.p-10.align-middle.d-none.d-sm-table-cell.col-auto > div").shouldBe(visible);
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.pt-1.d-sm-flex > div > button").click();
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.pt-1.d-sm-flex > div > ul").shouldBe(visible);
        $(byText("Show Grouping Path")).click();
        $("#manage-groupings > div.table-responsive > table > thead > tr > th:nth-child(3)").shouldBe(and("Visible and text", visible, text("Grouping Path")));
        $("#manage-groupings > div.table-responsive > table > thead > tr > th:nth-child(2)").shouldNotBe(visible);
        $("#hawaii\\.edu\\:custom\\:test\\:listserv-tests\\:JTTEST-L").shouldBe(visible);
        $("#manage-groupings > div.row.m-auto.pt-3.pb-3 > div.col-lg-3.col-md-4.col-12.p-0.pt-1.d-sm-flex > div > button").click();
        $(byText("Show All")).click();
        $("#manage-groupings > div.table-responsive > table > thead > tr > th:nth-child(2)").shouldBe(and("Is visible with text", visible, text("Description")));
        $("#manage-groupings > div.table-responsive > table > thead > tr > th:nth-child(3)").shouldBe(text("Grouping Path"));
        $("#manage-groupings > div.table-responsive > table > tbody > tr:nth-child(1) > td:nth-child(3)").should(visible);
        $("#manage-groupings > div.table-responsive > table > tbody > tr:nth-child(1) > td.mw-0.p-10.align-middle.d-none.d-sm-table-cell.w-35 > div").should(visible);
    }
}
