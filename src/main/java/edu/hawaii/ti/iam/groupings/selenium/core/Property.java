package edu.hawaii.ti.iam.groupings.selenium.core;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Property {

    private final Environment environment;

    // Constructor.
    public Property(Environment environment) {
        this.environment = environment;
    }

    public String value(String key) {
        return environment.getProperty(key);
    }

}
