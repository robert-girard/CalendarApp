package Message;

import Calendar.*;

/**
 * user case class for a shareable calender
 * contains the calendar, message, and method to to the share it
 */
public class ShareableCalendar extends Shareable {
    private final CalendarInterface c;

    public ShareableCalendar(Message msg, CalendarInterface c) {
        super(msg);
        this.c = c;
    }

    /**
     * @param currentUser the current user
     */
    public void share(Share currentUser) {
        currentUser.addCalendar(c);
    }

    /**
     * Identifies the sharable content
     *
     * @return enum specific identifier for calendar
     */
    public SharedItems whatAmI() {
        return SharedItems.CALENDAR;
    }
}
