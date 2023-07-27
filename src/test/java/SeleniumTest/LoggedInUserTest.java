package SeleniumTest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static com.codeborne.selenide.Selectors.byText;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoggedInUserTest {
    static MainPage user = new MainPage();
    // WebDriver driver = new ChromeDriver();
    // Configuration config = new Configuration();

    @BeforeAll
    public static void setUpAll() {
        try {
            user.getUserCredentials();
        } catch(Exception e) {
            System.out.println("not found");
        }
//        Configuration.proxyEnabled = true;
        Configuration.fileDownload = FileDownloadMode.FOLDER;
        Configuration.downloadsFolder = System.getProperty("user.dir") + File.separator + "downloadFile";
        Configuration.browserSize = "1280x800";
        Configuration.headless = false;
        SelenideLogger.addListener("allure", new AllureSelenide());
        user.loggingInNoDuoAuth();
    }

    @BeforeEach
    public void setUp() {
            open(user.baseURL + "login");
    }
    @AfterAll
    public static void close() {
        closeWebDriver();
    }
    @Test
    public void navBarLogin() {
        navBarLogoutButton();
        $x("//a[contains(@href, '/uhgroupings/login') and text() = 'Login ']").click();
        $x("/html/body/main/div[2]/div[1]/div/div/div[2]/h1/span").shouldBe(text(user.firstName));
    }
    @Test
    public void navBarInfo() {
        $x("//a[contains(@class, 'nav-link') and text() = 'Info']").click();
        $(byText("What is a UH Grouping?")).shouldBe(visible);
        webdriver().shouldHave(url(user.baseURL + "info"));
    }

    @Test
    public void navBarLogoutButton() {
        $x("//button[contains(@class, 'btn-outline-secondary')]").shouldHave(text("Logout (" + user.username +")"));
        $x("//button[contains(@class, 'btn-outline-secondary')]").click();
        $x("//a[contains(@class, 'login')]").shouldBe(visible);
    }
    @Test
    public void logoutButton() {
        $x("/html/body/main/div[2]/div[1]/div/div/div[2]/h1/span").shouldBe(text(user.firstName));
        $x("/html/body/main/div[1]/div/div[1]/div/div/form/button").click();
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[2]/a").shouldBe(visible);
    }


    @Test
    public void groupingsIcon() {
        $x("//img[@alt='UH Groupings Logo']").click();
        webdriver().shouldHave(url(user.baseURL + ""));
        $x("//p[@class='lead'][text()='Manage your groupings in one place, use them in many.']");
    }
    @Test
    public void equalOpportunity() {
        $x("//a[text()='equal opportunity/affirmative action institution']").click();
        webdriver().shouldHave(url("https://www.hawaii.edu/offices/eeo/policies/?policy=antidisc"));
    }

    @Test
    public void usagePolicyTest() {
        try {
            File downloadedFile = $("#footer-top > div > div > div.col-md-8.col-sm-7 > p:nth-child(2) > a").download();
            String path = downloadedFile.getPath();
            InputStream downloadedPDF = Files.newInputStream(Paths.get(path));
            String downloadedHash = DigestUtils.sha1Hex(downloadedPDF);
            InputStream expectedPDF = Files.newInputStream(Paths.get(path));
            String expectedHash = DigestUtils.sha1Hex(expectedPDF);
            assertEquals(expectedHash, downloadedHash);
            FileUtils.deleteDirectory(new File(System.getProperty("user.dir") + File.separator + "downloadFile"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void navBarMemberships() {
        $x("//a[contains(@class, 'nav-link') and text() = 'Memberships']").click();
        webdriver().shouldHave(url(user.baseURL + "memberships"));
        $x("//table[contains(@class, 'manage-groupings')]").shouldBe(visible);
    }
    @Test
    public void navBarGroupings() {
        $x("//a[contains(@class, 'nav-link') and text() = 'Groupings']").click();
        webdriver().shouldHave(url(user.baseURL + "groupings"));
    }

    @Test
    public void navBarFeedback() {
        $x("//a[contains(@class, 'nav-link') and text()='Feedback']").click();
        webdriver().shouldHave(url(user.baseURL + "feedback"));
        $x("//form[contains(@action, '/uhgroupings/feedback')]").shouldBe(visible);
    }


    @Test
    public void membershipsButton() {
        $x("//a[contains(@class, 'btn') and text() = 'Memberships']").click();
        webdriver().shouldHave(url(user.baseURL + "memberships"));
        $x("//table[contains(@class, 'manage-groupings')]").shouldBe(visible);
    }

    @Test
    public void groupingsButton() {
        $x("//a[contains(@class, 'btn') and text() = 'Groupings']").click();
        webdriver().shouldHave(url(user.baseURL + "groupings"));
        $(byText("Manage My Groupings")).shouldBe(visible);
    }

    @Test
    public void adminPage() {
        open(user.baseURL + "admin");
        $(byText("403")).shouldBe(visible);
    }

    @Test
    public void homePageFirstName() {
        $x("/html/body/main/div[2]/div[1]/div/div/div[2]/h1/span").shouldBe(text(user.firstName));
    }

}
