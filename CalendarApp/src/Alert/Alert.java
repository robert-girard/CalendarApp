package Alert;

import AppID.KeyedEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * An event alert, has the alert time，the id of the alert and the id of event it belongs to.
 */
public class Alert extends KeyedEntity implements Serializable {

    /*
     * The time to stimulate the alert
     */
    protected LocalDateTime dttmActivate;

    /*
     * The name of the alert
     */
    protected String name;

    /*
     * is the alert activated
     */
    protected boolean isActivated;

    /**
     * Constructs a new Alert from given all details of alerts
     *
     * @param name         The unique id of the alert
     * @param dttmActivate The time to activate the alert
     */
    public Alert(String name, LocalDateTime dttmActivate) {
        super();
        this.name = name;
        this.dttmActivate = dttmActivate;
        isActivated = dttmActivate.isBefore(dttmActivate);
    }


    /**
     * Getter for the date time to activate
     *
     * @return dttmActivate: the activate time for the alert
     */
    public LocalDateTime getDttmActivate() {
        return dttmActivate;
    }


    /**
     * Sets the dttmActivate of this Event.
     *
     * @param dttmActivate The time that the alert activate
     */
    public void setDttmActivate(LocalDateTime dttmActivate) {
        this.dttmActivate = dttmActivate;
    }

    /**
     * Return the toString
     *
     * @return String: return the string type
     */
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String formattedDateTime = dttmActivate.format(formatter);
        return this.name + " " + formattedDateTime;
    }

    /**
     * Edit the alert's deadline
     *
     * @param time the new time
     */
    public void setTime(LocalDateTime time) {
        this.dttmActivate = time;
    }

    public void setName(String name) {this.name = name;}

}
