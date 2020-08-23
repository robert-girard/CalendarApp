package AppID;

import java.io.Serializable;

/**
 * An abstract class that contains the methods for storing and getting the Id of an item.
 *
 * @author Zach Folan
 */
public abstract class KeyedEntity implements Serializable {
    /**
     * The unique Id of the Entity.
     */
    private final String id;

    /**
     * A default constructor which creates and stores a new UUID using AppID.IdFactory.
     */
    public KeyedEntity() {
        this.id = IdFactory.newUniqueId();
    }

    /**
     * Returns the unique Id of this Entity.
     *
     * @return The unique id of this Entity.
     */
    public String getId() {
        return id;
    }
}
