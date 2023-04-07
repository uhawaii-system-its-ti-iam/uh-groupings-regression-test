### uh-groupings-regression-test

### Before running the tests
Put regression-tests-overrides-empty.properties file into .{username}-conf directory and rename it to regression-tests-overrides.properties. Move batch.txt file into .{username}-conf and put your UH username and on a new line the test account username.

Fill in the admin information with your information and ask a teammate for the test account information.

Download this [pdf](https://www.hawaii.edu/policy/docs/temp/ep2.210.pdf) into the project top level directory. I can't think of a better way to verify the usage policy link on the website is up to date. If you can think of a better way, please let me know or add the changes.

#### Setting up email account (optional)
Go into your google account settings and go to the security tab on the left. Scroll down to Less secure app access and turn that setting on (this allows the test to read and verify the email output is correct). Now go into your Gmail and open the settings tab and click on the "See all settings button". Go down to the IMAP section and enable IMAP. You could instead manually verify the content of the sent email.

#### Setting up the data
The regression test does not use default data, so before running the tests there is some set up to do on the UH Groupings test environment. 

Your UH account needs to be an admin for UH Groupings and dual factor authentication enabled. Your UH account also shouldn't be a member or an owner of any of the test account's groupings.

The test account should be a normal user not an admin. It should be the owner of the seven default groupings. The test account needs to be a member of "JTTEST-L" grouping. Other memberships shouldn't matter. 

#### Running the tests
You need to have the browser you want to test installed. Safari can't be run on Windows and needs to be done on MacOS.
Tests can be run with Maven or through Intellij. For Intellij click the run button at the top and it will run all the tests in the current file. By default it will run the test in Chrome. If you want to run the tests in Firefox, for example, put the line below in the setUpAll method. 
```java
Configuration.browser = "firefox";
```
With maven enter 
```
./mvnw clean test -D selenide.browser={browser you want to run} -D test={test file}
```
List of browser names

- ```firefox```
- ```chrome``` 
- ```edge```  
- ```ie```
- ```safari```

##### When a test fails
If a test fails when running it through Intellij, it will say where it failed and provide a screenshot and HTML of the page when it failed. This can be used to figure out why/how it failed.

If a test that changes data fails, it will not reset to the inital state. So, you need to go back and reset it manually.

A test can fail for reasons other than incorrect behavior. The UI could have changed, the initial state of the accounts were incorrect, or the test server could go down. So, if a test does fail, you should check the behavior manually. 

##### Creating new tests
Example test: While not logged in, the info link in the navbar should redirect you do /info

First we want to click on the info link. To do so we need to find the info element. A quick way to do it is to right click on the element and click inspect in the dropdown. 

![inspect element](/images/inspect-element.png). 

This will bring up the dev tools pane with the inspected element highlighted. Right click on the highlighted element and hover over copy, then click copy selector in the dropdown. 

![selector](/images/selector.png)

Your clipboard should now have something that looks like this in it. This is the CSS selector for the info link in the navbar.

    #navbarSupportedContent > ul > li:nth-child(1) > a
With selenide referencing an element's CSS selector can be done with $ method.

    $("CSS selector")

Once you have the selector you can interact with it like clicking on it. Below is an example of clicking on the Info link in the navbar. There are other ways of finding an element. CSS selector is just one way.
    
```java
    $("#navbarSupportedContent > ul > li:nth-child(1) > a").click();
```

In terms of readability it isn't great. Below is a slightly more readable version of the same thing. It finds the element with the nav-item class and has the text of "Info" which would be our info link. Either way works, it is up to you on how you want to write it.

```java
    $(".nav-item").find(byText("Info")).click();
```
Verifying the link works. Using Selenide conditions it is pretty straight forward. Find your element like above and use .should(condition).

```java
    $("body > main > div.seafoam-bg.pt-5.pb-5 > div > div:nth-child(1) > div:nth-child(1) > h1").shouldHave(text("What is a UH Grouping?"));
```
The above asserts that the selector should have the text "What is a UH Grouping?" and if it doesn't the test fails. .should has aliases shouldHave and shouldBe. They are all equivalent, so just use the one that makes the most grammatical sense.

This was an exmaple of a simple test, but whatever you can do by interacting with the browser, Selenide can do as well.

Here is a link to the Selenide documentation [Link](https://selenide.org/documentation.html). 


