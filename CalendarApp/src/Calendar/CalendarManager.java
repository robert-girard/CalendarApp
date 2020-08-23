package Calendar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A Class for managing calendars
 * Stores and permits access to calendars based on user/userId
 */
public class CalendarManager implements Serializable {
    final Map<String, CalendarInterface> calendarMap;


    public CalendarManager() {
        calendarMap = new HashMap<String, CalendarInterface>();
    }

    /**
     * Retrieve Calendar.Calendar by CalendarID
     *
     * @param calendarID the ID of the calendar you want to retrieve
     * @return a calendar
     */
    public CalendarInterface getCalendarByID(String calendarID) {
        return calendarMap.get(calendarID);
    }

    /**
     * Adds a calendar to the repository
     * NOTE: replaces the current ID->calendar association if it already exists
     *
     * @param c the calendar
     */
    public void putCalendar(CalendarInterface c) {
        calendarMap.put(c.getId(), c);
    }

    /**
     * removes calendar from repository
     *
     * @param c the calendar to remove from repository
     */
    public void deleteCalendar(CalendarInterface c) {
        this.deleteCalendarById(c.getId());
    }

    /**
     * Removes calendar from the repository.
     *
     * @param calendarId The id of the calendar that will be deleted.
     */
    public void deleteCalendarById(String calendarId) {
        this.calendarMap.remove(calendarId);
    }

    /**
     * removes calendar from repository by iD
     *
     * @param c the calendar id to remove the calendar repository
     */
    public void deleteCalendar(String c) {
        calendarMap.remove(c);

    }

}
