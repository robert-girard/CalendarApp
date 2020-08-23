package Message;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to assocaite inbox to specific user
 * manager can be stored and loaded in persistant memory
 * users can only have one inbox and inbox are not shared so the relation is one-to-one
 */
public class InboxManager implements Serializable {
    private final Map<String, Inbox> UidToInbox;

    public InboxManager() {
        UidToInbox = new HashMap<String, Inbox>();
    }

    /**
     * Adds an userId association
     *
     * @param uId the user id to associate with
     * @param inbox the inbox to be stored
     */
    public void putInbox(String uId, Inbox inbox) {
        UidToInbox.put(uId, inbox);
    }

    /**
     * gets an inbox based on the user ID
     *
     * @param uId the user id to get the inbox of
     * @return the inbox of the specific user
     */
    public Inbox getInbox(String uId) {
        return UidToInbox.get(uId);
    }
}
