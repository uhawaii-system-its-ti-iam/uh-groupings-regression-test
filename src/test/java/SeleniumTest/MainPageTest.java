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
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.*;

public class MainPageTest {
    static MainPage admin = new MainPage();

    @BeforeAll
    public static void setUpAll() {
        try {
            admin.getAdminCredentials();
        } catch(Exception e) {
            System.out.println("not found");
        }
//        Configuration.proxyEnabled = true;
        Configuration.fileDownload = FileDownloadMode.FOLDER;
        Configuration.downloadsFolder = System.getProperty("user.dir") + File.separator + "downloadFile";
        Configuration.browserSize = "1280x800";
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        open(admin.baseURL);
    }
    @AfterAll
    public static void close() {
        closeWebDriver();
    }

    @Test
    public void navBarInfo() {
        $x("//a[@href='/uhgroupings/info']").click();
        $x("/html/body/main/div[1]/div/div[1]/div[1]/h1").shouldHave(text("What is a UH Grouping?"));
        webdriver().shouldHave(url(admin.baseURL + "info"));
    }
    @Test
    public void learnMoreButton() {
        $x("//a[@role='button'][@href='https://uhawaii.atlassian.net/wiki/spaces/UHIAM/pages/13403213/UH+Groupings']").click();
        webdriver().shouldHave(url("https://uhawaii.atlassian.net/wiki/spaces/UHIAM/pages/13403213/UH+Groupings"));
    }
    @Test
    public void navBarLogin() {
        $x("//a[@href='/uhgroupings/login']").click();
        $("#uh-seal").shouldBe(visible);
        webdriver().shouldHave(url("https://cas-test.its.hawaii.edu/cas/login?service=https%3A%2F%2Fwww.test.hawaii.edu%2Fuhgroupings%2Flogin%2Fcas"));
    }
    @Test
    public void loginHereButton() {
        $x("//button[@class='btn btn-lg dark-teal-bg'][text()='Login Here']").click();

    }

    @Test
    public void groupingsIcon() {
        $x("//img[@alt='UH Groupings Logo']").click();
        webdriver().shouldHave(url(admin.baseURL));
        $x("//p[@class='lead'][text()='Manage your groupings in one place, use them in many.']").should(exist);
    }

    @Test
    public void equalOpportunity() {
        $x("//a[text()='equal opportunity/affirmative action institution']").click();
        webdriver().shouldHave(url("https://www.hawaii.edu/offices/eeo/policies/?policy=antidisc"));
    }
    //@Test
    public void loggingIn() {
        navBarLogin();
        $("#username").val(admin.username);
        $("#password").val(admin.password);
        $x("//button[@name='submit']").click();
        switchTo().frame($("#duo_iframe"));
        $("#username").should(disappear);
        $("button").should(appear);
        $x("//button[text()='Send Me a Push ']").click();
        $x("//h2[text()='Choose an authentication method']").should(disappear);
        $x("//span[text()='treyyy']").shouldBe(visible);
        closeWebDriver();
    }

    @Test
    public void usagePolicyTest() {
        try {
            File downloadedFile = $("#footer-top > div > div > div.col-md-8.col-sm-7 > p:nth-child(2) > a").download();
            String path = downloadedFile.getPath();
            InputStream downloadedPDF = Files.newInputStream(Paths.get(path));
            String downloadedHash = DigestUtils.sha1Hex(downloadedPDF);
            InputStream expectedPDF = Files.newInputStream(Paths.get(System.getProperty("user.dir") + "/ep2.210.pdf"));
            String expectedHash = DigestUtils.sha1Hex(expectedPDF);
            assertEquals(expectedHash, downloadedHash);
            FileUtils.deleteDirectory(new File(System.getProperty("user.dir") + File.separator + "downloadFile"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
