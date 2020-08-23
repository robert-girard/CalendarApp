package Alert;

import Event.Event;
import Exceptions.ObjectNotFoundException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Manages all alerts that have been created
 */
public class AlertManager implements Serializable {

    /**
     * A Map containing all Alerts with Alert Id's as keys and an Alert as the value.
     */
    private final Map<String, Alert> Alerts;
    /**
     * A Map with event's id as keys and a list of alert's id
     */
    private final Map<String, List<String>> EventIDtoAlertID;


    /**
     * Construct a new AlertManager
     */
    public AlertManager() {
        EventIDtoAlertID = new HashMap<>();
        Alerts = new HashMap<String, Alert>();
    }

    /**
     * Associates an Alert to an event. Since an event can have many alerts this is added to the list of alerts for the event. New alerts will be added to the alert repository
     *
     * @param eventId the eventId to which the alert will be associated
     * @param alert the alert to be associated to the event
     */
    private void addAlertEvent(String eventId, Alert alert) {
        if (!Alerts.containsKey(alert.getId())) {
            Alerts.put(alert.getId(), alert);
        }
        if (EventIDtoAlertID.containsKey(eventId)) {
            // The event has some alerts already
            List<String> aIDs = EventIDtoAlertID.get(eventId);
            aIDs.add(alert.getId());
            EventIDtoAlertID.put(eventId, aIDs);
        } else {
            // The event has no alerts previously
            List<String> aIDs = new ArrayList<>();
            aIDs.add(alert.getId());
            EventIDtoAlertID.put(eventId, aIDs);
        }
    }

    /**
     * Adds an Alert to Alerts using the Alert Id as the key. ADDING TO ALERTS
     *
     * @param alert The Alert that will be added to Alerts.
     */
    public void addAlert(Alert alert) {
        this.Alerts.put(alert.getId(), alert);
    }


    /**
     *  Removes an alert from an event without deleting alert
     *
     * @param eventId the event from which the alert will be removed
     * @param alert the alert that will be removed
     */
    private void deleteAlertEvent(String eventId, Alert alert) {

        if (EventIDtoAlertID.containsKey(eventId)) {
            // The event has some alerts already
            List<String> aIDs = EventIDtoAlertID.get(eventId);
            aIDs.remove(alert.getId());
        }

    }

    /**
     * Delete an Alert to Alerts using the Alert Id as the key. DELETING FROM ALERTS
     *
     * @param alert The Alert that will be added to Alerts.
     */
    private boolean deleteAlert(Alert alert) {
        if (Alerts.containsKey(alert.getId())) {
            this.Alerts.remove(alert.getId(), alert);
            return true;
        } else {
            return false;
        }

    }


    /**
     * Create a new individual alert
     *
     * @param eventId The event's id this alert belongs to
     */
    public void createIndividualAlert(IndividualAlert ia, String eventId) {
        // Adding the new alert to Alerts map
        addAlert(ia);
        // Adding the new alert to EventIDtoAlertID map
        addAlertEvent(eventId, ia);

    }

    /**
     * Create a new frequency alert and all future alerts will be generated
     *
     * @param name         The name of event that the alert belongs to
     * @param dttmEnd      The time the frequency ends
     * @param frequency    The frequency that alert repeats
     * @param dttmActivate The time this alert activate
     */
    public Alert createFrequencyAlert(
            LocalDateTime dttmEnd, LocalDateTime dttmActivate, String name, long frequency, String eventId) {

        FrequentAlert newFrequencyAlert = new FrequentAlert(dttmActivate, name, dttmEnd, frequency);

        //ArrayList<String> alertIds = new ArrayList<String>();
        ArrayList<Alert> futureAlerts = new ArrayList<Alert>();

        // Add the new created alert
        futureAlerts.add(newFrequencyAlert);
        // Contains all future alerts
        futureAlerts.addAll(newFrequencyAlert.generateFutureAlerts());

        for (Alert alert : futureAlerts) {
            // Adding the new alert to Alerts map
            addAlert(alert);

            // Adding the new alert to EventIDtoAlertID map
            //alertIds.add(alert.getId());
            addAlertEvent(eventId, alert);
        }

        return newFrequencyAlert;

    }


    /**
     * Delete a single alert(either individual or frequent) from the existing alerts by taking the alertID
     *
     * @param alertID The unique id of alert that the alert has
     * @param eventID the unique id of event that the alert belongs to
     */
    public void deleteSingleAlert(String alertID, String eventID) throws ObjectNotFoundException {

        if (this.Alerts.containsKey(alertID)) {
            Alert alert = Alerts.get(alertID);
            // delete the alert from both maps
            deleteAlertEvent(eventID, alert);
            boolean isDelete;
            isDelete = deleteAlert(alert);

            if (!isDelete) {
                throw new ObjectNotFoundException("There is no such alert");
            }
        } else {
            throw new ObjectNotFoundException("There is no such alert.");
        }

    }


    /**
     * Delete a single alert(either individual or frequent) from the existing alerts by taking the alertID
     *
     * @param alertID The unique id of event that the alert belongs to
     */
    public void deleteSingleAlert(String alertID) throws ObjectNotFoundException {

        if (this.Alerts.containsKey(alertID)) {
            Alert alert = Alerts.get(alertID);
            // delete the alert from both maps
            //deleteAlertEvent(eventID, alert);
            if (this.findAlertEventId(alertID) != null) {
                String eventID = this.findAlertEventId(alertID);
                this.deleteAlertEvent(eventID, alert);
                this.deleteAlert(alert);
            }
        } else {
            throw new ObjectNotFoundException("There is no such alert.");
        }

    }

    /**
     * Private helper
     * <p>
     * Helps to find the eventID that the alert belongs to
     */
    private String findAlertEventId(String alertID) {
        Set<String> allEventIds = this.EventIDtoAlertID.keySet();
        for (String id : allEventIds) {
            List<String> allAlerts = this.EventIDtoAlertID.get(id);
            if (allAlerts.contains(alertID)) {
                return id;
            }
        }
        return null;

    }

    /**
     * Delete the whole frequency of FrequentAlert from the existing alerts by taking the alertID
     *
     * @param alertID The unique id of event that the alert belongs to
     */
    public void deleteFrequentAlert(String alertID, String eventID) throws ObjectNotFoundException {

        if (this.Alerts.containsKey(alertID)) {
            FrequentAlert alert = (FrequentAlert) Alerts.get(alertID);
            ArrayList<FrequentAlert> allAlerts = new ArrayList<FrequentAlert>();

            for (String eID : Alerts.keySet()) {
                Alert thisAlert = Alerts.get(eID);

                if (thisAlert instanceof FrequentAlert) { // Start of outer loop
                    if (((FrequentAlert) thisAlert).getFrequencyID().equals(alert.getFrequencyID())) { // Start of inner loop
                        allAlerts.add((FrequentAlert) thisAlert);
                    } // End of inner loop
                } // End of outer loop

            }

            for (FrequentAlert alert1 : allAlerts) {
                deleteAlertEvent(eventID, alert1);
                deleteAlert(alert1);
            }
        } else
            throw new ObjectNotFoundException("There is no such alert.");


//        for (String eID : EventIDtoAlertID.keySet()) {
//            EventIDtoAlertID.get(eID).removeAll(Collections.singleton(alertID));
//            //NOTE: umm err hope this is the reference not a copy else Alert wont be deleted mmmK
//        }
    }


    /**
     * Getter all alerts
     *
     * @return ArrayList of alerts
     */
    public List<Alert> getAllAlerts() {
        if (Alerts.isEmpty()) {
            return new ArrayList<Alert>();
        }
        return new ArrayList<Alert>(Alerts.values());
    }

    /**
     * Getter all alerts from an event
     *
     * @param eventId The event id
     * @return ArrayList of alerts
     */
    public ArrayList<Alert> getAlertFromEvent(String eventId) throws ObjectNotFoundException {
        if (this.EventIDtoAlertID.containsKey(eventId)) {
            List<String> alertsIds = this.EventIDtoAlertID.get(eventId);
            ArrayList<Alert> alerts = new ArrayList<Alert>();
            for (String id : alertsIds) {
                alerts.add(this.Alerts.get(id));
            }
            return alerts;
        } else {
            throw new ObjectNotFoundException("There is no alerts to this event.");
        }

    }

    /**
     * gets an alert by ID. If Alert cant be found. throws an ObjectNotFound Exception
     *
     * @param alertId The AlertId that identifies the alert to be found
     * @return the alert that was stored by ID
     * @throws ObjectNotFoundException when alertID is not valid
     */
    public Alert getAlert(String alertId) throws ObjectNotFoundException {
        Alert a = Alerts.get(alertId);
        if (a == null) {
            throw new ObjectNotFoundException();
        }
        return a;
    }

    /**
     * Handle Dissociation of Alert from Event and deletes the Alert if not used elsewhere
     *
     * @param e The Event being deleted
     * @throws ObjectNotFoundException
     */
    public void handleEventDeletion (Event e) throws ObjectNotFoundException {
        if (!EventIDtoAlertID.containsKey(e.getId())) {
            throw new ObjectNotFoundException();
        }
        List<String> AlertIds = EventIDtoAlertID.remove(e.getId());

        for (String aId : AlertIds) {
            boolean foundAlertId = false;
            for (String eId : EventIDtoAlertID.keySet()) {
                if (EventIDtoAlertID.get(eId).contains(aId)) {
                    foundAlertId = true;
                }
            }
            if (!foundAlertId) {
                Alerts.remove(aId);
            }
        }
    }

}
