package SeleniumTest;

import com.codeborne.selenide.SelenideDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.switchTo;

// page_url = https://www.jetbrains.com/
public class MainPage {
    public String baseURL = "https://www.test.hawaii.edu/uhgroupings/";
    public String username;
    public String password;
    public String firstName;
    public String uhNumber;
    public String emailHost;
    public String mailStoreType;
    public Duration timeout = Duration.ofSeconds(40);
    public void getAdminCredentials() throws IOException {
        Properties properties = new Properties();
        FileInputStream inputStream = new FileInputStream(System.getProperty("user.home")+ File.separator + "." + System.getProperty("user.name") + "-conf" + File.separator + "regression-tests-overrides.properties");
        properties.load(inputStream);
        username = properties.getProperty("adminUsername");
        password = properties.getProperty("adminPassword");
        firstName = properties.getProperty("adminFirstName");
        uhNumber = properties.getProperty("adminUhNumber");
        emailHost = properties.getProperty("emailHost");
        mailStoreType = properties.getProperty("mailStoreType");
        inputStream.close();
    }
    public void getUserCredentials() throws IOException {
        Properties properties = new Properties();
        FileInputStream inputStream = new FileInputStream(System.getProperty("user.home")+ File.separator + "." + System.getProperty("user.name") + "-conf" + File.separator + "regression-tests-overrides.properties");
        properties.load(inputStream);
        username = properties.getProperty("userUsername");
        password = properties.getProperty("userPassword");
        firstName = properties.getProperty("userFirstName");
        uhNumber = properties.getProperty("userUhNumber");
        inputStream.close();
    }
    public void loggingIn() {
        open(this.baseURL + "login");
        $("#username").val(this.username);
        $("#password").val(this.password);
        $x("//button[@name='submit']").click();
        switchTo().frame($("#duo_iframe"));
        $("#username").should(disappear);
        $("button").should(appear);
        $x("//button[text()='Send Me a Push ']").click();
        $x("//h2[text()='Choose an authentication method']").should(disappear);
        $x("//*[@id=\"app-bar\"]/nav/section[2]").should(disappear, this.timeout);
        switchTo().parentFrame();
        $x("/html/body/main/div[2]/div[1]/div/div/div[2]/h1/span").shouldBe(text(this.firstName), this.timeout);
        $x("/html/body/main/div[1]/div/div[1]/div/div/form/button").click(); //logs out
        //closeWebDriver();
    }
    public void loggingIn(SelenideDriver browser) {
        browser.open(this.baseURL + "login");
        browser.$("#username").val(this.username);
        browser.$("#password").val(this.password);
        browser.$x("//button[@name='submit']").click();
        browser.switchTo().frame($("#duo_iframe"));
        browser.$("#username").should(disappear);
        browser.$("button").should(appear);
        browser.$x("//button[text()='Send Me a Push ']").click();
        browser.$x("//h2[text()='Choose an authentication method']").should(disappear);
        browser.$x("//*[@id=\"app-bar\"]/nav/section[2]").should(disappear, this.timeout);
        browser.$x("/html/body/main/div[2]/div[1]/div/div/div[2]/h1/span").shouldBe(text(this.firstName), this.timeout);
        browser.$x("/html/body/main/div[1]/div/div[1]/div/div/form/button").click(); //logs out
    }
    public void loggingInNoDuoAuth() {
        open(this.baseURL + "login");
        $("#username").val(this.username);
        $("#password").val(this.password);
        $x("//button[@name='submit']").click();
        $x("/html/body/main/div[2]/div[1]/div/div/div[2]/h1/span").shouldBe(text(this.firstName), this.timeout);
        $x("/html/body/main/div[1]/div/div[1]/div/div/form/button").click(); //logs out
        //closeWebDriver();
    }
    public void loggingInNoDuoAuth(SelenideDriver browser) {
        browser.open(this.baseURL + "login");
        browser.$("#username").val(this.username);
        browser.$("#password").val(this.password);
        browser.$x("//button[@name='submit']").click();
        browser.$x("/html/body/main/div[2]/div[1]/div/div/div[2]/h1/span").shouldBe(text(this.firstName), this.timeout);
        browser.$x("/html/body/main/div[1]/div/div[1]/div/div/form/button").click(); //logs out
        //closeWebDriver();
    }
    public static void main(String[] args) {
        MainPage mainPage = new MainPage();
        MainPage user = new MainPage();
        try {
            mainPage.getAdminCredentials();
            user.getUserCredentials();
        } catch(Exception e) {
            System.out.println("wow");
        }
        System.out.println(mainPage.username);
        System.out.println(user.uhNumber);
        System.out.println(user.username);
    }
}
