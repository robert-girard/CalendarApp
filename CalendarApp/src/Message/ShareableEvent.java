package Message;

import Event.Event;

/**
 * user case class for a shareable Event
 * contains the event, message, and method to to the shareit
 */
public class ShareableEvent extends Shareable {
    private final Event e;

    public ShareableEvent(Message msg, Event e) {
        super(msg);
        this.e = e;
    }

    public Event getEvent() {
        return e;
    }

    public void share(Share app) {
        app.addEvent(this.e);
        //todo cal.receiveMessage()  //ie add to inbox.... we want to give user option to accept so must provide a function to use used by consoleapp (residing in )
    }

    public SharedItems whatAmI() {
        return SharedItems.EVENT;
    }
}
