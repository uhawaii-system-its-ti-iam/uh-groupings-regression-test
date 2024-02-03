package edu.hawaii.ti.iam.groupings.selenium.page;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import edu.hawaii.ti.iam.groupings.selenium.core.Property;
import edu.hawaii.ti.iam.groupings.selenium.core.User;
import edu.hawaii.ti.iam.groupings.selenium.core.UserFactory;

public class AbstractTestBase {

    protected final Log logger = LogFactory.getLog(getClass());

    protected Property property;
    private UserFactory userFactory;

    // Constructor.
    public AbstractTestBase() {
        // Empty.
    }

    @Autowired
    public final void setProperty(Property property) {
        this.property = property;
    }

    @Autowired
    public final void setUserFactory(UserFactory userFactory) {
        this.userFactory = userFactory;
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

    protected User createUser(String key) {
        return userFactory.create(key);
    }

}
