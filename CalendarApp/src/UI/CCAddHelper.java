package UI;

import App.CurrentUser;
import Event.Event;
import Exceptions.EndDateBeforeStartDateException;
import Exceptions.ObjectNotFoundException;
import Memo.Memo;
import Tag.Tag;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import Alert.IndividualAlert;

/**
 * A helper class for UI.CalendarConsole that deals with methods involving adding an object to the calendar
 */
public class CCAddHelper extends UserAction {
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
     * A constructor for UI.CCAddHelper
     *
     * @param user      The current user that this class will be helping
     * @param dateTimeFormatter The dateTimeFormatter for this helper class
     */
    public CCAddHelper(CurrentUser user, DateTimeFormatter dateTimeFormatter) {
        super(user);
        this.dateTimeFormatter = dateTimeFormatter;
        scanner = new Scanner(System.in);
        utilUI = new UtilityUI();
    }

    /**
     * Associates a time tag or memo to event
     *
     * @param event to associate with
     */
    public void editEventHelper(Event event) {
        System.out.println("Please enter your new event info or quit");
        System.out.println("0: name\n1: time\n2: tag\n3: memo\n4: quit");
        String choice = scanner.nextLine();
        switch (choice) {
            case "0":
                editEventName(event);
                break;
            case "1":
                addTime(event);
                break;
            case "2":
                addTag(event);
                break;
            case "3":
                addMemo(event);
                break;
            default:
                return;
        }
        editEventHelper(event);
    }

    /**
     * Edits the name of an event
     *
     * @param event the event which will have its name edited
     */
    private void editEventName(Event event){
        System.out.println("Please evnter the new name of your event: ");
        event.setName(scanner.nextLine());
    }

    /**
     * Allows user to add a new event, this method will be called in UI.CalendarConsole class
     */
    public void addNewEvent() {
        System.out.println("Please enter name of your new event: ");
        String eventName = scanner.nextLine();
        Event event = new Event(eventName);
        super.currentUser.addEvent(event);
        editEventHelper(event);
        super.currentUser.addEvent(event);
        System.out.println("Your event is successfully created.");
    }

    /**
     * Allow user to add a tag to an event, this method will be called in UI.CalendarConsole class
     *
     * @throws InputMismatchException  Exception that the input type is different from the required
     */
    public void addTagToEvent() throws InputMismatchException {
        List<Event> events = super.currentUser.activeCalendar.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("You have no event");
            return;
        }
        Event event = utilUI.pick(events, "event");
        if (event == null) {
            System.out.println("Invalid input, send back to menu");
            return;
        }
        addTag(event);
    }

    /**
     * Prompt user to select an event then create a memo and pair them
     */
    public void addMemoToEvent(String userName) {
        List<Event> events = super.currentUser.activeCalendar.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("You have no event");
            return;
        }
        Event event = utilUI.pick(events, "event");
        addMemo(event);
    }

    /**
     * Allow user to add a series to the calendar, will be called in UI.CalendarConsole class
     */
    public void addSeries() {
        List<Event> events = super.currentUser.activeCalendar.getAllEvents();
        if (events.size() == 0) {
            System.out.println("Not enough events to make series");
            return;
        }
        System.out.println("Enter 0 to create a series by linking two existing events");
        System.out.println("Enter 1 to create a series by using an event, frequency, and number of events");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline left-over
        if (choice == 0) {
            if (events.size() < 2) {
                System.out.println("Not enough events");
                return;
            }
            Event event1 = utilUI.pick(events, "event");
            Event event2 = utilUI.pick(events, "event");
            while (event1 == event2) {
                System.out.println("You cannot choose the same event twice");
                event2 = utilUI.pick(events, "event");
            }
            System.out.println("Enter a series name");
            String seriesName = scanner.nextLine();
            List<Event> series = Arrays.asList(event1, event2);
            super.currentUser.activeCalendar.createSeries(series, seriesName);
        } else {
            Event event = utilUI.pick(events, "event");
            System.out.println("Enter the frequency in days");
            long frequency = scanner.nextLong();
            System.out.println("Enter the number of events");
            long numberOfEvents = scanner.nextLong();
            scanner.nextLine();
            super.currentUser.activeCalendar.createSeries(event, frequency * 24 * 60 * 1000, numberOfEvents);
        }
    }

    /**
     * Prompts the user for information and add a new event
     */
    public void addNewAlert() {
        List<Event> allEvents = super.currentUser.activeCalendar.getAllEvents();
        if (!allEvents.isEmpty()) {
            Event event = utilUI.pick(allEvents, "event");
            System.out.println("Please enter the name of your new alert");
            String alertName = scanner.nextLine();
            System.out.println("Please enter the activation time of your new alert " + "\n" +
                    "in the format (yyyy-MM-dd HH:mm): ");
            String startTimeStr = scanner.nextLine();
            LocalDateTime alertTime = LocalDateTime.parse(startTimeStr, dateTimeFormatter);
            super.currentUser.activeCalendar.createIndividualAlert(new IndividualAlert(alertTime, alertName), event);
        } else {
            System.out.println("There are not events to add an alert to. Please add an event first");
        }
    }

    /**
     * UI to add a time to the event
     *
     * @param event the event to add a time to
     */
    private void addTime(Event event) {
        UtilityUI utilui = new UtilityUI();
        try {
            System.out.println("Please the start time of you event.");
            LocalDateTime startTime = utilui.getLocalDateTime(dateTimeFormatter);
            System.out.println("Please the end time of you event.");
            LocalDateTime endTime = utilui.getLocalDateTime(dateTimeFormatter);
            super.currentUser.activeCalendar.editEventDttm(event, startTime, endTime);
        } catch (EndDateBeforeStartDateException e) {
            System.out.println("End date cannot be before the start date." +
                    "\nenter 1 to try again or enter 0 to get back to menu");
            String choice = scanner.next();
            scanner.nextLine();  // Consume newline left-over
            if (choice.equals("1")) {
                addTime(event);
            } else if (choice.equals("0")) {
                System.out.println("Sending you back to menu");
            } else {
                System.out.println("Invalid input, sending you back to menu");
            }
        } catch (ObjectNotFoundException ignored) {

        }
    }

    /**
     * UI to associate a tag to the event
     *
     * @param event the event to add a tag to
     */
    private void addTag(Event event) {
        System.out.println("Enter 0 to create a new tag, 1 to use an existing tag, or 2 to quit");
        String choice = scanner.nextLine();
        if (choice.equals("1")) {
            Tag tag = utilUI.pick(super.currentUser.activeCalendar.getAllTags(), "tag");
            if (tag == null)
                return;
            super.currentUser.activeCalendar.addTagToEvent(tag, event);
            System.out.println("You tag has been added");
        } else if (choice.equals("0")) {
            System.out.println("Enter the name for your new tag");
            String tagName = scanner.nextLine();
            super.currentUser.activeCalendar.createTag(tagName, event);
            System.out.println("You tag has been added");
        }
    }

    /**
     * UI to associate a memo to an event
     *
     * @param event the event to add a memo to
     */
    private void addMemo(Event event) {
        System.out.println("Enter 0 to create a new memo, 1 to use an existing memo, or 2 to quit");
        String choice = scanner.nextLine();
        if (choice.equals("1")) {
            Memo memo = utilUI.pick(super.currentUser.activeCalendar.getAllMemos(), "memo");
            if (memo == null)
                return;
            super.currentUser.activeCalendar.addMemoToEvent(memo, event);
            System.out.println("You memo has been added");
        } else if (choice.equals("0")) {
            System.out.println("Enter the name for your new memo");
            String memoName = scanner.nextLine();
            super.currentUser.activeCalendar.addMemoToEvent(memoName, event);
            System.out.println("You memo has been added");
        }
    }

}
