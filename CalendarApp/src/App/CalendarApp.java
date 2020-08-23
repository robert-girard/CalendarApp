package App;

import Calendar.*;
import Alarm.SortByDate;
import Exceptions.ObjectNotFoundException;
import Alert.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Observable;
import java.util.Observer;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;



/**
 * The class that contains and can modify current user's list of calendars
 * The observable interface allows other classes to be notified when an activity is  at given time
 */
public class CalendarApp extends Observable {
    private static final long interval = 1000; //interval of alert execution in ms
    private static Timer alertTimer;
    private CalendarInterface activeCalendar;
    private Clock activeClock;


    /**
     * Constructor for class
     *
     * @param activeCalendar the calendar from which activities will be activities will be triggered
     * @param activeClock    the clock to use to trigger activities
     * @param observer       initial observer that will be watching for triggered activities
     */
    public CalendarApp(CalendarInterface activeCalendar, Clock activeClock, Observer observer) {
        this.activeCalendar = activeCalendar;
        this.activeClock = activeClock;
        this.addObserver(observer);
    }

    /**
     * Allow user to start the alert timer
     *
     *
     */
    public void startAlertTimer() {
        alertTimer = new Timer();
        alertTimer.scheduleAtFixedRate(new AlertTask(this.activeClock, this.activeCalendar), new Date(), interval);
        this.setChanged();
    }

    /**
     * Stop the alert timer
     */
    public void stopAlertTimer() {
        alertTimer.cancel();
    }

    /**
     * changes the calendar to monitor to triggerable activities
     *
     * @param cal The calendar to switch to
     */
    public void switchCalendar(CalendarInterface cal) {
        alertTimer.cancel();
        this.activeCalendar = cal;
        alertTimer = new Timer();
        AlertTask alarm = new AlertTask(this.activeClock, this.activeCalendar);
        alertTimer.scheduleAtFixedRate(alarm, new Date(), interval);
        this.setChanged();
    }

    public void switchClock(Clock newClock) {
        alertTimer.cancel();
        this.activeClock = newClock;
        alertTimer = new Timer();
        AlertTask alarm = new AlertTask(this.activeClock, this.activeCalendar);
        alertTimer.scheduleAtFixedRate(alarm, new Date(), interval);
        this.setChanged();

    }


    /**
     * Allow user to check the time with the given clock
     */
    public void checkTime() {
        System.out.println(getCurrentTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    /**
     * Get current time from the stored clock
     */
    public LocalDateTime getCurrentTime(){
        return LocalDateTime.now(this.activeClock);
    }

    /**
     *
     */
    private class AlertTask extends TimerTask {
        private final Clock clock;
        private final CalendarInterface calendar;

        public AlertTask(Clock clock, CalendarInterface calendar) {
            this.clock = clock;
            this.calendar = calendar;
            assert (calendar != null);
        }

        /**
         * The method that will be run every time the task is triggered
         * checks all alerts to see if they should be triggered and notifies observers of those
         */
        public void run() {
            List<Alert> alerts = calendar.getAllAlerts();
            List<Alert> presentAlerts = new ArrayList<Alert>();
            if (!alerts.isEmpty()) {
                alerts.sort(new SortByDate());
            }
            for (Alert a : alerts) {
                if (a.getDttmActivate().isBefore(LocalDateTime.now(clock))) {
                    presentAlerts.add(a);
                }
            }
            for (Alert a : presentAlerts) {
                try {
                    calendar.removeIndividualAlert(a);
                } catch (ObjectNotFoundException e) {
                    // nothing to be done
                }
            }
            setChanged();
            notifyObservers(presentAlerts);
        }

    }



}

