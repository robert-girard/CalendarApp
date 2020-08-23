package Exceptions;

/**
 * Id out of range exception.
 *
 * @author Zach Folan
 */
public class ObjectNotFoundException extends Exception {
    /**
     * A default constructor.
     */
    public ObjectNotFoundException() {
        super();
    }

    /**
     * A constructor that takes a String as a parameter to be stored as a message.
     *
     * @param msg The message associated with the exception.
     */
    public ObjectNotFoundException(String msg) {
        super(msg);
    }
}