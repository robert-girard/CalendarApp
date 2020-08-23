package Alert;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A subclass of Alert that represents an alert that can repeat with a frequency
 */
public class FrequentAlert extends Alert implements Serializable {

    /*
     * The end time of the frequency ends
     */
    private final LocalDateTime dttmEnd;

    /*
     * The frequency of the frequency alert repeats
     */
    private long frequency;

    /*
     * The unique id for frequency alerts that in the same series
     */
    private final String frequencyID;


    /**
     * Constructs a new FrequentAlert by given attributes
     *
     * @param dttmActivate The time to activate the alert
     * @param name         The name of alert
     * @param dttmEnd      The time the frequency ends
     * @param frequency    The frequency that alert repeats ******in minutes*****
     */
    public FrequentAlert(LocalDateTime dttmActivate, String name, LocalDateTime dttmEnd, long frequency) {
        super(name, dttmActivate);
        this.dttmEnd = dttmEnd;
        this.frequency = frequency;
        this.frequencyID = UUID.randomUUID().toString();
    }

    /**
     * Private constructor
     * Constructs a new FrequentAlert by eventId, dttEnd, frequency and the same frequencyId
     *
     * @param eventId      The unique id of event that the alert belongs to
     * @param dttmEnd      The time the frequency ends
     * @param frequency    The frequency that alert repeats
     * @param dttmActivate The time this alert activate
     * @param frequencyID  The frequencyID that this alert belongs to
     */
    private FrequentAlert(LocalDateTime dttmActivate, String eventId, LocalDateTime dttmEnd, long frequency,
                          String frequencyID) {
        super(eventId, dttmActivate);
        this.dttmEnd = dttmEnd;
        this.frequency = frequency;
        this.frequencyID = frequencyID;
    }


    /**
     * Getter for frequentID
     *
     * @return frequencyID
     */
    public String getFrequencyID() {
        return this.frequencyID;
    }

    /**
     * Setter for frequency
     *
     * @param frequency: new frequency to the alert
     */
    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }


    /**
     * To activate the alert.
     *
     * @return boolean: if the alert activates(if it is the time) return true, otherwise return false
     */
    public boolean activate() {
        if (LocalDateTime.now().getYear() == this.dttmActivate.getYear()) {
            if (LocalDateTime.now().getDayOfYear() == this.dttmActivate.getDayOfYear()) {
                if (LocalDateTime.now().getHour() == this.dttmActivate.getHour()) {
                    if (LocalDateTime.now().getMinute() == this.dttmActivate.getMinute()) {
                        this.isActivated = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Return the toString
     *
     * @return String: return the string type
     */
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String formattedDateTime = dttmActivate.format(formatter);
        System.out.println("Formatted LocalDateTime : " + formattedDateTime);
        return this.name + " " + formattedDateTime + " Frequency: " + this.frequency;
    }


    /**
     * Generate all future frequency alerts
     *
     * @return a list of all future frequency alert
     */
    public ArrayList<Alert> generateFutureAlerts() {

        ArrayList<Alert> allFrequencyAlrts = new ArrayList<Alert>();
        // newDttmActivate contains new time this alert should be up
        LocalDateTime newDttmActivate = this.dttmActivate.plusMinutes(frequency);

        //Add self first
        allFrequencyAlrts.add(this);

        // Add all alerts before the deadline
        while (this.dttmEnd.isAfter(newDttmActivate)) {
            // Generate a alert before the end time
            Alert newAlert = new FrequentAlert(newDttmActivate, this.name, this.dttmEnd,
                    this.frequency, this.frequencyID);
            allFrequencyAlrts.add(newAlert);
            newDttmActivate = newDttmActivate.plusMinutes(frequency);
        }

        // Add the alert at the deadline
        if (this.dttmEnd.getMinute() == newDttmActivate.getMinute()) {
            Alert newAlert = new FrequentAlert(newDttmActivate, this.name, this.dttmEnd,
                    this.frequency, this.frequencyID);
            allFrequencyAlrts.add(newAlert);
        }

        return allFrequencyAlrts;

    }
}
