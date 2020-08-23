package App;

import Loader.Loader;
import Message.MessageManager;
import Message.Shareable;

import java.io.IOException;

/**
 * Gateway class for messaging feature. Handles saving/loading undelivered messages and delivery of undeliviered messages to the current users inbox.
 */
public class Messaging {
    private MessageManager messageManager;
    private final String outerPath = ""; //File.separator + "phase2" + File.separator + "CalendarApp" + File.separator + "resources" + File.separator;

    public Messaging () {
        try {
            messageManager =  (MessageManager) Loader.load(outerPath + "Messages.ser");
        } catch (Exception i) {
            messageManager = new MessageManager();
        }
    }

    /**
     * Send a sharable object
     *
     * @param s The object to be sent
     */
    public void sendShareable(Shareable s) {
        messageManager.send(s);
        try {
            Loader.save(outerPath + "Messages.ser", messageManager);
        } catch (IOException e) {
            System.out.println("Fail to save the Messages");
        }
    }

    /**
     * Receive massages for a given user
     *
     * @param user The user to receive messages
     *
     */
    public void receiveMessages(CurrentUser user) {
        String username = user.getUserName();
        user.inbox.receive(messageManager.receive(username));
    }
}
