package App;

import Alert.Alert;
import Calendar.CalendarInterface;
import Event.Event;
import Message.InboxInterface;
import Message.Share;
import Series.Series;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;


/**
 * CurrentUser Class holds all user data for the logged in user. It permits access to create/read/modify/delete user data for the logged in user
 */
public class CurrentUser implements Share {
    private final String userId;
    private final String userName;
    private List<CalendarInterface> calendars;

    /**
     * Interface for the loaded Calendar permits access to read/modify the calendar the current user is working in.
     * It should be noted that the current user can have only one calendar loaded at a time.
     */
    public CalendarInterface activeCalendar;

    /**
     * Interface allowing access to read messages sent to them and add shared items contained in those messages
     *
     */
    public final InboxInterface inbox;
    private CalendarApp app;

    /**
     * Constructor for CurrentUser
     *
     * @param userName The Name of the user
     * @param id       the Id of the user
     * @param cal      the calendar interface that will be active when the current user is made
     * @param inbox    the inbox interface that will be active when the current user is made
     */
    public CurrentUser(String userName, String id, CalendarInterface cal, InboxInterface inbox) {
        this.userName = userName;
        this.userId = id;
        this.activeCalendar = cal;
        this.calendars = new ArrayList<CalendarInterface>();
        this.calendars.add(cal);
        this.inbox = inbox;
    }

    /**
     * Starts all timing functionality related to the active calendar
     *
     * @param clock Which clock to use at which time
     * @param observer who will be notified when timing based activities are triggered
     */
    public void runCalendar(Clock clock, Observer observer) {
        if (this.app != null) {
            app.stopAlertTimer();
        }
        this.app = new CalendarApp(this.activeCalendar, clock, observer);
        app.startAlertTimer();
    }

    /**
     * Returns the current time.
     */
    public LocalDateTime getCurrentTime(){
        return this.app.getCurrentTime();
    }

    /**
     * Stops all timing funcitonality related to the active calendar
     */
    public void stopCalendar()
    {
        app.stopAlertTimer();
    }

    /**
     * identifies the Current user by returning the current user's user id
     *
     * @return userId the user ID of the current user
     */
    public String getUserId() {
        return userId;
    }

    /**
     * The current user has a list of calendars which are eligible for activation to the active calendar. This sets that list.
     *
     * @param calendars List of calendars to which the current user may select as active
     */
    public void setCalendars(List<CalendarInterface> calendars) {
        this.calendars = calendars;
    }

    public void switchCalendar(CalendarInterface c) {
        activeCalendar = c;
        if (app != null) {
            app.switchCalendar(this.activeCalendar);
        }
    }


    /**
     * Adds a calendar to the current user's list of possible active calendars
     * Note: required for Share interface as the method to use by SharableCalendar for sharing.
     * Additional note: calendars added in this way are not saved into the user account until syncCalendar is called upon during logout
     * @param cal the calendar to be added
     */
    public void addCalendar(CalendarInterface cal) {
        calendars.add(cal);
    }

    /**
     * Load user's calendar with the given index
     *
     * @param uID   User ID
     * @param index The index of the calendar to be loaded in the calendarList
     */
    public void LoadUserCalendar(String uID, int index) {
        this.activeCalendar = calendars.get(index); //TODO should this just be in Login?
    }

    /**
     * Get all events before the given time
     *
     * @param now The current time
     * @return all events before the given time
     */
    public List<Event> getPastEvents(LocalDateTime now) {
        return this.activeCalendar.getPastEvents(now);
    }

    /**
     * Get all ongoing events for the given time
     *
     * @param now The current time
     * @return all ongoing events for the given time
     */
    public List<Event> getCurrentEvents(LocalDateTime now) {
        return this.activeCalendar.getCurrentEvents(now);
    }

    /**
     * Get all future events for the given time
     *
     * @param now The current time
     * @return all events after the given time
     */
    public List<Event> getFutureEvents(LocalDateTime now) {
        return this.activeCalendar.getFutureEvents(now);
    }

    /**
     * Get all alerts of this current calendar
     *
     * @return all alerts of this current calendar
     */
    public List<Alert> getAllAlerts() {
        return this.activeCalendar.getAllAlerts();
    }

    /**
     * Display an event
     *
     * @param e the event to be displayed
     * @return The string representation of this event
     */
    public String displayEvent(Event e) {
        return this.activeCalendar.displayEvent(e);
    }

    /**
     * Display a series
     *
     * @param s the series to be displayed
     * @return The string representation of this series
     */
    public String displaySeries(Series s) {
        return this.activeCalendar.displaySeries(s);
    }

    /**
     * Get all events of the current calendar
     *
     * @return all events of the current calendar
     */
    public List<Event> getAllEvents() {
        return this.activeCalendar.getAllEvents();
    }


    /**
     * returns the list of the current users calendars
     *
     * @return calendars the list of current users calendars
     */
    public List<CalendarInterface> getCalendars() {
        return this.calendars;
    }

    /**
     * Adds an event to the current active calendar
     *
     * Note: required by Share interface as the action that an ShareableEvent would take to share
     *
     * @param e event
     */
    public void addEvent(Event e) {
        this.activeCalendar.addEvent(e);
    }


    public String getUserName() {
        return userName;
    }
}
