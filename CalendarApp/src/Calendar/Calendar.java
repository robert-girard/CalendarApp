package Calendar;

import Alert.*;
import AppID.KeyedEntity;
import Event.*;
import Exceptions.*;
import Memo.*;
import Series.*;
import Tag.*;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A calendar that holds events, alerts, memos and tags.
 *
 * @author Zach Folan, Raymond Liu
 */
public class Calendar extends KeyedEntity implements Serializable, CalendarInterface {


    /**
     * An events manager to hold all the events and get manipulate events.
     */
    private final EventManager eventManager;
    /**
     * A manager to hold and manipulate alerts.
     */
    private final AlertManager alertManager;
    /**
     * A manager to hold and manipulate series.
     */
    private final SeriesManager seriesManager;
    /**
     * A manager to hold and manipulate tags.
     */
    private final TagManager tagManager;
    /**
     * A manager to hold and manipulate memos.
     */
    private final MemoManager memoManager;
    private final String calendarName;

    /**
     * A default constructor for Calendar.Calendar.
     */
    public Calendar(String calendarName) {
        this.eventManager = new EventManager();
        this.alertManager = new AlertManager();
        this.memoManager = new MemoManager();
        this.seriesManager = new SeriesManager();
        this.tagManager = new TagManager();
        this.calendarName = calendarName;
    }

    // ----------------------------------------------------------------------------------------------------------------
    // Main operations

    // TODO: Finish search method
    public List<KeyedEntity> searchEntities(String searchTerm) {
        List<KeyedEntity> searchResults = new ArrayList<>(seriesManager.searchSeries(searchTerm));
        // Search methods in eventsManager, memoManager, tagManager, etc.
        throw new NotImplementedException();
    }

    public String getCalendarName() {
        return calendarName;
    }

    // ----------------------------------------------------------------------------------------------------------------
    // Memo operations

    public List<Memo> getAllMemos() {
        return this.memoManager.getAllMemos();
    }

    @Override
    public void editMemo(Memo oldMemo, String newMemoString) {
        memoManager.updateMemo(oldMemo.getId(), newMemoString);
    }

    public List<Event> getEventsByMemo(Memo memo) throws ObjectNotFoundException {
        String memoId = memo.getId();
        return eventManager.getEventsByIds(memoManager.getEventIDsByMemoID(memoId));
    }

    public void addMemoToEvent(Memo memo, Event event) {
        this.memoManager.associateMemoToEvent(memo, event.getId());
    }

    @Override
    public void addMemoToEvent(String memo, Event event) {
        memoManager.associateMemoToEvent(new Memo(memo), event.getId());
    }

    public void removeMemo(Memo memo) {
        this.memoManager.deleteMemo(memo.getId());
    }

    // ----------------------------------------------------------------------------------------------------------------
    // Series operations

    public List<Series> getSeriesBySeriesName(String seriesName) {
        return this.seriesManager.getSeriesByName(seriesName);
    }

    public void createSeries(List<Event> events, String seriesName) {
        this.seriesManager.createSeries(this.eventManager.getEventIdsByEvents(events), seriesName);
    }

    public void createSeries(Event firstEvent, long frequency, long numberOfEvents) {
        throw new NotImplementedException();
    }

    public List<Event> getEventsInSeries(Series series) throws ObjectNotFoundException {
        List<String> eventIds = this.seriesManager.getEventIdsFromSeries(series.getId());
        List<Event> events = new ArrayList<Event>();
        for (String eventId : eventIds) {
            events.add(this.eventManager.getEventById(eventId));
        }
        return events;
    }

    public String displaySeries(Series series) {
        String output = series.toString();
        try {
            for (Event event : this.getEventsInSeries(series)) {
                output = output.concat("\n"); // todo check the warning
                output = output.concat(event.toString());
            }
            return output;
        } catch (Exception e) {
            return "";
        }
    }

    // ----------------------------------------------------------------------------------------------------------------
    // Event operations

    /**
     * Returns an all events that have not occurred and are occurring in the future.
     *
     * @param currentTime The current time of the system.
     * @return All events whose dttmStart has not passed.
     */
    public List<Event> getFutureEvents(LocalDateTime currentTime) {
        return this.eventManager.getFutureEvents(currentTime);
    }

    /**
     * Returns all Events that have already occurred.
     *
     * @param currentTime The current time of the system.
     * @return All events whose dttmEnd has passed.
     */
    public List<Event> getPastEvents(LocalDateTime currentTime) {
        return this.eventManager.getPreviousEvents(currentTime);
    }

    /**
     * Returns all Events that are currently on going.
     *
     * @param currentTime The current time of the system.
     * @return All events whose dttmStart has passed and their dttmEnd has not.
     */
    public List<Event> getCurrentEvents(LocalDateTime currentTime) {
        return this.eventManager.getCurrentEvents(currentTime);
    }

    public List<Event> getAllEvents() {
        return this.eventManager.getAllEvents();
    }

    public List<Event> getEventsByName(String eventName) {
        return eventManager.getEventsByName(eventName);
    }

    /**
     * Adds preexisting event to even manager
     *
     * @param e the event you want to add
     */
    public void addEvent(Event e) {
        this.eventManager.addEvent(e);
    }

    public List<Event> getEventsByDate(LocalDateTime dttm) {
        return this.eventManager.getEventsByDate(dttm);
    }

    public void editEventDttm(Event event, LocalDateTime start, LocalDateTime end) throws EndDateBeforeStartDateException, ObjectNotFoundException {
        eventManager.editEventDttmById(event.getId(), start, end);
    }

    /**
     * Delete and event and everything associated with it
     *
     * @param event the event to be deleted
     * @throws ObjectNotFoundException thrown if the object isnt found
     */
    // TODO: Finish implementing this method in all managers
    public void removeEvent(Event event) throws ObjectNotFoundException {
        String eventId = event.getId();
        this.eventManager.deleteEventById(eventId);
        this.alertManager.handleEventDeletion(event);
        this.memoManager.handleEventDeletion(event);
        /*
        this.seriesManager.deleteEventFromAllSeries(eventId);
        this.tagManager.disassociateAllTag(eventId);
        this.alertManager.disassociateAllAlerts(eventId);
        */
    }

    public String displayEvent(Event event) {
        String s = "Event " + event.getName();
        if (event.hasTime()) {
            s += ": ";
            s += event.getDttmStart() + " to ";
            s += event.getDttmEnd() + "\n";
        }
        return s;
    }

    // ----------------------------------------------------------------------------------------------------------------
    // Tag operations

    public List<Event> getEventsFromTag(Tag tag) throws ObjectNotFoundException {
        List<String> eventIds = this.tagManager.getEventIDByTagID(tag.getId());
        return this.eventManager.getEventsByIds(eventIds);
    }

    public List<Tag> getAllTags() {
        return this.tagManager.getAllTags();
    }

    public Tag getTagByTagName(String name) {
        String id = tagManager.getTagIDByTagName(name);
        return tagManager.getTagByID(id);
    }


    public void addTagToEvent(Tag tag, Event event) {
        this.tagManager.pairTag(tag.getId(), event.getId());
    }

    public void createTag(String tagName, Event event) {
        Tag tag = tagManager.createTag(tagName);
        this.tagManager.pairTag(tag.getId(), event.getId());
    }

    // ----------------------------------------------------------------------------------------------------------------
    // Alert operations

    public void createIndividualAlert(IndividualAlert a, Event event) {
        alertManager.createIndividualAlert(a, event.getId());
    }

    public void editAlert(String Id, String newName, LocalDateTime activationTime) throws ObjectNotFoundException {
        Alert a = alertManager.getAlert(Id);
        a.setTime(activationTime);
        a.setName(newName);
    }

    public List<Alert> retrieveAlertsFromEventId(Event event) throws ObjectNotFoundException {
        return alertManager.getAlertFromEvent(event.getId());
    }

    public List<Alert> getAllAlerts() {
        return alertManager.getAllAlerts();
    }

    public void removeIndividualAlert(Alert alert) throws ObjectNotFoundException {
        this.alertManager.deleteSingleAlert(alert.getId());
    }


}
