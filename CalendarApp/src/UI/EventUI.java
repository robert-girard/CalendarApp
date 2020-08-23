package UI;

import App.CurrentUser;
import Event.Event;
import Exceptions.ObjectNotFoundException;
import Series.Series;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A submenu ui for event related functions
 */
public class EventUI extends SubMenu{
    /**
     * All options for the menu
     */
    private static final String[] options = {"1: Display past events",
            "2: Display current events",
            "3: Display future events",
            "4: Add new event",
            "5: Search events",
            "6: Edit an event",
            "7: Delete an event",
            "8: Return to Main Menu"
    };

    public EventUI(CurrentUser currentUser){
        super(currentUser, options);
    }

    /**
     * see SubMenu javadoc
     */
    protected void doOption(int selection) {
        switch (selection) {
            case 1:
                displayPastEvents();
                break;
            case 2:
                displayCurrentEvents();
                break;
            case 3:
                displayFutureEvents();
                break;
            case 4:
                addNewEvent();
                break;
            case 5:
                searchEvents();
                break;
            case 6:
                editEvent();
                break;
            case 7:
                deleteEvent();
                break;
        }
    }

    private LocalDateTime getCurrentTime(){
        return currentUser.getCurrentTime();
    }

    private void deleteEvent(){
        Event event = utilFuncs.pick(currentUser.activeCalendar.getAllEvents(), "Event");
        if(event == null) {
            return;
        }
        try{
            this.currentUser.activeCalendar.removeEvent(event);
        } catch (ObjectNotFoundException ex){
            //nothing to do since the alert to be deleted cannot be found it must be gone already
        }
    }

    private void editEvent(){
        Event event = utilFuncs.pick(currentUser.activeCalendar.getAllEvents(), "Event");
        if(event == null){
            return;
        }

        addHelp.editEventHelper(event);
        this.currentUser.activeCalendar.addEvent(event);
    }
    private void searchEvents() {
        String[] options = {"by event name", "by series name", "by tag", "by memo", "by time"};
        System.out.println("You can search events");
        int counter = 0;
        for (String option : options) {
            System.out.println(counter++ + ": " + option);
        }
        int choice = sc.nextInt();
        sc.nextLine();  // Consume newline left-over
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
        List<Event> events = searchHelp.searchEventByName();
        //Display Methods
        if (events != null) {
            dispHelp.displayEvents(events);
        } else {
            System.out.println("There is no events with this name");
        }
    }

    private void searchEventBySeriesName() {
        List<Series> series = searchHelp.searchEventBySeriesName();
        dispHelp.displaySeries(series);
    }


    private void searchEventByTag() {
        List<Event> events = searchHelp.searchEventByTag();
        dispHelp.displayEvents(events);
    }

    private void searchEventByMemo() {
        List<Event> events = searchHelp.searchEventByMemo();
        if (events == null) {
            System.out.println("sending back to menu");
            return;
        }
        dispHelp.displayEvents(events);
    }

    private void searchEventByTime() {
        System.out.println("You will enter a date, then you will pick an interval for this search of" +
                "events. Then choose one of three options of how you want to search :)");
        LocalDateTime searchTime = utilFuncs.getLocalDateTime(dateTimeFormatter);
        List<Event> eventsFound = searchHelp.searchEventByTime(searchTime);
        dispHelp.displayEvents(eventsFound);
    }

    private void addNewEvent(){
        addHelp.addNewEvent();
    }

    private void displayFutureEvents(){
        dispHelp.displayEvents(currentUser.activeCalendar.getFutureEvents(getCurrentTime()));
    }

    private void displayCurrentEvents(){
        dispHelp.displayEvents(currentUser.activeCalendar.getCurrentEvents(getCurrentTime()));
    }

    private void displayPastEvents(){
        dispHelp.displayEvents(currentUser.activeCalendar.getPastEvents(getCurrentTime()));
    }

}
