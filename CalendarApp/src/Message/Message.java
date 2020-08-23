package Message;

import AppID.KeyedEntity;

import java.io.Serializable;


/**
 * The entity class for a message.
 */
public class Message extends KeyedEntity implements Serializable {
    private final String message;
    private final String senderName;
    private final String recipientName;

    public Message(String message, String senderName, String recipientName) {
        this.message = message;
        this.senderName = senderName;
        this.recipientName = recipientName;
    }


    /**
     * Gets the message text
     *
     * @return message txt
     */
    public String getMessage() {
        return message;
    }

    /**
     * gets the naem of the person the message is going to
     *
     * @return the name of the person the message is going to
     */
    public String getRecipientName() {
        return recipientName;
    }

    /**
     * gets the the name of the sender
     *
     * @return the name of the sender
     */
    public String getSenderName() {
        return senderName;
    }
}
