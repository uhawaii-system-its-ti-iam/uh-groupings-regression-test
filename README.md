### uh-groupings-regression-test

### Before running the tests
Put regression-tests-overrides-empty.properties file into .{username}-conf directory and rename it to regression-tests-overrides.properties. Move batch.txt file into .{username}-conf and put your UH username and on a new line the test account username.

Fill in the admin information with your information and ask a teammate for the test account information.

Download this [pdf](https://www.hawaii.edu/policy/docs/temp/ep2.210.pdf) into the project top level directory. I can't think of a better way to verify the usage policy link on the website is up to date. If you can think of a more robust way, please let me know or add the changes.

#### Setting up email account (optional)
Go into your google account settings and go to the security tab on the left. Scroll down to Less secure app access and turn that setting on (this allows the test to read and verify the email output is correct). Now go into your Gmail and open the settings tab and click on the "See all settings button". Go down to the IMAP section and enable IMAP. You could instead manually verify the content of the sent email.

#### Running the tests
You need to have the browser you want to test installed. Safari can't be run on Windows and needs to be done on MacOS.
Tests can be run with Maven or through Intellij. For Intellij click the run button at the top and it will run all the tests in the current file. By default it will run the test in Chrome. If you want to use, for example, Firefox in the setUpAll method put in 
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




