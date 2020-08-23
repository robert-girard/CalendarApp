package UI;

import Alert.Alert;
import App.CurrentUser;
import Calendar.CalendarInterface;
import Event.Event;
import Series.Series;

import java.util.List;

public class CCDisplayHelper extends UserAction {

    public CCDisplayHelper(CurrentUser user) {
        super(user);
    }

    public void displayEvents(List<Event> events) {
        if (events == null || events.size() == 0) {
            System.out.println("You don't have any events of this type");
            return;
        }
        for (Event e : events)
            System.out.println(currentUser.displayEvent(e));
    }

    public void displaySeries(List<Series> series) {
        for (Series s : series) {
            System.out.println(currentUser.displaySeries(s));
        }
    }

    public void displayActiveAlerts(List<Alert> alerts) {
        int counter = 1;
        if (!alerts.isEmpty()) {
            System.out.println("ALERTS ACTIVATED:");
        }
        for (Alert alert : alerts) {
            System.out.print(counter + " ");
            System.out.println(alert);
        }
    }

    public void displayAlerts(List<Alert> alerts) {
        int counter = 1;
        for (Alert alert : alerts) {
            System.out.print(counter + " ");
            System.out.println(alert);
        }
    }

    public <T> void displayList(List<T> lst) {
        int counter = 1;
        for (T item : lst) {
            System.out.print(counter + " ");
            System.out.println(item);
        }
    }

    public void displayCalendars(List<CalendarInterface> cals) {
        for (int i = 0; i < cals.size(); i++) {
            System.out.println(i + "\t" + cals.get(i).getCalendarName());
        }
    }


}
