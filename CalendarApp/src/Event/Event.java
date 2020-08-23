package Event;

import AppID.KeyedEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * An calendar item with a name, start time, end time and a unique Id.
 *
 * @author Zach Folan
 */
public class Event extends KeyedEntity implements Serializable {
    /**
     * The name of the Event.Event.
     */
    private String name;
    /**
     * The start time of the Event.Event.
     */
    private LocalDateTime dttmStart;
    /**
     * The end time of the Event.Event.
     */
    private LocalDateTime dttmEnd;

    private boolean hasTime;

    public Event(String name) {
        this.name = name;
        hasTime = false;
    }

    /**
     * Constructs a new Event.Event from the given name, dttmStart and dttmEnd.
     *
     * @param name      The name of the Event.Event.
     * @param dttmStart The start time of the Event.Event.
     * @param dttmEnd   The end time of the Event.Event.
     */
    public Event(String name, LocalDateTime dttmStart, LocalDateTime dttmEnd) {
        this.name = name;
        setDttm(dttmStart, dttmEnd);
    }

    public boolean hasTime() {
        return hasTime;
    }

    /**
     * Returns the name of this Event.Event.
     *
     * @return The name of this Event.Event.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns The start time of this Event.Event.
     *
     * @return The start time of this Event.Event.
     */
    public LocalDateTime getDttmStart() {
        if (!hasTime) {
            return null;
        }
        return dttmStart;
    }

    /**
     * Returns the end time of this Event.Event.
     *
     * @return The end time of this Event.Event.
     */
    public LocalDateTime getDttmEnd() {
        assert hasTime : "this event doesn't have time.";
        return dttmEnd;
    }

    /**
     * Sets the name of this Event.Event.
     *
     * @param newName The new name of this Event.Event.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Sets the DateTimes of an event
     *
     * @param start the start DateTime
     * @param end the end DateTime
     */
    public void setDttm(LocalDateTime start, LocalDateTime end) {
        dttmStart = start;
        dttmEnd = end;
        hasTime = true;
    }

    /**
     * Checks if this event is on a date.
     *
     * @param dttm The date to compare to this event.
     * @return True if dttm is on the same date as either dttmStart or dttmEnd. False otherwise.
     */
    public boolean isOnDate(LocalDateTime dttm) {
        assert hasTime : "this event doesn't have time.";
        LocalDate dttmStartDate = this.getDttmStart().toLocalDate();
        LocalDate dttmEndDate = this.getDttmEnd().toLocalDate();
        LocalDate date = dttm.toLocalDate();
        return (date.compareTo(dttmStartDate) == 0 || date.compareTo(dttmEndDate) == 0);
    }

    /**
     * Overrides the toString method to display this event.
     *
     * @return The string representation of the Event.Event.
     */
    @Override
    public String toString() {
        if (hasTime) {
            return "name='" + this.getName() + '\'' +
                    ", start time='" + this.getDttmStart() + '\'' +
                    ", end time='" + this.getDttmEnd();
        }
        return "name='" + name;
    }
}
