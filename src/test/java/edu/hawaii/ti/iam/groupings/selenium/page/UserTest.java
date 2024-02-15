package edu.hawaii.ti.iam.groupings.selenium.page;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import edu.hawaii.ti.iam.groupings.selenium.core.User;

public class UserTest {

    static private User user;

    @BeforeAll
    static public void beforeAll() {
        user = new User.Builder()
                .username("username")
                .password("password")
                .firstname("firstname")
                .uhuuid("uhuuid")
                .build();
    }

    @Test
    public void accessors() {
        assertThat(user.username(), equalTo("username"));
        assertThat(user.password(), equalTo("password"));
        assertThat(user.firstname(), equalTo("firstname"));
        assertThat(user.uhuuid(), equalTo("uhuuid"));
    }

    @Test
    public void testEqual() {
        User user1 = new User.Builder()
                .username("username")
                .password("password")
                .firstname("firstname")
                .uhuuid("uhuuid")
                .build();

        User user2 = new User.Builder()
                .username("username2")
                .password("password2")
                .firstname("firstname2")
                .uhuuid("uhuuid2")
                .build();

        assertEquals(user, user1);
        assertNotEquals(user, user2);
    }

    @Test
    public void testToString() {
        String expected = "User[username=" + user.username() + ", password=" + user.password() + ", firstname=" + user.firstname() +", uhuuid=" + user.uhuuid() + "]";

        assertEquals(user.toString(), expected);
    }

    @Test
    public void invalid() {
        // Check the exception.
        assertThrows(NullPointerException.class,
                () -> new User.Builder().build());

        // Another way.
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> new User.Builder().build());
        assertThat(thrown.getMessage(), equalTo("username cannot be null."));

        thrown = assertThrows(NullPointerException.class,
                () -> new User.Builder()
                        .username("u")
                        .build());
        assertThat(thrown.getMessage(), equalTo("password cannot be null."));

        thrown = assertThrows(NullPointerException.class,
                () -> new User.Builder()
                        .username("u")
                        .password("p")
                        .build());
        assertThat(thrown.getMessage(), equalTo("firstname cannot be null."));

        thrown = assertThrows(NullPointerException.class,
                () -> new User.Builder()
                        .username("u")
                        .password("p")
                        .firstname("f")
                        .build());
        assertThat(thrown.getMessage(), equalTo("uhuuid cannot be null."));
    }
}