package Calendar;

import Alert.*;
import AppID.KeyedEntity;
import Event.Event;
import Exceptions.EndDateBeforeStartDateException;
import Exceptions.ObjectNotFoundException;
import Series.Series;
import Memo.Memo;
import Tag.Tag;

import java.time.LocalDateTime;
import java.util.*;

// I am trying to create an Interface that Calendar.Calendar implements,
// so that there's an abstraction layer between the high level stuff and the things in Calendar.Calendar
// In other words, a higher level class shouldn't need to know everything about the Calendar.Calendar
public interface CalendarInterface {
    // Main methods
    List<KeyedEntity> searchEntities(String searchTerm);

    //UUID funcitons
    String getId();

    // Memo methods
    List<Memo> getAllMemos();

    void editMemo(Memo oldMemo, String newMemoString);


    List<Event> getEventsByMemo(Memo memo) throws ObjectNotFoundException;

    void removeMemo(Memo memo);

    void addMemoToEvent(Memo memo, Event event);

    void addMemoToEvent(String memo, Event event);

    List<Series> getSeriesBySeriesName(String seriesName);

    void createSeries(List<Event> events, String seriesName);

    void createSeries(Event firstEvent, long frequency, long numberOfEvents);

    List<Event> getEventsInSeries(Series series) throws ObjectNotFoundException;

    String displaySeries(Series series); // To compensate for that our Series itself doesn't contain everything

    // Event methods
    List<Event> getEventsByDate(LocalDateTime dttm);

    List<Event> getPastEvents(LocalDateTime currentTime);

    List<Event> getCurrentEvents(LocalDateTime currentTime);

    List<Event> getFutureEvents(LocalDateTime currentTime);

    List<Event> getAllEvents();

    List<Event> getEventsByName(String name);

    String displayEvent(Event event); // To compensate for the fact that our Event itself doesn't contain everything.


    void editEventDttm(Event event, LocalDateTime start, LocalDateTime end) throws EndDateBeforeStartDateException, ObjectNotFoundException;

    // Tag methods
    List<Tag> getAllTags();

    List<Event> getEventsFromTag(Tag tag) throws ObjectNotFoundException;

    Tag getTagByTagName(String name);


    void addTagToEvent(Tag tag, Event event);


    void createTag(String tagName, Event event);

    // Alert methods
    void createIndividualAlert(IndividualAlert a, Event event);


    void editAlert(String Id, String newName, LocalDateTime activationTime) throws ObjectNotFoundException;

    List<Alert> retrieveAlertsFromEventId(Event event) throws ObjectNotFoundException;

    List<Alert> getAllAlerts();

    void removeIndividualAlert(Alert alert) throws ObjectNotFoundException;

    void addEvent(Event e);

    void removeEvent(Event event) throws ObjectNotFoundException;

    String getCalendarName();

}