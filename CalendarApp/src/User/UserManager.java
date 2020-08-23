package User;

import Exceptions.ObjectNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that contains all of the users from history and can create and edit user
 */
public class UserManager implements Serializable {
    /**
     * A map from user's name to user's id
     */
    private final Map<String, String> nameToUID;
    /**
     * A map from user's id to the User object
     */
    private final Map<String, User> uIDToUser;
    /**
     * A map from user's id to the id of this user's calendar
     */
    private Map<String, String> uIDToCID;
    private final Map<String, List<String>> uIDToCIDList;


    /**
     * Construct a new UserManager object
     */
    public UserManager() {
        nameToUID = new HashMap<>();
        uIDToUser = new HashMap<>();
        //uIDToCID = new HashMap<>();
        uIDToCIDList = new HashMap<>();
    }

    /**
     * retrieve the UserID.
     * NOTE: UserID is locked by password authentication. once you have User.User ID you have full access to calendar
     *
     * @param name     the username
     * @param password the users attempted login password
     * @return the id of the user
     */
    public String getUserID(String name, String password) {
        String uid = nameToUID.get(name);
        if (uid == null) {
            return null;
        }
        User user = uIDToUser.get(uid);
        if (user.authenticate(password)) {
            return uid;
        }
        return null;
    }

    public String getUserIDQuick(String name) {
        return nameToUID.get(name);
    }

    /**
     * Deletes a user by removing all relationships.
     *
     * @param uId The id of the User that will be deleted.
     */
    public void deleteUser(String uId) {
        try {
            this.validateUserId(uId);
            this.nameToUID.remove(uId);
            //this.uIDToCID.remove(uId);
            this.uIDToUser.remove(uId);
            this.uIDToCIDList.remove(uId);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get user object
     *
     * @param uid the userID
     * @return the user object corresponding to the passed id
     */
    public User retrieveUser(String uid) {
        return uIDToUser.get(uid);
    }

    /**
     * Creates a user account and adds it to the repository of users
     *
     * @param name the user name
     * @param pw   the proposed password
     * @return the UUID for the new user
     */
    public String createNewUser(String name, String pw) {
        User user = new User(name, pw);
        nameToUID.put(name, user.getId());
        uIDToUser.put(user.getId(), user);
        return user.getId();
    }

    /**
     * Associates a Calendar.Calendar with user
     * NOTE: user can only have one calendar. trying to add another will override the old
     *
     * @param uid the user id
     * @param cid the calendar id
     */
    public void addCalendarToUser(String uid, String cid) {
        if (uIDToCIDList.containsKey(uid)) {
            if (!uIDToCIDList.get(uid).contains(cid))
                getUserCalendarList(uid).add(cid);
        } else {
            List<String> newCIDList = new ArrayList<>();
            newCIDList.add(cid);
            uIDToCIDList.put(uid, newCIDList);
        }
    }

    public String getUserCalendarID(String uid) {
        return uIDToCID.get(uid);
    }

    public List<String> getUserCalendarList(String uid) {
        return uIDToCIDList.get(uid);
    }

    /**
     * Checks that there is a user for the given uId.
     *
     * @param id The id of the user that is being validated.
     * @throws ObjectNotFoundException Thrown if the id is not related to a user.
     */
    private void validateUserId(String id) throws ObjectNotFoundException {
        if (!(this.uIDToUser.containsKey(id))) {
            throw new ObjectNotFoundException();
        }
    }

    public boolean hasUser(String userName) {
        return nameToUID.containsKey(userName);
    }

}
