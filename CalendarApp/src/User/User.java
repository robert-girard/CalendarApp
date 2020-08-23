package User;

import AppID.KeyedEntity;

import java.io.Serializable;

/**
 * A class representing a user that has a name and a password
 */
public class User extends KeyedEntity implements Serializable {
    /**
     * The name of this user
     */
    private final String name;
    /**
     * The password of this user's account
     */
    private String password;

    /**
     * creates a "ghost user" to avoid using java.util.Optional, since Optional makes casting annoying
     */
    public User() {
        name = "";
        password = "";
    }

    /**
     * creates a User object with name and password
     *
     * @param userName the username
     * @param password the password of the user
     */
    public User(String userName, String password) {
        this.name = userName;
        this.password = password;
    }

    /**
     * Check password
     *
     * @param password the password attempt
     * @return true of password correct
     */
    boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public String getName() {
        return name;
    }

    /**
     * Change password, need to authenticate first
     *
     * @param oldpassword the old password
     * @param newpassword the new password
     * @return whether authentication passed
     */
    boolean setPassword(String oldpassword, String newpassword) {
        if (this.authenticate(oldpassword)) {
            password = newpassword;
            return true;
        }
        return false;
    }

}
