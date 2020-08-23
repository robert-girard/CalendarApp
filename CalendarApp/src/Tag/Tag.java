package Tag;

import AppID.KeyedEntity;

import java.io.Serializable;

/**
 * A class representing a tag that contains its name and id
 *
 * @author Kai Zhang
 */
public class Tag extends KeyedEntity implements Serializable {
    /**
     * The name of the tag
     */
    private String name;

    /**
     * Construct a new tag with the given name
     *
     * @param name The name of the tag used for this tag
     */
    public Tag(String name) {
        super();
        this.name = name;
    }

    /**
     * Set tag name with the given name
     *
     * @param name The new name of ths tag that will replace the original name
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Get name of this tag
     *
     * @return The name of this tag
     */
    String getName() {
        return name;
    }

    /**
     * Override the toString method so that it is easier to print tag
     *
     * @return A string representation of this tag
     */
    @Override
    public String toString() {
        return "Tag name:'" + name;
    }
}
