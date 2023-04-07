package SeleniumTest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.opencsv.CSVWriter;
import io.qameta.allure.selenide.AllureSelenide;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;

import static com.codeborne.selenide.Condition.and;
import static com.codeborne.selenide.Condition.appear;
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
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IdleLogoutTest {
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
//        Configuration.proxyEnabled = true;
//        Configuration.fileDownload = FileDownloadMode.FOLDER;
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
    public void idleLogout() {
        Duration idleModal = Duration.ofSeconds(25 * 60 + 30);
        Duration logoutTimer = Duration.ofSeconds(5 * 60 + 30);
        $("body > div.modal.fade.ng-scope.ng-isolate-scope.in > div > div").should(appear, idleModal);
        $(byText("Login Here")).should(exist, logoutTimer);
    }
}
