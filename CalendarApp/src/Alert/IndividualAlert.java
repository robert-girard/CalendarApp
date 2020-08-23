package Alert;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A subclass of Alert representing an individual alert that only contains one date
 */
public class IndividualAlert extends Alert implements Serializable {

    /**
     * Constructs a new individualAlert from given event
     *
     * @param dttmActivate The time to activate the alert
     */
    public IndividualAlert(LocalDateTime dttmActivate, String name) {
        super(name, dttmActivate);
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

}
