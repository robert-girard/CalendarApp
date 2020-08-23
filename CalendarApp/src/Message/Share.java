package Message;

import Calendar.CalendarInterface;
import Event.Event;

/**
 * provides an interface for how to acquire sharable content
 */
public interface Share {
    void addCalendar(CalendarInterface cal);

    void addEvent(Event e);
}
