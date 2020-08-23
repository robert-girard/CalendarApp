package Exceptions;

/**
 * End date before start date exception.
 *
 * @author Zach Folan
 */
public class EndDateBeforeStartDateException extends Exception {
    /**
     * A default constructor.
     */
    public EndDateBeforeStartDateException() {
        super();
    }

    /**
     * A constructor that takes a String as a parameter to be stored as a message.
     *
     * @param msg A message for the exception.
     */
    public EndDateBeforeStartDateException(String msg) {
        super(msg);
    }
}
