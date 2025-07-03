package edu.hawaii.ti.iam.groupings.selenium.page.firefox;

import static com.codeborne.selenide.Condition.and;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.DownloadOptions.using;
import static com.codeborne.selenide.FileDownloadMode.FOLDER;
import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.clearBrowserCookies;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.ex.FileNotDownloadedError;
import com.opencsv.CSVWriter;

import edu.hawaii.ti.iam.groupings.selenium.core.User;
import edu.hawaii.ti.iam.groupings.selenium.page.AbstractTestBase;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GroupingSelectionTestFirefox extends AbstractTestBase {

    private WebDriver driver;
    private User admin;
    private User user;
    private final Duration timeout = Duration.ofSeconds(80);

    public GroupingSelectionTestFirefox() {
        super();
    }

    @BeforeAll
    public static void beforeAll() {
        //        Proxy proxy = new Proxy();
        //        proxy.setAutodetect(false);
        //        proxy.setHttpProxy("localhost:8888");
        //        proxy.setSslProxy("localhost:8080");
        //        Configuration.proxyEnabled = true;
        //        Configuration.fileDownload = PROXY;

        //        ChromeOptions options = new ChromeOptions();
        //        options.setCapability("proxy", proxy);

        WebDriverManager.firefoxdriver().setup();
        WebDriverRunner.setWebDriver(new FirefoxDriver());
        //        Configuration.fileDownload = FOLDER;
        //        Configuration.downloadsFolder = System.getProperty("user.dir") + File.separator + "downloadFile";
        //        Configuration.downloadsFolder = "build/reports/tests";
        //                System.getProperty("user.dir") + File.separator + "downloadFile";
        //        SelenideLogger.addListener("allure", new AllureSelenide());
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

        open(property.value("url.groupings"));
        $(by("id", "overlay")).shouldBe(disappear, timeout);
        $(byText("testiwta-many")).click();
        $(by("id", "sel")).shouldBe(visible);
    }

    @AfterEach
    public void afterEach() {
        driver.close();
    }

    @Test
    @Order(1)
    public void groupingName() {
        $(by("id", "selectedGroupHeader")).shouldHave(text("testiwta-many"));
    }
    @Test
    @Order(2)
    public void groupingPath() {
        $(byText("tmp:testiwta:testiwta-many")).shouldBe(visible);
    }
    @Test
    @Order(3)
    public void groupingDescription() {
        $("#sel > div > section:nth-child(1) > div > div:nth-child(3) > div > div:nth-child(1) > p").shouldHave(text("Description: Many Groupings in Basis.                 "));
    }
    @Test
    @Order(4)
    public void returnToGroupingsButton() {
        $("body > main > div.container > div:nth-child(3) > div.mt-4 > button:nth-child(1)").shouldBe(visible, timeout).click();
        $(by("id", "sel")).should(disappear);
        $(by("id", "manage-groupings")).shouldBe(visible);
    }
    @Test
    @Order(5)
    public void filterMembers() {
        $("#all > div.row > div.col-md-4.py-2 > input").setValue("aaron");
        $("#all > div.table-responsive-sm > table > tbody > tr:nth-child(1) > td:nth-child(1) > div").shouldHave(text("aaron"));

    }
    @Disabled
    @Test
    @Order(6)
    public void exportMembersCSV() {
        $("#csvButton > button").shouldBe(not(disabled), timeout);
        //        $(byText("Export Members")).click();
        //        $("#csvButton > button").click();
        //        $("#csvButton > div > button:nth-child(1)").shouldBe(interactable);
        $(by("id", "csvButton")).click();
        File downloadedFile = new File("");
        try {
            downloadedFile = $("#csvButton > div > div:nth-child(1)").download(using(FOLDER)
                    //                .withFilter(withExtension("csv"))
                    .withTimeout(5000)
            );
        } catch (FileNotDownloadedError e) {
            System.out.println(e);
        }
        String path = downloadedFile.getPath();
        //        System.out.println(path);
        try {
            String expectedValue = writeCSV(admin.username());
            InputStream downloadedCSV = Files.newInputStream(Paths.get(path));
            String downloadedValue = IOUtils.toString(downloadedCSV, StandardCharsets.UTF_8);
            //                System.out.println(downloadedValue);
            String hashedDownload = DigestUtils.sha1Hex(downloadedCSV);
            //                System.out.println(expectedHash +"\n" + hashedDownload);
            assertEquals(expectedValue, hashedDownload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!Configuration.browser.equals("firefox")) {
            assertEquals("testiwta-many_members_list.csv", downloadedFile.getName(), "File name does not match");
        } else {
            assertEquals("testiwta-many members_list.csv", downloadedFile.getName(), "File name does not match");
        }
        try {
            FileUtils.deleteDirectory(new File(System.getProperty("user.dir") + File.separator + "downloadFile"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Disabled
    @Test
    @Order(7)
    public void exportBasisCSV() {
        $("#csvButton > button").shouldBe(not(disabled), timeout);
        $("#csvButton > button").click();
        $("#csvButton > ul > li:nth-child(1) > label").shouldBe(interactable);
        File downloadedFile = $("#csvButton > ul > li:nth-child(1) > label").download();
        $("#group-pills > li:nth-child(2) > a").click();
        String path = downloadedFile.getPath();
        try {
            String expectedValue = writeCSV(user.username());
            InputStream downloadedCSV = Files.newInputStream(Paths.get(path));
            String downloadedValue = IOUtils.toString(downloadedCSV, StandardCharsets.UTF_8);
            //                System.out.println(downloadedValue);
            String hashedDownload = DigestUtils.sha1Hex(downloadedCSV);
            //                System.out.println(expectedHash +"\n" + hashedDownload);
            assertEquals(expectedValue, hashedDownload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!Configuration.browser.equals("firefox")) {
            assertEquals("testiwta-many_basis_list.csv", downloadedFile.getName(), "File name does not match");
        } else {
            assertEquals("testiwta-many basis_list.csv", downloadedFile.getName(), "File name does not match");
        }
        try {
            FileUtils.deleteDirectory(new File(System.getProperty("user.dir") + File.separator + "downloadFile"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String writeCSV(String username) throws IOException {
        String tmp;
        String user;
        String pattern2 = "\n";
        String pattern3 = " ([a-zA-z] )+";
        String pattern4 = " (Basis & Include|Basis|Include)";
        String pattern5 = ", Jr";
        String pattern6 = ", (III|VI|II|Jr)*";
        String pattern7 = " [0-9]{8} ";
        String pattern8 = " \\||\\| ";
        String[] csvRow = new String[5];
        ArrayList<String[]> csvStuff = new ArrayList<>();
        WebElementCondition clickable = and("can be clicked", visible, enabled);
        File csvFile = new File(System.getProperty("user.dir") + "/downloadFile/" + username + "-many_members_list.csv");
        FileWriter outputFile = new FileWriter(csvFile);
        CSVWriter writer = new CSVWriter(outputFile, ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.RFC4180_LINE_END);
        String[] header = {"Last", "First", "Username", "UH Number", "Email"};
        writer.writeNext(header);
        csvStuff.add(header);
        SelenideElement nextButton = $$(byText("Next")).filterBy(clickable).first().parent().parent();

        while(true) {
            $("#basis-display > div.row > div.col-md-8 > div").shouldNotBe(visible, timeout);
            ElementsCollection x = $$("tbody > tr").filterBy(visible);
            System.out.println(x.size());
            for (SelenideElement row : x) {
                user = row.getText();
                user = user.replace(pattern2, " ");
                user = user.replaceAll(pattern5,"");
                user = user.replaceAll(pattern6, "");
                user = user.replaceAll(pattern3, "|");
                user = user.replaceAll(pattern7, "|$0|");
                user = user.replaceAll(pattern4, "");
                user = user.replaceAll(pattern8, "|");
                if (StringUtils.countMatches(user, "|") < 3) {
                    user = user.replace(" ", "|");
                }
                String[] formatted = user.split("\\|");

                if (formatted.length > 4) {
                    for (int i = 0; i < formatted.length - 4; i++) {
                        formatted[0] = formatted[0] + " " + (formatted[i + 1]);
                    }
                    for (int i = 1; i < formatted.length - (formatted.length - 4); i++) {
                        formatted[i] = formatted[i + (formatted.length - 4)];
                    }
                }
                tmp = formatted[1];
                formatted[1] = formatted[0];
                formatted[0] = tmp;
                tmp = formatted[3];
                formatted[3] = formatted[2];
                formatted[2] = tmp;
                System.arraycopy(formatted, 0, csvRow, 0, 4);
                if (csvRow[2].equals("N/A")) {
                    csvRow[4] = "";
                    csvRow[2] = "";
                } else {
                    csvRow[4] = csvRow[2] + "@hawaii.edu";
                }
                if (csvRow[3].equals("23505665")) {
                    csvRow[1] = csvRow[1] + " A";
                }
                csvStuff.add(csvRow);
                writer.writeNext(csvRow);
            }
            if (nextButton.is(cssClass("disabled"))) {
                break;
            }
            nextButton.click();
        }
        writer.close();
        InputStream text = Files.newInputStream(Paths.get(csvFile.getPath()));
        String words = IOUtils.toString(text, StandardCharsets.UTF_8);
        String hashedValue = DigestUtils.sha1Hex(text);
        return hashedValue;
    }
    @Test
    @Order(8)
    public void addRemoveMembersUsername() {
        open(property.value("url.groupings"));
        $(byText("testiwta-store-empty")).click();
        $x("//*[@id=\"group-pills\"]/li[3]/a").click();
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(user.username()).pressEnter();
        $x("/html/body/div[1]/div/div").shouldBe(visible);
        $x("//*[@id=\"modal-body\"]/table/tbody/tr[2]/th[2]").shouldHave(text(user.username()));
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
        $x("/html/body/div[1]/div/div").shouldBe(visible);
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, timeout);
        $("#overlay").should(disappear, timeout);
        $("#modal-body").shouldHave(text(user.firstname()), timeout);
        $(byText("OK")).click();
        removeMemberTextBox(user.username());
    }

    @Test
    @Order(9)
    public void addRemoveMembersUhNumber() {
        open(property.value("url.groupings"));
        $(byText("testiwta-store-empty")).click();
        $x("//*[@id=\"group-pills\"]/li[3]/a").click();
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(user.uhuuid()).pressEnter();
        $x("/html/body/div[1]/div/div").shouldBe(visible);
        $("#modal-body > table > tbody").shouldHave(text(user.uhuuid()));
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
        $x("/html/body/div[1]/div/div").shouldBe(visible);
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, timeout);
        $("#overlay").should(disappear, timeout);
        $("#modal-body").shouldHave(text(user.firstname()));
        $(byText("OK")).click();
        removeMemberTextBox(user.uhuuid());
    }

    @Test
    @Order(10)
    public void removeMemberTrashCan() {
        open(property.value("url.groupings"));
        $(byText("testiwta-store-empty")).click();
        $x("//*[@id=\"group-pills\"]/li[3]/a").click();
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(user.uhuuid()).pressEnter();
        $x("/html/body/div[1]/div/div").shouldBe(visible, timeout);
        $("#modal-body > table > tbody").shouldHave(text(user.uhuuid()), timeout);
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
        $x("/html/body/div[1]/div/div").shouldBe(visible, timeout);
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, timeout);
        $("#modal-body").shouldHave(text(user.firstname()), timeout);
        $(byText("OK")).click();
        $("#include-display input[title=\"Filter Members\"]").setValue(user.username());
        $("span[class=\"far fa-trash-alt fa-pull-right clickable pt-1 ng-isolate-scope\"]").click();
        $("#modal-body").shouldBe(visible, timeout);
        $("#modal-body").$("tbody").shouldHave(text(user.uhuuid()), timeout);
        $(byText("Yes")).click();
        $("#modal-body").shouldHave(text(user.firstname()), timeout);
        $(byText("OK")).click();
        $("#overlay").should(disappear, timeout);
        $("#include-display tbody").shouldNotHave(text(user.username()), timeout);
    }

    public void removeMemberTextBox(String userInfo) {
        open(property.value("url.groupings"));
        $("#overlay").should(disappear, timeout);
        $(byText("testiwta-store-empty")).click();
        $x("//*[@id=\"group-pills\"]/li[3]/a").click();
        $("#include-display > div.table-responsive-sm > table > tbody").shouldHave(text(userInfo), timeout);
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(userInfo);
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memBtns > button.btn.btn-remove").click();
        $x("/html/body/div[1]/div/div").shouldBe(visible, timeout);
        $("#modal-body").shouldHave(text(userInfo), timeout);
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
        $(byText("OK")).click();
        $("#overlay").should(disappear, timeout);
        $(byText(user.username())).shouldNot(exist, timeout);
    }
    @Test
    @Order(11)
    public void multiAddMembersUsername() {
        open(property.value("url.groupings"));
        $("#overlay").should(disappear, timeout);
        $(byText("testiwta-store-empty")).click();
        $x("//*[@id=\"group-pills\"]/li[3]/a").click();
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(admin.username() + " " + user.username()).pressEnter();
        $("#modal-body").shouldBe(visible);
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
        $("#overlay").should(disappear, timeout);
        $(byText("OK")).click();
        $x("//*[@id=\"pill-content\"]").shouldHave(and("Admin and user first name should be in the include table", text(admin.firstname()), text(user.firstname())));
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(admin.username() + " " + user.username());
        $(byText("Remove")).click();
        $x("//*[@id=\"modal-body\"]").shouldHave(and("Modal is visible and contains admin and user username", text(admin.username()), text(user.username())));
        $(byText("Yes")).click();
        $(byText("OK")).click();
        $("#overlay").should(disappear, timeout);
        $x("//*[@id=\"pill-content\"]").shouldNotHave(and("Admin and user should not be in include table", text(admin.username()), text(user.username())));
    }
    @Test
    @Order(12)
    public void multiAddMembersUhNumber() {
        open(property.value("url.groupings"));
        $("#overlay").should(disappear, timeout);
        $(byText("testiwta-store-empty")).click();
        $x("//*[@id=\"group-pills\"]/li[3]/a").click();
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(admin.uhuuid() + " " + user.uhuuid()).pressEnter();
        $("#modal-body").shouldBe(visible);
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
        $("#overlay").should(disappear);
        $(byText("OK")).click();
        $x("//*[@id=\"pill-content\"]").shouldHave(and("Admin and user first name should be in the include table", text(admin.firstname()), text(user.firstname())));
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(admin.uhuuid() + " " + user.uhuuid());
        $(byText("Remove")).click();
        $x("//*[@id=\"modal-body\"]").shouldHave(and("Modal is visible and contains admin and user username", text(admin.username()), text(user.username())));
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
        $(byText("OK")).click();
        $("#overlay").should(disappear, timeout);
        $x("//*[@id=\"pill-content\"]").shouldNotHave(and("Admin and user should not be in include table", text(admin.username()), text(user.username())));
    }
    @Test
    // When running this test make a batch-import.txt file in your .<name>-conf and put testiwta and testiwtb on different lines
    @Order(13)
    public void batchImport() throws InterruptedException {
        open(property.value("url.groupings"));
        $("#overlay").should(disappear, timeout);
        $(byText("testiwta-store-empty")).click();
        $("#overlay").should(disappear, timeout);
        $x("//*[@id=\"group-pills\"]/li[3]/a").click();
        $("#overlay").should(disappear, timeout);
        $(byText("Import File")).click();
        $("#modal-body").should(exist);
        Thread.sleep(10000);
        try {
            File batchUpload = $("input[type='file']")
                    .shouldBe(visible)
                    .uploadFile(Paths.get(System.getProperty("user.home"), "." + System.getProperty("user.name") + "-conf", "batch-import.txt").toFile());
            $$("button").find(text("Import")).click();
            $("#modal-body > div > table > tbody").shouldHave(text(admin.uhuuid()));
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").shouldBe(visible);
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
            $("#modal-body").should(exist, timeout);
            $(byText("OK")).click();
            $("#overlay").should(disappear, timeout);
            $x("//*[@id=\"include-display\"]/div[2]").shouldHave(and("Table should have user and admin username", text(user.username()), text(admin.username())));
            $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(admin.username() + " " + user.username());
            $(byText("Remove")).click();
            $("#modal-body > div > table > tbody").shouldHave(and("Modal is visible and contains admin and user username", text(admin.username()), text(user.username())));
            $(byText("Yes")).click();
            $(byText("OK")).click();
            $("#overlay").should(disappear, timeout);
            $x("//*[@id=\"pill-content\"]").shouldNotHave(and("Admin and user should not be in include table", text(admin.username()), text(user.username())));
        } catch(NullPointerException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(14)
    public void groupingPreferences() {
        SelenideDriver browser1 = new SelenideDriver(new SelenideConfig().browser("chrome").headless(false).baseUrl(property.value("app.url.home")));

        open(property.value("url.memberships"));
        $("#memberTab > li:nth-child(2) > a").shouldBe(visible, timeout);
        $("#memberTab > li:nth-child(2) > a").click();
        $("#membership-opportunities > div.ng-scope > div.table-responsive > table > tbody > tr:nth-child(1) > td.w-35.p-10.align-middle.ng-binding").shouldBe(visible, Duration.ofSeconds(15));
        $("#optIn").setValue("testiwtb-store-empty").pressEnter();
        $(byText("No groupings are currently available.")).should(disappear, timeout);
        $("#membership-opportunities tbody").shouldBe(empty);

        browser1.open(property.value("app.url.login"));
        loginWith(browser1.getWebDriver(), user);

        browser1.open(property.value("url.groupings"));
        browser1.$(byText("testiwtb-store-empty")).click();
        browser1.$(by("id", "overlay")).should(disappear, timeout);
        browser1.$("#group-pills > li:nth-child(7) > a > i").click();
        browser1.$("#allowOptIn").shouldNotBe(selected);
        browser1.$("#allowOptOut").shouldNotBe(selected);
        browser1.$("#allowOptIn").click();
        open(property.value("url.memberships"));
        $("#memberTab > li:nth-child(2) > a").click();
        refresh();
        $(byText("Membership Opportunities")).click();
        $("#optIn").setValue("testiwtb-store-empty").pressEnter();
        $(byText("No groupings are currently available.")).should(disappear, timeout);
        $("#membership-opportunities tbody").shouldHave(text("testiwtb-store-empty"), timeout);
        $("#membership-opportunities > div.ng-scope > div.table-responsive > table > tbody > tr > td.w-8.align-middle.text-center > button").should(exist);
        $("#membership-opportunities > div.ng-scope > div.table-responsive > table > tbody > tr > td.w-8.align-middle.text-center > button").click();
        $(by("id", "overlay")).should(disappear, timeout);
        $("#memberTab > li:nth-child(1) > a").click();
        $("#current-memberships input").setValue("testiwtb-store-empty");
        $("#current-memberships tbody > tr:nth-child(1) > td.w-8.align-middle.text-center > span").shouldHave(text("Required"));
        browser1.$("#allowOptOut").click();
        open(property.value("url.memberships"));
        $(by("id", "overlay")).shouldBe(disappear, timeout);
        $("#current-memberships input").setValue("testiwtb-store-empty");
        $("#current-memberships > div.ng-scope > div.table-responsive > table > tbody > tr:nth-child(1) > td.w-8.align-middle.text-center > div > button").click();
        $(by("id", "overlay")).should(disappear, timeout);
        browser1.open(property.value("url.groupings"));
        $(by("id", "overlay")).should(disappear, timeout);
        browser1.$(byText("testiwtb-store-empty")).click();
        $(by("id", "overlay")).should(disappear, timeout);
        browser1.$("#group-pills > li:nth-child(7) > a > i").click();
        browser1.$("#allowOptIn").shouldBe(selected);
        browser1.$("#allowOptOut").shouldBe(selected);
        browser1.$("#allowOptIn").click();
        browser1.$("#allowOptOut").click();
        browser1.close();
        clearBrowserCookies();
    }
}