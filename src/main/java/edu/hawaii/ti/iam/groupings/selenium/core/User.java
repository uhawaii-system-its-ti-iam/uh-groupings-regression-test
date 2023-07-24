package edu.hawaii.ti.iam.groupings.selenium.core;

import java.util.Objects;

public class User {

    private final String username;
    private final String password;
    private final String firstname;
    private final String uhnumber;

    // Private constructor.
    private User(String username, String password, String firstname, String uhnumber) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.uhnumber = uhnumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getUhnumber() {
        return uhnumber;
    }

    public static class Builder {

        private String username;
        private String password;
        private String firstname;
        private String uhnumber;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder uhnumber(String uhnumber) {
            this.uhnumber = uhnumber;
            return this;
        }

        public User create() {
            Objects.requireNonNull(username, "username cannot be null.");
            Objects.requireNonNull(password, "password cannot be null.");
            Objects.requireNonNull(firstname, "firstname cannot be null.");
            Objects.requireNonNull(uhnumber, "uhnumber cannot be null.");
            return new User(username, password, firstname, uhnumber);
        }
    }
}
