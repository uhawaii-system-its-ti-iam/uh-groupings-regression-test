package edu.hawaii.ti.iam.groupings.selenium.page;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeborne.selenide.WebDriverRunner;

import edu.hawaii.ti.iam.groupings.selenium.core.Property;
import edu.hawaii.ti.iam.groupings.selenium.core.User;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest
public class LoggedInUserTest {

    private static final Log logger = LogFactory.getLog(HomePageTest.class);

    private final Property property;
    private WebDriver driver;

    // Constructor.
    public LoggedInUserTest(@Autowired Property property) {
        this.property = property;
    }

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        WebDriverRunner.setWebDriver(new ChromeDriver());
        //        WebDriverManager.firefoxdriver().setup();
        //        WebDriverRunner.setWebDriver(new FirefoxDriver());
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
                .username(property.value("student.user.username"))
                .password(property.value("student.user.password"))
                .firstname(property.value("student.user.firstname"))
                .uhnumber(property.value("student.user.uhnumber"))
                .build();
        assertThat(user.getUsername(), not(equalTo("SET-IN-OVERRIDES")));

        $("#username").val(user.getUsername());
        $("#password").val(user.getPassword());
        $x("//*[@id=\"login-form-controls\"]/button").click();

    }

    @AfterEach
    public void afterEach() {
        driver.close();
    }

    @Test
    public void navBarAbout() {
        final String urlFull = property.value("url.about");
        final String urlRel = property.value("url.relative.about");
        $x("//a[@href='/" + urlRel + "']").click();
        $x("/html/body/main/div[1]/div/div[1]/div[1]/h1").shouldHave(text("What is a UH Grouping?"));
        webdriver().shouldHave(url(urlFull));
    }

    @Test
    public void navBarLogoutButtonText() {
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[5]/form/button").shouldHave(text("Logout (" + property.value("student.user.username") + ")"));
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[5]/form/button").click();
        $x("/html/body/div/nav/div/div/ul/li[2]/a").shouldHave(text("Login"));
    }

    @Test
    public void groupingsIcon() {
        $x("//img[@alt='UH Groupings Logo']").click();
        webdriver().shouldHave(url(property.value("app.url.home") + "/")); // Inconsistent ending use.
        $x("//p[@class='lead'][text()='Manage your groupings in one place, use them in many.']").should(exist);
    }

    @Test
    public void equalOpportunity() {
        $x("//a[text()='equal opportunity/affirmative action institution']").click();
        webdriver().shouldHave(url(property.value("url.policy")));
    }

    @Test
    public void usagePolicyTest() throws Exception {
        File downloadedFile = $("a[href*='ep2.210.pdf']").download();
        Path path = Paths.get(downloadedFile.getPath());
        InputStream downloadedPDF = Files.newInputStream(path);
        String downloadedHash = DigestUtils.sha1Hex(downloadedPDF);
        InputStream expectedPDF = Files.newInputStream(path);
        String expectedHash = DigestUtils.sha1Hex(expectedPDF);
        assertThat(expectedHash, equalTo(downloadedHash));

        WebElement field = driver.findElement(By.linkText("Usage Policy"));
        assertThat(field.getText(), equalTo("Usage Policy"));
        assertThat(field.getAttribute("href"), endsWith("ep2.210.pdf"));
    }

    @Test
    public void adminPageTest() {
        open(property.value("url.admin"));
        WebElement statusCode = driver.findElement(By.xpath("/html/body/div[2]/div/div/p[2]/span[1]"));
        WebElement statusLabel = driver.findElement(By.xpath("/html/body/div[2]/div/div/p[2]/span[2]"));
        assertThat(statusCode.getText(), equalTo("403"));
        assertThat(statusLabel.getText(), equalTo("(Forbidden)"));

    }

    @Test
    public void navbarMembershipsTest() {
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[1]/a").click();
        webdriver().shouldHave(url(property.value("url.memberships")));
    }

    @Test
    public void navbarGroupingsTest() {
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[2]/a").click();
        webdriver().shouldHave(url(property.value("url.groupings")));
    }

    @Test
    public void navbarAboutTest() {
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[3]/a").click();
        webdriver().shouldHave(url(property.value("url.about")));

    }

    @Test
    public void navbarFeedbackTest() {
        $x("//*[@id=\"navbarSupportedContent\"]/ul/li[4]/a").click();
        webdriver().shouldHave(url(property.value("url.feedback")));
    }

    @Test
    public void membershipsButtonTest() {
        $x("/html/body/main/div[3]/div[2]/div/div/div[1]/div[2]/a").click();
        webdriver().shouldHave(url(property.value("url.memberships")));
    }

    @Test
    public void groupingsButtonTest() {
        $x("/html/body/main/div[3]/div[2]/div/div/div[2]/div[2]/a").click();
        webdriver().shouldHave(url(property.value("url.groupings")));
    }

    @Test
    public void welcomeMessageTest() {
        $x("/html/body/main/div[3]/div[1]/div/div/div[2]/h1").shouldHave(text("Welcome, " + property.value("student.user.firstname") +"!" ));
        $x("/html/body/main/div[3]/div[1]/div/div/div[2]/div/h1").shouldHave(text("Role: Owner"));
    }

    @Test
    public void announcementTest() {
        $x("/html/body/main/div[1]/div/div/div").shouldBe(visible);
        $x("/html/body/main/div[1]/div/div/div/button/span").click();
        $x("/html/body/main/div[1]/div/div/div").shouldNotBe(visible);
    }

}