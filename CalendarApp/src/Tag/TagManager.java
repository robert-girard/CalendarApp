package Tag;

import java.io.Serializable;
import java.util.*;

/**
 * A class that stores and manages tags and events in a map.
 *
 * @author Kai Zhang
 */
public class TagManager implements Serializable {

    /**
     * A map that maps the id of tag to tag objects
     */
    private final Map<String, Tag> tags;

    /**
     * A map that maps the tag id to a list of event ids
     */
    private final Map<String, List<String>> tagMap;

    /**
     * A default constructor of TagManager
     */
    public TagManager() {
        this.tagMap = new HashMap<>();
        this.tags = new HashMap<>();
    }

    /**
     * Create and store a new Tag object
     *
     * @param tagName The name of the new tag
     */
    public Tag createTag(String tagName) {
        Tag newTag = new Tag(tagName);
        tags.put(newTag.getId(), newTag);
        return newTag;
    }

    /**
     * Pair up a tag and an event and update tagMap and tags accordingly
     *
     * @param eUUID The UUID of the event that will be tagged
     * @param tUUID The Id of the Tag object that will be associated
     */
    public void pairTag(String tUUID, String eUUID) {
        if (!tagMap.containsKey(tUUID)) {
            List<String> newTagEvents = new ArrayList<>();
            newTagEvents.add(eUUID);
            tagMap.put(tUUID, newTagEvents);
        } else if (!tagMap.get(tUUID).contains(eUUID)) {
            tagMap.get(tUUID).add(eUUID);
        }
    }


    /**
     * Get a list of event ids for a given tag
     *
     * @param tUUID The id of tag that user is interested in
     * @return A list of events ids that are associated with the given tag id
     */
    public List<String> getEventIDByTagID(String tUUID) {
        List<String> eids = tagMap.get(tUUID);
        if (eids == null) {
            return new ArrayList<>();
        }
        return eids;
    }

    /**
     * return all the tags that this manager is storing with its corresponding events in a String form
     *
     * @return An ArrayList of String representing all the tags and their associated events in the form
     * tagName: Event1 Event2
     */
    public List<Tag> getAllTags() {
        return new ArrayList<>(tags.values());
    }

    /**
     * Return the tag that has <tUUID>
     *
     * @param tUUID The tUUID of the tag
     * @return The tag that has <tUUID>
     */
    public Tag getTagByID(String tUUID) {
        return tags.get(tUUID);
    }

    /**
     * Take the name of the tag and return the id of that tag
     *
     * @param tagName The name of the tag
     * @return The id of the tag with name <tagName>, if this tag does not exist, return an empty string
     */
    public String getTagIDByTagName(String tagName) {
        for (Tag tag : tags.values()) {
            if (tag.getName().equals(tagName)) {
                return tag.getId();
            }
        }
        return "";
    }

}
