package edu.hawaii.ti.iam.groupings.selenium.core;

import java.util.Objects;

public record User(String username, String password, String firstname, String uhnumber) {

    public static final class Builder {
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

        public User build() {
            Objects.requireNonNull(username, "username cannot be null.");
            Objects.requireNonNull(password, "password cannot be null.");
            Objects.requireNonNull(firstname, "firstname cannot be null.");
            Objects.requireNonNull(uhnumber, "uhnumber cannot be null.");
            return new User(username, password, firstname, uhnumber);
        }
    }
}
