package Message;

import java.util.List;

/**
 * provides inbox Interface barrier layer access to inbox
 *
 */
public interface InboxInterface {

    void receive(List<Shareable> msgs);

    Shareable readFirstUnread() throws IndexOutOfBoundsException;

    int numUnread();
}
