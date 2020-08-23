package Event;

import Exceptions.EndDateBeforeStartDateException;
import Exceptions.ObjectNotFoundException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all operations involving Events.
 *
 * @author Zach Folan
 */
public class EventManager implements Serializable {
    /**
     * A Map containing all Events with Event.Event Id's as keys and an Event.Event as the value.
     */
    private final Map<String, Event> events;

    /**
     * A default constructor for the Event.EventsManager class
     */
    public EventManager() {
        this.events = new HashMap<String, Event>();
    }

    /**
     * Get the events that are scheduled on this date time
     *
     * @param dttm the date and time to get Events from
     * @return a List of Events that are scheduled on this date time
     */
    public List<Event> getEventsByDate(LocalDateTime dttm) {
        List<Event> result = new ArrayList<Event>();
        for (Event e : this.getAllEvents()) {
            if (e.hasTime() && e.isOnDate(dttm)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Returns all events with name eventName ignoring case. Since Event names aren't unique we return a list of all events with that name.
     *
     * @param eventName The name of the event the method is looking for.
     * @return All events that have the same name as eventName ignoring case.
     */
    public List<Event> getEventsByName(String eventName) {
        List<Event> result = new ArrayList<Event>();
        for (Event e : this.getAllEvents()) {
            if (e.getName().equalsIgnoreCase(eventName)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Returns an all events that are currently occurring.
     *
     * @return All events that whose dttmStart has already passed but their dttmEnd has not.
     */
    public List<Event> getCurrentEvents(LocalDateTime currentTime) {
        List<Event> result = new ArrayList<>();
        for (Event e : this.getAllEvents()) {
            if (e.hasTime() && e.getDttmStart().isBefore(currentTime) && e.getDttmEnd().isAfter(currentTime)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Returns an all events that have not occurred and are occurring in the future.
     *
     * @return All events that whose dttmStart has not already passed.
     */
    public List<Event> getFutureEvents(LocalDateTime currentTime) {
        List<Event> result = new ArrayList<>();
        for (Event e : this.getAllEvents()) {
            if (e.hasTime() && e.getDttmStart().isAfter(currentTime)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Returns an all events that have already occurred.
     *
     * @return All events that whose dttmEnd has already passed.
     */
    public List<Event> getPreviousEvents(LocalDateTime currentTime) {
        List<Event> result = new ArrayList<>();
        for (Event e : this.getAllEvents()) {
            if (e.hasTime() && e.getDttmEnd().isBefore(currentTime)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Allows editing of start and endtime of
     *
     * @param id    the Id of the event to edit
     * @param start the new start time
     * @param end   the new end time
     * @throws EndDateBeforeStartDateException thrown if the event ends before it starts
     * @throws ObjectNotFoundException         Thrown if the event to be edited does not exist
     */
    public void editEventDttmById(String id, LocalDateTime start, LocalDateTime end) throws EndDateBeforeStartDateException, ObjectNotFoundException {
        Event event = getEventById(id);
        validateDates(start, end);
        event.setDttm(start, end);
    }

    /**
     * Adds an Event.Event to events using the event Id as the key.
     *
     * @param event The Event.Event that will be added to Events.
     */
    public void addEvent(Event event) {
        this.events.put(event.getId(), event);
    }

    /**
     * Pass in a list of Event Id's and get back a list of Event objects with those Id's.
     *
     * @param eventIds The list of event ids that will be found.
     * @return A list of Event whose id's are in eventIds.
     * @throws ObjectNotFoundException If an id doesn't exist the method throws an error.
     */
    public List<Event> getEventsByIds(List<String> eventIds) throws ObjectNotFoundException {
        List<Event> result = new ArrayList<Event>();
        for (String id : eventIds) {
            result.add(this.getEventById(id));
        }
        return result;
    }

    /**
     * Deletes the Event.Event whose Id is id.
     *
     * @param id The unique Id of the event that will be deleted.
     */
    public void deleteEventById(String id) throws ObjectNotFoundException {
        this.validateId(id);
        this.events.remove(id);
    }

    /**
     * Pass in a list of Events and get back a list of Event Id's.
     *
     * @param events The list of events
     * @return The list of event ids corresponding to those lists
     */
    public List<String> getEventIdsByEvents(List<Event> events) {
        List<String> result = new ArrayList<>();
        for (Event event : events) {
            result.add(event.getId());
        }
        return result;
    }

    /**
     * Returns the Event.Event who's Id is id.
     *
     * @param id The unique Id of the event that will be returned.
     * @return The Event.Event whosee Id is id.
     */
    public Event getEventById(String id) throws ObjectNotFoundException {
        this.validateId(id);
        return this.events.get(id);
    }

    /**
     * Return all of the Events that this manager is storing.
     *
     * @return All events that the Event.Event manager has.
     */
    public List<Event> getAllEvents() {
        return new ArrayList<Event>(this.events.values());
    }

    /**
     * check if the date time entered is in valid format. If the end time is before the start time, throws exception.
     *
     * @param dttmStart the start time of the Event
     * @param dttmEnd   the end time of the Event
     * @throws EndDateBeforeStartDateException if the end time is before the start time
     */
    private void validateDates(LocalDateTime dttmStart, LocalDateTime dttmEnd) throws EndDateBeforeStartDateException {
        if (dttmStart.isAfter(dttmEnd) || dttmEnd.equals(dttmStart)) {
            throw new EndDateBeforeStartDateException();
        }
    }

    /**
     * checks whether this Event id is valid. If not, throw an exception.
     *
     * @param id the id of the Event to be checked
     * @throws ObjectNotFoundException if events does not contain this event, aka this event id is not valid
     */
    private void validateId(String id) throws ObjectNotFoundException {
        if (!(this.events.containsKey(id))) {
            throw new ObjectNotFoundException();
        }
    }
}
