package UI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import App.*;
import Calendar.CalendarInterface;
import CustomClock.CustomClock;

import Series.Series;

import Alert.*;
import Event.Event;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.List;

/**
 * a class that contains user log-in, and the display of menu and construction of
 * a new Calendar.Calendar after the user logs in
 */
public class CalendarConsole implements Observer {

    private final CustomClock clock;
    private final DateTimeFormatter dateTimeFormatter;
    final AccountUI accountUI;
    private CurrentUser currentUser;
    private MessageUI messageUI;
    private MemoUI memoUI;
    private AlertUI alertUI;
    private EventUI eventUI;
    private final Scanner scanner;
    private CCAddHelper AddHelper;
    private CCSearchHelper SearchHelper;
    private CCDisplayHelper DisplayHelper;
    private final UtilityUI utilUI;

    /**
     * constructor of a UI.CalendarConsole object
     */
    public CalendarConsole() {
        this.clock = new CustomClock();
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.accountUI = new AccountUI();
        this.scanner = new Scanner(System.in);
        utilUI = new UtilityUI();
    }

    @Override
    public void update(Observable o, Object arg) {
        DisplayHelper.displayActiveAlerts((List<Alert>) arg);
    }


    /**
     * Displays main menu to user
     */
    private void displayMenu() {
        String[] options = {"Event Menu",
                "view your alerts",
                "add a tag to event",
                "Memo Menu",
                "add series",
                "Alert Menu",
                "delete your account",
                "Check Messages",
                "Send a Message/Share content",
                "Find day of week",
                "Create a calendar",
                "Switch a calendar",
                "Change program time",
                "log out",
                "quit program"};
        for (int i = 0; i < options.length / 2 * 2; i += 2) {
            System.out.printf("%-40s %-40s\n", i + 1 + ": " + options[i], i + 2 + ": " + options[i + 1]);
        }
        if (options.length % 2 == 1) {
            System.out.println(options.length + ": " + options[options.length - 1]);
        }
    }

    /**
     * creates instances of helper UI specific to the current user and adds them as observes so that they will be notified if the user changes
     */
    private void initUser() {
        this.currentUser = accountUI.logIn();
        this.AddHelper = new CCAddHelper(this.currentUser, this.dateTimeFormatter);
        accountUI.addObserver(AddHelper);
        this.SearchHelper = new CCSearchHelper(this.currentUser, this.dateTimeFormatter);
        accountUI.addObserver(SearchHelper);
        this.DisplayHelper = new CCDisplayHelper(this.currentUser);
        accountUI.addObserver(DisplayHelper);
        this.messageUI = new MessageUI(currentUser);
        accountUI.addObserver(messageUI);
        this.memoUI = new MemoUI(currentUser);
        accountUI.addObserver(memoUI);
        this.alertUI = new AlertUI(currentUser);
        accountUI.addObserver(memoUI);
        this.eventUI = new EventUI(currentUser);
        accountUI.addObserver(eventUI);

        this.currentUser.runCalendar(clock, this);

    }

    /**
     * The method that is called in the main class, it also calls other methods in the program while it's running
     */
    public void run() {
        this.initUser();
        int selection = 0;
        while (selection != 15) {
            Scanner sc = new Scanner(System.in);
            displayMenu();
            selection = sc.nextInt();
            sc.nextLine();  // Consume newline left-over
            LocalDateTime now = LocalDateTime.now(clock);
            switch (selection) {
                case 1:
                    eventUI.menu();
                case 2:
                    DisplayHelper.displayAlerts(currentUser.getAllAlerts());// todo not quite working
                    break;
                case 3:
                    AddHelper.addTagToEvent();
                    break;
                case 4:
                    memoUI.menu();
                    break;
                case 5:
                    AddHelper.addSeries();
                    break;
                case 6:
                    alertUI.menu();
                    break;
                case 7:
                    accountUI.deleteAccount(currentUser);
                    break;
                case 8:
                    messageUI.readUnread();
                    break;
                case 9:
                    messageUI.sendMessage(accountUI.userChecker());
                    break;
                case 10:
                    findDayInWeek();
                    break;
                case 11:
                    accountUI.createNewCalendar(currentUser);
                    break;
                case 12:
                    switchCalendar();
                    break;
                case 13:
                    changeTime();
                    break;
                case 14:
                    accountUI.logOut(currentUser);
                    this.initUser();
                    break;
                case 15:
                    break;
            }
        }
        accountUI.logOut(currentUser);
    }

    private void changeTime() {
        System.out.println("The current time is\n" + clock);
        System.out.println("Enter 0 to fast forward, 1 to fast backward, 2 to quit");
        String choice = scanner.nextLine();
        if (choice.equals("0")) {
            System.out.println("How many minutes do you want to fast forward?");
            int m = Integer.parseInt(scanner.nextLine());
            clock.fastForward(m);
            currentUser.runCalendar(clock, this);

        } else if (choice.equals("1")) {
            System.out.println("How many minutes do you want to fast backward?");
            int m = Integer.parseInt(scanner.nextLine());
            clock.fastBackward(m);
            currentUser.runCalendar(clock, this);
        }
        System.out.println("Now the time is\n" + clock);
    }


    private LocalDateTime getLocalDateTime(String name) {
        System.out.println("Please enter the " + name + " of your event" + "\nin the format (yyyy-MM-dd HH:mm): ");
        String startTimeStr = scanner.nextLine();
        try {
            return LocalDateTime.parse(startTimeStr, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            System.out.println("Your format is wrong. Try again.");
            return getLocalDateTime(name);
        }
    }

    private void searchEvents() {
        String[] options = {"by event name", "by series name", "by tag", "by memo", "by time"};
        System.out.println("You can search events");
        int counter = 0;
        for (String option : options) {
            System.out.println(counter++ + ": " + option);
        }
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline left-over
        if (choice == 0)
            searchEventByName();
        else if (choice == 1)
            searchEventBySeriesName();
        else if (choice == 2)
            searchEventByTag();
        else if (choice == 3)
            searchEventByMemo();
        else if (choice == 4)
            searchEventByTime();
    }

    private void searchEventByName() {
        List<Event> events = SearchHelper.searchEventByName();
        //Display Methods
        if (events != null) {
            DisplayHelper.displayEvents(events);
        } else {
            System.out.println("There is no events with this name");
        }
    }

    private void searchEventBySeriesName() {
        List<Series> series = SearchHelper.searchEventBySeriesName();
        DisplayHelper.displaySeries(series);
    }


    private void searchEventByTag() {
        List<Event> events = SearchHelper.searchEventByTag();
        DisplayHelper.displayEvents(events);
    }

    private void searchEventByMemo() {
        List<Event> events = SearchHelper.searchEventByMemo();
        if (events == null) {
            System.out.println("sending back to menu");
            return;
        }
        DisplayHelper.displayEvents(events);
    }

    private void searchEventByTime() {
        System.out.println("You will enter a date, then you will pick an interval for this search of" +
                "events. Then choose one of three options of how you want to search :)");
        LocalDateTime searchTime = getLocalDateTime("date");
        List<Event> eventsFound = SearchHelper.searchEventByTime(searchTime);
        DisplayHelper.displayEvents(eventsFound);
    }


    private void findDayInWeek() {
        System.out.println("Enter a date (yyyy-mm-dd) to find what day is that date.");
        String s = scanner.nextLine();
        LocalDate date;
        try {
            date = LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            System.out.println("Your format is wrong.");
            return;
        }
        System.out.println(s + " is on " + date.getDayOfWeek());
    }

    /**
     * UI to let user pick from list of calendars as change the active calendar to the chosen one
     */
    private void switchCalendar() {
        System.out.println("Switching calendars");
        List<CalendarInterface> calendars = currentUser.getCalendars();
        CalendarInterface c = utilUI.pick(calendars, "calendar");
        currentUser.switchCalendar(c);
    }

}