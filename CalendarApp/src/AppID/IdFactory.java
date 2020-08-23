package AppID;

import java.util.UUID;

/**
 * Handles the creation of unique Ids for the whole app.
 *
 * @author Zach Folan
 */
public class IdFactory {
    /**
     * Creates and returns a new unique Id.
     *
     * @return A new unique Id.
     */
    public static String newUniqueId() {
        return UUID.randomUUID().toString();
    }
}
