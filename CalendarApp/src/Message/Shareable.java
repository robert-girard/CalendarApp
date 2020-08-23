package Message;

import java.io.Serializable;

/**
 * super class for any shareable content
 */
public abstract class Shareable implements Serializable {
    private final Message msg;

    public Shareable(Message msg) {
        this.msg = msg;
    }

    public abstract void share(Share app);

    public abstract SharedItems whatAmI();

    public String recipient() {
        return msg.getRecipientName();
    }

    public String sender() {
        return msg.getSenderName();
    }

    public String message() {
        return msg.getMessage();
    }
}
