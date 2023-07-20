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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoggedInAdminTest {
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
//        Configuration.proxyEnabled = true;
        Configuration.fileDownload = FileDownloadMode.FOLDER;
        Configuration.downloadsFolder = System.getProperty("user.dir") + File.separator + "downloadFile";
        SelenideLogger.addListener("allure", new AllureSelenide());
        admin.loggingIn();
    }

    @BeforeEach
    public void setUp() {
            open(admin.baseURL + "login");
    }
    @AfterAll
    public static void close() {
        closeWebDriver();
    }
    @Test
    public void navBarLogin() {
        navBarLogoutButton();
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[2]/a").click();
        $x("/html/body/main/div[2]/div[1]/div/div/div[2]/h1/span").shouldBe(text(admin.firstName));
    }
    @Test
    public void navBarInfo() {
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[4]/a").click();
        $x("/html/body/main/div[1]/div/div[1]/div[1]/h1").shouldHave(text("What is a UH Grouping?"));
        webdriver().shouldHave(url(admin.baseURL + "info"));
    }

    @Test
    public void navBarLogoutButton() {
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[6]/form/button").shouldHave(text("Logout (" + admin.username +")"));
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[6]/form/button").click();
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[2]/a").shouldBe(visible);
    }
    @Test
    public void logoutButton() {
        $x("/html/body/main/div[2]/div[1]/div/div/div[2]/h1/span").shouldBe(text(admin.firstName));
        $x("/html/body/main/div[1]/div/div[1]/div/div/form/button").click();
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[2]/a").shouldBe(visible);
    }


    @Test
    public void groupingsIcon() {
        $x("//img[@alt='UH Groupings Logo']").click();
        webdriver().shouldHave(url(admin.baseURL + ""));
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
    public void navBarAdmin() {
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[1]/a").click();
        webdriver().shouldHave(url(admin.baseURL + "admin"));
    }
    @Test
    public void navBarMemberships() {
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[2]/a").click();
        webdriver().shouldHave(url(admin.baseURL + "memberships"));
    }
    @Test
    public void navBarGroupings() {
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[3]/a").click();
        webdriver().shouldHave(url(admin.baseURL + "groupings"));
    }

    @Test
    public void navBarFeedback() {
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[5]/a").click();
        webdriver().shouldHave(url(admin.baseURL + "feedback"));
    }

    @Test
    public void adminButton() {
        $("#navbarSupportedContent > ul > li:nth-child(1) > a").click();
        webdriver().shouldHave(url(admin.baseURL + "admin"));
    }

    @Test
    public void membershipsButton() {
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[2]/a").click();
        webdriver().shouldHave(url(admin.baseURL + "memberships"));
    }

    @Test
    public void groupingsButton() {
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[3]/a").click();
        webdriver().shouldHave(url(admin.baseURL + "groupings"));
    }

    @Test
    public void homePageFirstNameTest() {
        $x("/html/body/main/div[2]/div[1]/div/div/div[2]/h1/span").shouldBe(text(admin.firstName));
    }
}
