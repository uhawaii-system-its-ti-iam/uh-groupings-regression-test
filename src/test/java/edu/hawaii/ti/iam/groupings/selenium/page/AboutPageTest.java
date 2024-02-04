package edu.hawaii.ti.iam.groupings.selenium.page;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeborne.selenide.WebDriverRunner;

import edu.hawaii.ti.iam.groupings.selenium.core.Property;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest
public class AboutPageTest extends AbstractTestBase {

    private WebDriver driver;

    // Constructor.
    public AboutPageTest(@Autowired Property property) {
        super(property);
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
        open(property.value("url.about"));
        driver = WebDriverRunner.getWebDriver();
    }

    @AfterEach
    public void afterEach() {
        driver.close();
    }

    @Test
    public void requestGroupingsFormLink() {
        $(byText("A request form is available")).click();
        webdriver().shouldHave(
                url("https://uhawaii.atlassian.net/wiki/spaces/UHIAM/pages/13402308/UH+Groupings+Request+Form"));
    }

    @Test
    public void whatIsGroupingsLink() {
        $(byText("General information about groupings is available")).click();
        webdriver().shouldHave(url("https://uhawaii.atlassian.net/wiki/spaces/UHIAM/pages/13403213/UH+Groupings"));
    }

    @Test
    public void githubLink() {
        $(byText("GitHub")).click();
        webdriver().shouldHave(url("https://github.com/uhawaii-system-its-ti-iam/uh-groupings-ui"));
    }

    @Test
    public void mavenLink() {
        $(byText("(Introduction)")).click();
        webdriver().shouldHave(url("https://maven.apache.org/what-is-maven.html"));
    }

    @Test
    public void tomcatLink() {
        $(byText("(Welcome)")).click();
        webdriver().shouldHave(url("https://tomcat.apache.org/tomcat-8.5-doc/index.html"));
    }

    @Test
    public void springFrameworkGuideLink() {
        $("body > main > div.container.mb-5 > div > div.col-md-9 > div > div:nth-child(1) > p:nth-child(3) > a:nth-child(1)").click();
        webdriver().shouldHave(
                url("https://docs.spring.io/spring-framework/docs/5.2.3.RELEASE/spring-framework-reference/"));
    }

    @Test
    public void springFrameworkApiLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[1]/p[3]/a[2]").click();
        webdriver().shouldHave(url("https://docs.spring.io/spring-framework/docs/5.2.3.RELEASE/javadoc-api/"));
    }

    @Test
    public void springBootLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[1]/p[4]/a").click();
        webdriver().shouldHave(url("https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/htmlsingle/"));
    }

    @Test
    public void springSecuirityLink() {
        $(byText("(Quickstart)")).click();
        webdriver().shouldHave(url("https://spring.io/projects/spring-security"));
    }

    @Test
    public void thymeleafLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[2]/p[2]/a").click();
        webdriver().shouldHave(
                url("https://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html#introducing-thymeleaf"));
    }

    @Test
    public void bootstrapLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[2]/p[3]/a").click();
        webdriver().shouldHave(url("https://getbootstrap.com/docs/4.5/getting-started/introduction/"));
    }

    @Test
    public void angularJSLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[2]/p[4]/a[1]").click();
        webdriver().shouldHave(url("https://code.angularjs.org/1.7.8/docs/guide"));
    }

    @Test
    public void jQueryCurrentVersionLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[3]/p[1]/a[1]").click();
        webdriver().shouldHave(url("https://api.jquery.com/category/version/3.5/"));
    }

    @Test
    public void jQueryApiLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[3]/p[1]/a[2]").click();
        webdriver().shouldHave(url("https://api.jquery.com/"));
    }

    @Test
    public void jasmineCurrentVersionLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[3]/p[2]/a[1]").click();
        webdriver().shouldHave(url("https://github.com/jasmine/jasmine/releases/tag/v3.7.1"));
    }

    @Test
    public void jasmineGuideLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[3]/p[2]/a[2]").click();
        webdriver().shouldHave(url("https://jasmine.github.io/pages/getting_started.html"));
    }

    @Test
    public void jenkinsLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[3]/p[3]/a").click();
        webdriver().shouldHave(url("https://www.jenkins.io/doc/"));
    }

    @Test
    public void underscoreJSLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[3]/p[4]/a").click();
        webdriver().shouldHave(url("https://underscorejs.org/"));
    }
}
