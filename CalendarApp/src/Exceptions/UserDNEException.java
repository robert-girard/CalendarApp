package Exceptions;

/**
 * User Does Not Exist Exception
 */
public class UserDNEException extends Exception {
    /**
     * if the user does not exist (no message)
     */
    public UserDNEException() {
        super();
    }

    /**
     * if user does not exist
     *
     * @param msg the message to be displayed
     */
    public UserDNEException(String msg) {
        super(msg);
    }
}
