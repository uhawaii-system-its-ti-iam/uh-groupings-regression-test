package SeleniumTest;

import com.codeborne.selenide.*;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.apache.commons.io.FileUtils;
import org.apache.commons.codec.digest.DigestUtils;
import com.opencsv.CSVWriter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GroupingSelectionTest {
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
        Configuration.browser = "firefox";
        Configuration.browserSize = "1280x800";
        Configuration.headless = false;
        // Configuration.proxyEnabled = true;
        // Configuration.browser = "firefox";
        Configuration.fileDownload = FileDownloadMode.FOLDER;
        Configuration.downloadsFolder = System.getProperty("user.dir") + File.separator + "downloadFile";
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
        $(byText("testiwta-many")).click();
        $x("//*[@id=\"sel\"]/div").shouldBe(visible);
    }

    @Test
    @Order(1)
    public void groupingName() {
        $x("//*[@id=\"groupNameCol\"]/h2").shouldBe(text("testiwta-many"));
    }
    @Test
    @Order(2)
    public void groupingPath() {
        $(byText("tmp:testiwta:testiwta-many")).shouldBe(visible);
    }
    @Test
    @Order(3)
    public void groupingDescription() {
        $x("//*[@id=\"sel\"]/div/section[1]/div/div[3]/div/div[1]/p").shouldHave(text("Test Many Groups In Basis"));
    }
    @Test
    @Order(4)
    public void returnToGroupingsButton() {
        $(byText("Return to Groupings List")).click();
        $x("//*[@id=\"sel\"]/div").should(disappear);
        $x("//*[@id=\"manage-groupings\"]/div[2]").shouldBe(visible);
    }
    @Test
    @Order(5)
    public void filterMembers() {
        $x("//*[@id=\"all\"]/div[1]/div[2]/input").setValue("kl");
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
        $$x("//*[@id=\"all\"]/div[2]/table/tbody/tr").asFixedIterable().forEach(row -> row.shouldHave(text("kl")));
    }
    @Test
    @Order(6)
    public void exportMembersCSV() {
        $("#csvButton > button").shouldBe(not(disabled), user.timeout);
//        $(byText("Export Members")).click();
        $("#csvButton > div > button:nth-child(1)").shouldBe(interactable);
        try {
            File downloadedFile = $("#csvButton > div.d-flex.ng-isolate-scope > button").download();
            String path = downloadedFile.getPath();
            try {
                String expectedValue = writeCSV(user.username);
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
        } catch (FileNotFoundException e) {
            System.out.println("Could download file to path");
            throw new RuntimeException(e);
        }
        try {
            FileUtils.deleteDirectory(new File(System.getProperty("user.dir") + File.separator + "downloadFile"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(7)
    public void exportBasisCSV() {
        $("#csvButton > button").shouldBe(not(disabled), user.timeout);
        $("#csvButton > button").click();
        $("#csvButton > ul > li:nth-child(1) > label").shouldBe(interactable);
        try {
            File downloadedFile = $("#csvButton > ul > li:nth-child(1) > label").download();
            $("#group-pills > li:nth-child(2) > a").click();
            String path = downloadedFile.getPath();
            try {
                String expectedValue = writeCSV(user.username);
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
        } catch (FileNotFoundException e) {
            System.out.println("Could download file to path");
            throw new RuntimeException(e);
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
        Condition clickable = and("can be clicked", visible, enabled);
        File csvFile = new File(System.getProperty("user.dir") + "/downloadFile/" + username + "-many_members_list.csv");
        FileWriter outputFile = new FileWriter(csvFile);
        CSVWriter writer = new CSVWriter(outputFile, ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.RFC4180_LINE_END);
        String[] header = {"Last", "First", "Username", "UH Number", "Email"};
        writer.writeNext(header);
        csvStuff.add(header);
        SelenideElement nextButton = $$(byText("Next")).filterBy(clickable).first().parent().parent();

        while(true) {
            $("#basis-display > div.row > div.col-md-8 > div").shouldNotBe(visible, admin.timeout);
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
        open(user.baseURL + "groupings");
        $(byText("testiwta-store-empty")).click();
        $x("//*[@id=\"group-pills\"]/li[3]/a").click();
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(admin.username).pressEnter();
        $x("/html/body/div[1]/div/div").shouldBe(visible);
        $x("//*[@id=\"modal-body\"]/table/tbody/tr[2]/th[2]").shouldHave(text(admin.username));
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
        $x("/html/body/div[1]/div/div").shouldBe(visible);
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
        $("#modal-body").shouldHave(text(admin.firstName), user.timeout);
        $(byText("OK")).click();
        removeMemberTextBox(admin.username);
    }

    @Test
    @Order(9)
    public void addRemoveMembersUhNumber() {
        open(user.baseURL + "groupings");
        $(byText("testiwta-store-empty")).click();
        $x("//*[@id=\"group-pills\"]/li[3]/a").click();
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(admin.uhNumber).pressEnter();
        $x("/html/body/div[1]/div/div").shouldBe(visible);
        $("#modal-body > table > tbody").shouldHave(text(admin.uhNumber));
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
        $x("/html/body/div[1]/div/div").shouldBe(visible);
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
        $("#modal-body").shouldHave(text(admin.firstName));
        $(byText("OK")).click();
        removeMemberTextBox(admin.uhNumber);
    }

    @Test
    @Order(10)
    public void removeMemberTrashCan() {
        open(user.baseURL + "groupings");
        $(byText("testiwta-store-empty")).click();
        $x("//*[@id=\"group-pills\"]/li[3]/a").click();
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(admin.uhNumber).pressEnter();
        $x("/html/body/div[1]/div/div").shouldBe(visible);
        $("#modal-body > table > tbody").shouldHave(text(admin.uhNumber));
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
        $x("/html/body/div[1]/div/div").shouldBe(visible);
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
        $("#modal-body").shouldHave(text(admin.firstName));
        $(byText("OK")).click();
        $("#include-display input[title=\"Filter Members\"]").setValue(admin.username);
        $("span[class=\"far fa-trash-alt fa-pull-right clickable pt-1 ng-isolate-scope\"]").click();
        $("#modal-body").shouldBe(visible);
        $("#modal-body").$("tbody").shouldHave(text(admin.uhNumber));
        $(byText("Yes")).click();
        $("#modal-body").shouldHave(text(admin.firstName));
        $(byText("OK")).click();
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
        $("#include-display tbody").shouldNotHave(text(admin.username));
    }

//    @Test
    public void removeMemberTextBox(String userInfo) {
        open(user.baseURL + "groupings");
        $x("//*[@id=\"overlay\"]").should(disappear);
        $(byText("testiwta-store-empty")).click();
        $x("//*[@id=\"group-pills\"]/li[3]/a").click();
        $("#include-display > div.table-responsive-sm > table > tbody").shouldHave(text(userInfo));
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(userInfo);
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memBtns > button.btn.btn-remove").click();
        $x("/html/body/div[1]/div/div").shouldBe(visible);
        $("#modal-body").shouldHave(text(userInfo));
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
        $(byText("OK")).click();
        $x("//*[@id=\"overlay\"]").should(disappear);
        $(byText(admin.username)).shouldNot(exist);
    }
    @Test
    @Order(11)
    public void multiAddMembersUsername() {
        open(user.baseURL + "groupings");
        $x("//*[@id=\"overlay\"]").should(disappear, admin.timeout);
        $(byText("testiwta-store-empty")).click();
        $x("//*[@id=\"group-pills\"]/li[3]/a").click();
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(admin.username + " " + user.username).pressEnter();
        $("#modal-body").shouldBe(visible);
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
        $x("//*[@id=\"overlay\"]").should(disappear);
        $(byText("OK")).click();
        $x("//*[@id=\"pill-content\"]").shouldHave(and("Admin and user first name should be in the include table", text(admin.firstName), text(user.firstName)));
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(admin.username + " " + user.username);
        $(byText("Remove")).click();
        $x("//*[@id=\"modal-body\"]").shouldHave(and("Modal is visible and contains admin and user username", text(admin.username), text(user.username)));
        $(byText("Yes")).click();
        $(byText("OK")).click();
        $x("//*[@id=\"overlay\"]").should(disappear, admin.timeout);
        $x("//*[@id=\"pill-content\"]").shouldNotHave(and("Admin and user should not be in include table", text(admin.username), text(user.username)));
    }
    @Test
    @Order(12)
    public void multiAddMembersUhNumber() {
        open(user.baseURL + "groupings");
        $x("//*[@id=\"overlay\"]").should(disappear, admin.timeout);
        $(byText("testiwta-store-empty")).click();
        $x("//*[@id=\"group-pills\"]/li[3]/a").click();
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(admin.uhNumber + " " + user.uhNumber).pressEnter();
        $("#modal-body").shouldBe(visible);
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
        $x("//*[@id=\"overlay\"]").should(disappear);
        $(byText("OK")).click();
        $x("//*[@id=\"pill-content\"]").shouldHave(and("Admin and user first name should be in the include table", text(admin.firstName), text(user.firstName)));
        $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(admin.uhNumber + " " + user.uhNumber);
        $(byText("Remove")).click();
        $x("//*[@id=\"modal-body\"]").shouldHave(and("Modal is visible and contains admin and user username", text(admin.username), text(user.username)));
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
        $(byText("OK")).click();
        $x("//*[@id=\"overlay\"]").should(disappear, admin.timeout);
        $x("//*[@id=\"pill-content\"]").shouldNotHave(and("Admin and user should not be in include table", text(admin.username), text(user.username)));
    }
    @Test
    public void batchImport() {
        open(user.baseURL + "groupings");
        $x("//*[@id=\"overlay\"]").should(disappear, user.timeout);
        $(byText("testiwta-store-empty")).click();
        $x("//*[@id=\"overlay\"]").should(disappear, user.timeout);
        $x("//*[@id=\"group-pills\"]/li[3]/a").click();
        $x("//*[@id=\"overlay\"]").should(disappear, user.timeout);
        $(byText("Import File")).click();
        $("#modal-body").should(exist);
        try {
            File batchUpload = $("input[type=file]").uploadFile(new File(System.getProperty("user.home")+ File.separator + "." + System.getProperty("user.name") + "-conf" + File.separator + "batch-import.txt"));
            $$("button").find(text("Import")).click();
            $("#modal-body > div > table > tbody").shouldHave(text(admin.uhNumber));
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").shouldBe(visible);
            $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div > div.modal-footer.ng-scope > button.btn.btn-primary").click();
            $("#modal-body").should(exist, user.timeout);
            $(byText("OK")).click();
            $x("//*[@id=\"overlay\"]").should(disappear, admin.timeout);
            $x("//*[@id=\"include-display\"]/div[2]").shouldHave(and("Table should have user and admin username", text(user.username), text(admin.username)));
            $("#include-display > div.d-lg-flex.d-block.justify-content-lg-between.justify-content-start > div > form > div > div.memSearch > input").setValue(admin.username + " " + user.username);
            $(byText("Remove")).click();
            $("#modal-body > div > table > tbody").shouldHave(and("Modal is visible and contains admin and user username", text(admin.username), text(user.username)));
            $(byText("Yes")).click();
            $(byText("OK")).click();
            $x("//*[@id=\"overlay\"]").should(disappear, admin.timeout);
            $x("//*[@id=\"pill-content\"]").shouldNotHave(and("Admin and user should not be in include table", text(admin.username), text(user.username)));
        } catch(NullPointerException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(13)
    public void groupingPreferences() {
        closeWebDriver();
        SelenideDriver browser1 = new SelenideDriver(new SelenideConfig().browser("chrome").headless(false).baseUrl(admin.baseURL));
        open(admin.baseURL);
        admin.loggingIn();
        open(admin.baseURL + "memberships");
        $("#memberTab > li:nth-child(2) > a").shouldBe(visible, admin.timeout);
        $("#memberTab > li:nth-child(2) > a").click();
        $("#optIn").setValue("testiwta-store-empty").pressEnter();
        $(byText("No groupings are currently available.")).should(disappear, admin.timeout);
        $("#membership-opportunities tbody").shouldBe(empty);
        user.loggingInNoDuoAuth(browser1);
        browser1.open("groupings");
        browser1.$(byText("testiwta-store-empty")).click();
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
        browser1.$("#group-pills > li:nth-child(7) > a > i").click();
        browser1.$("#allowOptIn").shouldNotBe(selected);
        browser1.$("#allowOptOut").shouldNotBe(selected);
        browser1.$("#allowOptIn").click();
        open(admin.baseURL + "memberships");
        $("#memberTab > li:nth-child(2) > a").click();
//        sleep(10000);
        refresh();
        $(byText("Membership Opportunities")).click();
        $("#optIn").setValue("testiwta-store-empty").pressEnter();
        $(byText("No groupings are currently available.")).should(disappear, admin.timeout);
        $("#membership-opportunities tbody").shouldHave(text("testiwta-store-empty"), admin.timeout);
        $("#membership-opportunities > div.ng-scope > div.table-responsive > table > tbody > tr > td.w-8.align-middle.text-center > button").should(exist);
        $("#membership-opportunities > div.ng-scope > div.table-responsive > table > tbody > tr > td.w-8.align-middle.text-center > button").click();
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
        $("#memberTab > li:nth-child(1) > a").click();
        $("#current-memberships input").setValue("testiwta-store-empty");
        $("#current-memberships tbody > tr:nth-child(1) > td.w-8.align-middle.text-center > span").shouldHave(text("Required"));
        browser1.$("#allowOptOut").click();
        open(admin.baseURL + "memberships");
        $("#current-memberships input").setValue("testiwta-store-empty");
        $("#current-memberships > div.ng-scope > div.table-responsive > table > tbody > tr:nth-child(1) > td.w-8.align-middle.text-center > div > button").click();
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
        browser1.open("groupings");
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
        browser1.$(byText("testiwta-store-empty")).click();
        $x("//*[@id=\"overlay\"]/div/div").should(disappear, user.timeout);
        browser1.$("#group-pills > li:nth-child(7) > a > i").click();
        browser1.$("#allowOptIn").shouldBe(selected);
        browser1.$("#allowOptOut").shouldBe(selected);
        browser1.$("#allowOptIn").click();
        browser1.$("#allowOptOut").click();
        browser1.close();
        clearBrowserCookies();
        closeWebDriver();
        user.loggingInNoDuoAuth();
    }
}
