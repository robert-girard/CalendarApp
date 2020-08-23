package Memo;

import Event.Event;
import Exceptions.ObjectNotFoundException;

import java.io.Serializable;
import java.util.*;

/**
 * A class that contains and interacts with memos' ids, memos objects and events' ids
 */
public class MemoManager implements Serializable {

    final Map<String, String> MemoMap; //map<eventID, Memo>
    final Map<String, Memo> Memos;

    /**
     * Constructor
     */
    public MemoManager() {
        MemoMap = new HashMap<>();
        Memos = new HashMap<String, Memo>();
    }

    /**
     * Takes a memo and associates it to event
     * NOTE: at most one memos can be associated to an event and many events to a memo. if an event already has a memo, this function will overwrite it and delete the old
     *
     * @param eID  the ID of the Event to be associated with the memor
     * @param memo the String contained in the memo
     */
    public void associateMemoToEvent(Memo memo, String eID) {
        String oldMemoId = MemoMap.remove(eID);
        if (oldMemoId != null && !MemoMap.containsValue(oldMemoId)) {  //clean up by deleting overwritten memo if it isnt used on another event
            Memos.remove(oldMemoId);
        }
        MemoMap.put(eID, memo.getId());
        Memos.put(memo.getId(), memo);
    }

    /**
     * will remove a memo from existence and delete all associations of it to events
     *
     * @param mID the ID of the memo you are to delete
     */
    public void deleteMemo(String mID) {
        boolean foundMemo = false;
        for (String eID : MemoMap.keySet()) {
            if (MemoMap.get(eID).equals(mID)) {
                MemoMap.remove(eID);
                foundMemo = true;
                break;
            }
        }
        if (foundMemo) {
            deleteMemo(mID);
        } else {
            Memos.remove(mID);
        }
    }

    /**
     * Allows for modification of a memo
     *
     * @param mID     The ID of the Memo.Memo you want to modify
     * @param newText the text the memo will be updated to
     */
    public void updateMemo(String mID, String newText) {
        Memo memo = Memos.get(mID);
        memo.setMemoString(newText);
        Memos.put(mID, memo);       // since you are getting a ref to the memo do you really need to put it back?
    }


    /**
     * Since one memo can be associated to many events This function will find all the events (IDs) for a given memoID
     *
     * @param MemoID The ID of the memo that you are trying to find event with
     * @return List of all event IDs that are associated to the memo.
     */
    public List<String> getEventIDsByMemoID(String MemoID) {
        List<String> EventIDs = new ArrayList<>();

        for (String eID : MemoMap.keySet()) {
            if (MemoMap.get(eID).equals(MemoID)) {
                EventIDs.add(eID);
            }
        }
        return EventIDs;
    }

    /**
     * Handle Dissociation of Memo from Event and deletes the memo if not used elsewhere
     *
     * @param e The Event being deleted
     * @throws ObjectNotFoundException
     */
    public void handleEventDeletion (Event e) throws ObjectNotFoundException {
        if (!MemoMap.containsKey(e.getId())) {
            throw new ObjectNotFoundException();
        }
        String memoId = MemoMap.remove(e.getId());
        if (!MemoMap.values().contains(memoId)) {
            Memos.remove(memoId);
        }
    }

    /**
     * Getter for all memos in the manager
     *
     * @return A set of all the memos in the Manager
     */
    public List<Memo> getAllMemos() {
        return new ArrayList<Memo>(Memos.values());
    }

}
