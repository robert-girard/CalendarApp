package Message;

import java.io.Serializable;
import java.util.*;

/**
 * Inbox class that stores users messages
 */
public class Inbox implements Serializable, InboxInterface {
    //MessageId to Message Maps
    private final List<Shareable> newMessages;
    private final Map<String, Message> oldMessages;

    public Inbox() {
        this.newMessages = new ArrayList<Shareable>();
        this.oldMessages = new HashMap<String, Message>();
    }

    /**
     * Ads messages to users inbox
     *
     * @param msgs shareables to add to users inbox
     */
    public void receive(List<Shareable> msgs) {
        newMessages.addAll(msgs);
    }

    /**
     * gets the first shareable the the inbox
     *
     * @return shareable the first unread sharable in the inbox
     *
     * @throws IndexOutOfBoundsException thrown if no more unread
     */
    public Shareable readFirstUnread() throws IndexOutOfBoundsException {
        return newMessages.remove(0);
    }

    /**
     *
     *
     * @return int the number of unread messages
     */
    public int numUnread() {
        return newMessages.size();
    }



}
