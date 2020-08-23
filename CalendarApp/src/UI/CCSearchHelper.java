package UI;

import App.*;
import Event.Event;
import Exceptions.ObjectNotFoundException;
import Memo.Memo;
import Series.Series;
import Tag.Tag;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A helper class for UI.CalendarConsole that deals with methods involving searching an object to the calendar
 */
public class CCSearchHelper extends UserAction {
    /**
     * The DateTimeFormatter this class will use
     */
    private final DateTimeFormatter dateTimeFormatter;
    /**
     * The scanner object that this class will use
     */
    private final Scanner scanner;
    private final UtilityUI utilUI;

    /**
     * Constructor for this helper class
     *
     * @param currentUser       The currentUser that this class will be modifying
     * @param dateTimeFormatter The DateTimeFormatter this class will use
     */
    public CCSearchHelper(CurrentUser currentUser, DateTimeFormatter dateTimeFormatter) {
        super(currentUser);
        this.dateTimeFormatter = dateTimeFormatter;
        scanner = new Scanner(System.in);
        utilUI = new UtilityUI();
    }

    /**
     * Allow user to search an event by event's name
     *
     * @return A list of events with that name
     */
    public List<Event> searchEventByName() {
        System.out.println("Enter an event name");
        String name = scanner.nextLine();
        return currentUser.activeCalendar.getEventsByName(name);
    }

    /**
     * Allow user to search an event by event's series name
     *
     * @return A list of events of that series
     */
    public List<Series> searchEventBySeriesName() {
        System.out.println("Enter an series name");
        String name = scanner.nextLine();
        return currentUser.activeCalendar.getSeriesBySeriesName(name);
    }

    /**
     * Prompts user for a tag name and display all events associates with it
     */
    public List<Event> searchEventByTag() {
        System.out.println("Enter a tag");
        String tagName = scanner.nextLine();
        List<Event> events = new ArrayList<>();
        try {
            Tag tag = currentUser.activeCalendar.getTagByTagName(tagName);
            events = currentUser.activeCalendar.getEventsFromTag(tag);
        } catch (ObjectNotFoundException ignored) {

        }
        return events;
    }

    /**
     * Allow user to search events by a given memo
     *
     * @return A list of events associated with that memo
     */
    public List<Event> searchEventByMemo() {
        Memo memo = utilUI.pick(currentUser.activeCalendar.getAllMemos(), "memo");
        List<Event> events = new ArrayList<>();
        if (memo == null) {
            return null;
        }
        try {
            events = currentUser.activeCalendar.getEventsByMemo(memo);
        } catch (ObjectNotFoundException ignored) {

        }
        return events;
    }

    /**
     * Provide user with different ways to search event by time
     *
     * @param searchTime The time that user inputs as a starting or ending point
     * @return A list of event in that time interval
     */
    public List<Event> searchEventByTime(LocalDateTime searchTime) {
        LocalDateTime startTime;
        LocalDateTime endTime;
        //first select search type
        System.out.println("Select one of the following ways to search:\n" +
                "1. Search Forward\t" + "2. Search Backward\t" +
                "3. Search events that are also in the same interval as this date");
        String searchType = scanner.next();
        if (!searchType.equals("1") && !searchType.equals("2") && !searchType.equals("3")) {
            System.out.println("Invalid input, sending back to menu");
            return null;
        }
        //record interval type
        System.out.println("Choose if you want to search by interval of\n" +
                "1. Month\t" + "2. Date\t" + "3. Hour\t");
        String intervalType = scanner.next();
        String intervalTypeName;
        if (intervalType.equals("1")) {
            intervalTypeName = "month(s)";
        } else if (intervalType.equals("2")) {
            intervalTypeName = "day(s)";
        } else if (intervalType.equals("3")) {
            intervalTypeName = "hour(s)";
        } else {
            System.out.println("Invalid input, sending back to menu");
            return null;
        }
        //ask how long is the interval
        long intervalLength = 0;
        if (searchType.equals("1") || searchType.equals("2")) {
            System.out.println("How many " + intervalTypeName + " do you want to search?");
            intervalLength = scanner.nextLong();
        }
        //set up start and end for the interval
        startTime = generateStartEndTime(searchTime, searchType, intervalType, intervalLength)[0];
        endTime = generateStartEndTime(searchTime, searchType, intervalType, intervalLength)[1];
        //find and display events
        List<LocalDateTime> dates = getDatesByInterval(startTime, endTime);
        //find all events in this interval
        //get event by date + entend method
        List<Event> eventsFound = new ArrayList<>();
        for (LocalDateTime date : dates) {
            eventsFound.addAll(currentUser.activeCalendar.getEventsByDate(date));
        }
        //display all the events
        return eventsFound;
    }

    private LocalDateTime[] generateStartEndTime(LocalDateTime searchTime, String searchType, String intervalType, long intervalLength) {
        LocalDateTime[] result = new LocalDateTime[2];
        LocalDateTime startTime;
        LocalDateTime endTime;
        if (searchType.equals("1")) {
            startTime = searchTime;
            endTime = getTimeOfIntervalEnds(searchTime, searchType, intervalType, intervalLength);
        } else if (searchType.equals("2")) {
            startTime = getTimeOfIntervalEnds(searchTime, searchType, intervalType, intervalLength);
            endTime = searchTime;
        } else {
            if (intervalType.equals("1")) {
                startTime = searchTime.withDayOfMonth(1);
                endTime = searchTime.withDayOfMonth(searchTime.getMonth().length(searchTime.toLocalDate().isLeapYear()));
            } else if (intervalType.equals("2")) {
                startTime = searchTime.toLocalDate().atStartOfDay();
                endTime = searchTime.toLocalDate().atTime(LocalTime.parse("24:00:00"));
            } else {
                startTime = searchTime.toLocalDate().atTime(searchTime.getHour(), 0);
                endTime = searchTime.toLocalDate().atTime(searchTime.getHour(), 59);
            }
        }
        result[0] = startTime;
        result[1] = endTime;
        return result;
    }

    private LocalDateTime getTimeOfIntervalEnds(LocalDateTime searchTime, String searchType, String intervalType, long intervalLength) {
        LocalDateTime result;
        //addition
        if (searchType.equals("1")) {
            if (intervalType.equals("1")) {
                result = searchTime.plusMonths(intervalLength);
            } else if (intervalType.equals("2")) {
                result = searchTime.plusDays(intervalLength);
            } else {
                result = searchTime.plusHours(intervalLength);
            }
        } else {
            if (intervalType.equals("1")) {
                result = searchTime.minusMonths(intervalLength);
            } else if (intervalType.equals("2")) {
                result = searchTime.minusDays(intervalLength);
            } else {
                result = searchTime.minusHours(intervalLength);
            }
        }
        return result;
    }

    //code copy from
    // http://www.java2s.com/Tutorials/Java/Data_Type_How_to/Date/Get_list_of_local_dates_from_a_java_util_Date.htm
    private List<LocalDateTime> getDatesByInterval(LocalDateTime startTime, LocalDateTime endTime) {
        List<LocalDateTime> result = new ArrayList<>();
        result.add(startTime);
        for (int i = 0; i < ChronoUnit.DAYS.between(startTime, endTime); i++) {
            result.add(startTime.plusDays(i + 1));
        }
        return result;
    }

}
