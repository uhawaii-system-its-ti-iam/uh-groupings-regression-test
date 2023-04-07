package SeleniumTest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;

public class InfoPageLinksTest {
    // WebDriver driver = new ChromeDriver();
    // Configuration config = new Configuration();
    static Duration timeout = Duration.ofSeconds(40);
    static MainPage user = new MainPage();
    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
        //Configuration.browser = "safari";
        SelenideLogger.addListener("allure", new AllureSelenide());
        Configuration.baseUrl = user.baseURL;
    }

    @BeforeEach
    public void setUp() {
        open("info");
        $x("/html/body/main/div[1]/div/div[1]/div/div/form/button").should(disappear, timeout);
    }
    @AfterAll
    public static void close() {
        closeWebDriver();
    }
    @Test
    public void requestGroupingsFormLink() {
        $x("/html/body/main/div[2]/div/div[1]/p[1]/a").click();
        webdriver().shouldHave(url("https://uhawaii.atlassian.net/wiki/spaces/UHIAM/pages/13402308/UH+Groupings+Request+Form"));
    }
    @Test
    public void whatIsGroupingsLink() {
        $x("/html/body/main/div[2]/div/div[1]/p[2]/a").click();
        webdriver().shouldHave(url("https://uhawaii.atlassian.net/wiki/spaces/UHIAM/pages/13403213/UH+Groupings"));
    }
    @Test
    public void githubLink() {
        $x("/html/body/main/div[4]/div/div[1]/p/a").click();
        webdriver().shouldHave(url("https://github.com/uhawaii-system-its-ti-iam/uh-groupings-ui"));
    }
    @Test
    public void mavenLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[1]/p[1]/a").click();
        webdriver().shouldHave(url("https://maven.apache.org/what-is-maven.html"));
    }
    @Test
    public void tomcatLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[1]/p[2]/a").click();
        webdriver().shouldHave(url("https://tomcat.apache.org/tomcat-8.5-doc/index.html"));
    }
    @Test
    public void springFrameworkGuideLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[1]/p[3]/a[1]").click();
        webdriver().shouldHave(url("https://docs.spring.io/spring-framework/docs/5.2.3.RELEASE/spring-framework-reference/"));
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
        $x("/html/body/main/div[4]/div/div[2]/div/div[2]/p[1]/a").click();
        webdriver().shouldHave(url("https://spring.io/projects/spring-security"));
    }
    @Test
    public void thymeleafLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[2]/p[2]/a").click();
        webdriver().shouldHave(url("https://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html#introducing-thymeleaf"));
    }
    @Test
    public void bootstrapLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[2]/p[3]/a").click();
        webdriver().shouldHave(url("https://getbootstrap.com/docs/4.5/getting-started/introduction/"));
    }
    @Test
    public void angularJSGuideLink() {
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
    public void jenkinsDocumentationLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[3]/p[3]/a").click();
        webdriver().shouldHave(url("https://www.jenkins.io/doc/"));
    }
    @Test
    public void underscoreJSLink() {
        $x("/html/body/main/div[4]/div/div[2]/div/div[3]/p[4]/a").click();
        webdriver().shouldHave(url("https://underscorejs.org/"));
    }
}
