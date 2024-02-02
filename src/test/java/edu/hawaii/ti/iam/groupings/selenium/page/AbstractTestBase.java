package edu.hawaii.ti.iam.groupings.selenium.page;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import edu.hawaii.ti.iam.groupings.selenium.core.Property;
import edu.hawaii.ti.iam.groupings.selenium.core.User;

public class AbstractTestBase {

    protected final Property property;

    protected final Log logger = LogFactory.getLog(getClass());

    public AbstractTestBase(Property property) {
        this.property = property;
    }

    protected void loginWith(WebDriver driver, User user) {
        logger.info("Logging in with " + user);

        WebElement loginUsername = driver.findElement(By.cssSelector("#username"));
        loginUsername.sendKeys(user.getUsername());

        WebElement loginPassword = driver.findElement(By.cssSelector("#password"));
        loginPassword.sendKeys(user.getPassword());

        WebElement loginButton = driver.findElement(By.name("submitBtn"));
        loginButton.click();
    }

}
