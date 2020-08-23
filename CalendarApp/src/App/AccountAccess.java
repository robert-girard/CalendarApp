package App;

import Calendar.Calendar;
import Calendar.CalendarManager;
import Calendar.CalendarInterface;
import Loader.Loader;
import Message.InboxManager;
import Message.Inbox;
import Message.MessageManager;
import User.UserManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles User account. Responsible for creating/saving/loading account
 *
 * Note: User data is only saved to file on logout. any unsaved info on improper exit will be lost
 */

public class AccountAccess implements HasUser {
    private final String outerPath = ""; //File.separator + "phase2" + File.separator + "CalendarApp" + File.separator + "resources" + File.separator;
    private UserManager userManager;
    private CalendarManager calendarManager;
    private InboxManager inboxManager;
    private MessageManager messageManager;

    public AccountAccess() {
        try { //todo alarm
            userManager = (UserManager) Loader.load(outerPath + "Accounts.ser");
        } catch (Exception i) {
            userManager = new UserManager();
        }
        try {
            calendarManager = (CalendarManager) Loader.load(outerPath + "Calendars.ser");
        } catch (Exception i) {
            calendarManager = new CalendarManager();
        }
        try {
            inboxManager = (InboxManager) Loader.load(outerPath + "Inboxes.ser");
        } catch (Exception i) {
            inboxManager = new InboxManager();
        }
    }

    /**
     * Allow user to login. acts as builder pattern for CurrentUser Objects.
     *
     * @param userName name that user inputs
     * @param password password that user inputs
     * @return CurrentUser object containing users calendars, inbox, and other user's data
     */
    public CurrentUser Login(String userName, String password) {
        String uid = userManager.getUserID(userName, password);

        if (uid == null)
            return null;

        List<CalendarInterface> calendarList = new ArrayList<CalendarInterface>();
        List<String> calendars = userManager.getUserCalendarList(uid);
        for (String cid : calendars) {
            if (!calendarList.contains(calendarManager.getCalendarByID(cid)))
                calendarList.add(calendarManager.getCalendarByID(cid));
        }

        Inbox inbox = inboxManager.getInbox(uid);
        CurrentUser user = new CurrentUser(userName, uid, calendarList.get(0), inbox);
        user.setCalendars(calendarList);

        return user;
    }

    /**
     * Allow user to create a new account
     *
     * @param userName     The user name of this new account
     * @param passWord     The password of this new account
     * @param calendarName The name of the first calendar of this new account
     * @return CurrentUser object containing users calendars, inbox, and other user's data
     *
     */
    public CurrentUser createNewAccount(String userName, String passWord, String calendarName) {
        String uid = userManager.createNewUser(userName, passWord);
        CalendarInterface cal = new Calendar(calendarName);
        calendarManager.putCalendar(cal);
        userManager.addCalendarToUser(uid, cal.getId());

        Inbox inbox = new Inbox();
        inboxManager.putInbox(uid, inbox);

        return new CurrentUser(userName, uid, cal, inbox);
    }

    /**
     * Allow user to delete current account
     *
     * @param uId the User account to delete identified by Id
     */
    public void deleteCurrentUserAccount(String uId) {
        calendarManager.deleteCalendarById(userManager.getUserCalendarID(uId));
        userManager.deleteUser(uId);
    }

    /**
     * Logs user out and saves current state to file.
     *
     * @param user the current user to logout.
     */
    public void logOut(CurrentUser user) {
        this.syncCalendars(user);

        try {
            Loader.save(outerPath + "Accounts.ser", userManager);
        } catch (IOException e) {
            System.out.println("Fail to save the Accounts");
        }
        try {
            Loader.save(outerPath + "Calendars.ser", calendarManager);
        } catch (IOException e) {
            System.out.println("Fail to save the Calendars");
        }
        try {
            Loader.save(outerPath + "Inboxes.ser", inboxManager);
        } catch (IOException e) {
            System.out.println("Fail to save the Inboxes");
        }

    }

    /**
     * Add calendars received via sharing to the user before saving to file
     * Will add calendars in current users account if it is not already there
     *
     * @param user the user to check if all calendars are stored
     */
    private void syncCalendars(CurrentUser user) {
        String uid = user.getUserId();
        for (CalendarInterface cal : user.getCalendars()) {
            String calid = cal.getId();
            if (!userManager.getUserCalendarList(user.getUserId()).contains(calid)) {
                userManager.addCalendarToUser(uid, calid);
            }
        }
    }

    /**
     * Look for the given user in userManager
     *
     * @param userName the name of user to search
     * @return whether the userManager contains this user
     */
    public boolean hasUser(String userName) {
        return userManager.hasUser(userName);
    }

    /**
     * Get the user name of the user
     *
     * @return The user name of this current user
     */
    public String getUserName(String uid) {
        return userManager.retrieveUser(uid).getName();
    }

    /**
     * Create a new calendar for this user
     *
     * @param calendarName The name for this new calendar
     */
    public void createNewCalendar(String calendarName, CurrentUser user) {
        CalendarInterface newCalendarInterface = new Calendar(calendarName);
        user.addCalendar(newCalendarInterface);
        calendarManager.putCalendar(newCalendarInterface);
        userManager.addCalendarToUser(user.getUserId(), newCalendarInterface.getId());
    }

    /**
     * adds a calendar to users account. used primarily by the sharing feature
     *
     * @param cal Calendar you want to add to the useres account
     */
    public void addCalendar(CalendarInterface cal, CurrentUser user) {
        user.addCalendar(cal);
        calendarManager.putCalendar(cal);
        userManager.addCalendarToUser(user.getUserId(), cal.getId());
    }

}
