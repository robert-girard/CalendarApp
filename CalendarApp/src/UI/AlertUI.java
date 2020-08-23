package UI;

import Alert.Alert;
import Alert.IndividualAlert;
import App.CurrentUser;
import Event.Event;
import Exceptions.ObjectNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AlertUI extends SubMenu {
    private static final String[] options = {"1: Browse Alerts",
            "2: Get Alert by Event",
            "3: Edit Alert",
            "4: Delete Alert",
            "5: Add Alert to Event",
            "6: Add Alerts to Event by frequency",
            "7: Return to Main Menu"
    };

    public AlertUI(CurrentUser currentUser) {
        super(currentUser, options);
    }

    /**
     * see javadoc from SubMenu super class
     */
    protected void doOption(int selection) {
        switch (selection) {
            case 1:
                browseAlerts();
                break;
            case 2:
                displayAlertByEvent();
                break;
            case 3:
                editAlert();
                break;
            case 4:
                deleteAlert();
                break;
            case 5:
                addHelp.addNewAlert();
                break;
            case 6:
                addAlerts();
                break;
        }
    }

    /**
     * UI to let user select an alert to delete
     */
    private void deleteAlert() {
        Alert a = utilFuncs.pick(currentUser.activeCalendar.getAllAlerts(), "Alert");
        if (a == null)
            return;
        try {
            currentUser.activeCalendar.removeIndividualAlert(a);
        } catch (ObjectNotFoundException e) {

            //nothing to do since the alert to be deleted cannot be found it must be gone already
        }
        System.out.println("Alert Deleted");

    }

    /**
     * UI to let user select an alert to edit (both name and activation time)
     * if the alert does not exist the user is notified
     */
    private void editAlert() {
        Alert a = utilFuncs.pick(currentUser.activeCalendar.getAllAlerts(), "Alert");
        if (a == null)
            return;

        System.out.println("Please enter the new Alert Name: ");
        String newName = sc.nextLine();

        LocalDateTime newTime = utilFuncs.getLocalDateTime(dateTimeFormatter);

        try {
            currentUser.activeCalendar.editAlert(a.getId(), newName, newTime);
        } catch (ObjectNotFoundException e) {
            System.out.println("Alert Not Found");
        }

    }

    /**
     * UI to let the user display any alert associated with a particular event
     */
    private void displayAlertByEvent() {
        List<Alert> alerts;
        Event event = utilFuncs.pick(currentUser.activeCalendar.getAllEvents(), "Event");
        if (event == null)
            return;
        try {
            alerts = currentUser.activeCalendar.retrieveAlertsFromEventId(event);
        } catch (ObjectNotFoundException e) {
            alerts = new ArrayList<Alert>();
        }
        if (alerts.isEmpty()) {
            System.out.println("No alerts found");
        } else {
            dispHelp.displayList(alerts);
        }
    }

    /**
     * UI to display all alerts to user
     */
    private void browseAlerts() {
        List<Alert> alerts= currentUser.getAllAlerts();
        if (alerts.isEmpty()) {
            System.out.println("No Alerts Found.");
        } else {
            dispHelp.displayAlerts(alerts);
        }

    }

    public void addAlerts() {
        UtilityUI utilUI = new UtilityUI();
        List<Event> allEvents = super.currentUser.activeCalendar.getAllEvents();
        Event event;

        if (!allEvents.isEmpty()) {
            event = utilUI.pick(allEvents, "event");
            if (event.getDttmStart() == null) {
                System.out.println("This event has no time to add alerts by.");
                return;
            }
        } else {
            System.out.println("There are not events to add an alert to. Please add an event first");
            return;
        }

        System.out.println("Please enter the duration between alerts (in minutes): ");
        int minutes = utilUI.validPosInt();

        System.out.println("Please enter the number of alert occurrences: ");
        int occurrences = utilUI.validPosInt();

        System.out.println("Please enter the name for your new alerts.");
        String alertName = sc.nextLine();

        LocalDateTime atime = event.getDttmStart();

        for (int i = 0; i <= occurrences; i++) {
            atime = atime.minusSeconds(minutes);
            currentUser.activeCalendar.createIndividualAlert(new IndividualAlert(atime, alertName + i), event);
        }
    }


}
