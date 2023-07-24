package edu.hawaii.ti.iam.groupings.selenium.core;

import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Property {

    @Value("${app.url}")
    private String appUrl;

    @Value("${url.base}")
    private String baseUrl;

    @Value("${admin.user.username}")
    private String adminusername;

    @Value("${admin.user.password}")
    private String adminpassword;

    @Value("${admin.user.firstname}")
    private String adminfirstname;

    @Value("${admin.user.uhnumber}")
    private String adminuhnumber;

    @Value("${user.user.username}")
    private String username;

    @Value("${user.user.password}")
    private String password;

    @Value("${user.user.firstname}")
    private String firstname;

    @Value("${user.user.uhnumber}")
    private String uhnumber;

    private Map<String, String> map = new HashMap<>();

    @PostConstruct
    public void postConstruct() {
        map.put("app.url", appUrl);
        map.put("url.base", baseUrl);
        map.put("admin.user.username", adminusername);
        map.put("admin.user.password", adminpassword);
        map.put("admin.user.firstname", adminfirstname);
        map.put("admin.user.uhnumber", adminuhnumber);
        map.put("user.user.username", username);
        map.put("user.user.password", password);
        map.put("user.user.firstname", firstname);
        map.put("user.user.uhnumber", uhnumber);
    }

    public String value(String key) {
        return map.get(key);
    }

}
