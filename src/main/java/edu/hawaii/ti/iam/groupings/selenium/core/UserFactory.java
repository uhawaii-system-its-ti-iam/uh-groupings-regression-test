package edu.hawaii.ti.iam.groupings.selenium.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    @Autowired
    private Property property;

    public User create(String key) {
        return new User.Builder()
                .username(property.value(key + ".user.username"))
                .password(property.value(key + ".user.password"))
                .firstname(property.value(key + ".user.firstname"))
                .uhnumber(property.value(key + ".user.uhnumber"))
                .build();
    }

}
