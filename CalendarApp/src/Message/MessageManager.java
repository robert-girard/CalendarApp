package Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains undelivered messages for storing loading in persistent memory
 */
public class MessageManager implements Serializable {

    //MessageId to Message Maps
    private final Map<String, List<Shareable>> undelivered;


    public MessageManager() {
        this.undelivered = new HashMap<String, List<Shareable>>();
    }

    /**
     * Adds a message to the undelivered messages. Called when a message is sent.
     *
     * @param sharedItem The message and contents to store
     */
    public void send(Shareable sharedItem) {
        String recipient = sharedItem.recipient();
        List<Shareable> msgs;
        if (undelivered.containsKey(recipient)) {
            msgs = undelivered.get(recipient);
        } else {
            msgs = new ArrayList<Shareable>();
        }
        msgs.add(sharedItem);
        undelivered.put(recipient, msgs);

    }


    /**
     * Checks for any undelivered messages and returns them. Called when a user wants to get messages to them that have not been delivered yet
     *
     * @param userName the user name of the recipient of the messages
     * @return a list of all messages to that recipient
     */
    public List<Shareable> receive(String userName) {
        if (!undelivered.containsKey(userName)) {
            return new ArrayList<Shareable>();
        }
        return undelivered.remove(userName);
    }
}
