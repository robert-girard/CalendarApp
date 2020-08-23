package UI;

import App.CurrentUser;
import Exceptions.ObjectNotFoundException;
import Memo.Memo;

import java.util.List;

import Event.Event;

/**
 * A submenu UI for memo related functions
 */
public class MemoUI extends SubMenu {
    private static final String[] options = {"1: Browse Memos",
                "2: Get Event by Memo",
                "3: Edit Memo",
                "4: Delete Memo",
                "5: Add Memo to Event",
                "6: Return to Main Menu"
        };

    public MemoUI(CurrentUser currentUser) {
        super(currentUser, options);
    }

    /**
     * see SubMenu javadoc
     */
    protected void doOption(int selection) {
        switch (selection) {
            case 1:
                browseMemos();
                break;//need current time as parameter
            case 2:
                displayEventByMemo();
                break;
            case 3:
                editMemo();
                break;
            case 4:
                deleteMemo();
                break;
            case 5:
                addMemoToEvent();
        }
    }

    /**
     * Displays all memos to user
     */
    private void browseMemos() {
        List<Memo> memos = currentUser.activeCalendar.getAllMemos();
        dispHelp.displayList(memos);
        if (memos.isEmpty()) {
            System.out.println("No Memos Found");
        }
    }

    /**
     * Displays all memos associated to an event selected by user
     */
    private void displayEventByMemo() {
        List<Memo> memos = currentUser.activeCalendar.getAllMemos();
        Memo m = utilFuncs.pick(memos, "Memos");
        if (m == null)
            return;
        try {
            List<Event> events = currentUser.activeCalendar.getEventsByMemo(m);
            dispHelp.displayList(events);
        } catch (ObjectNotFoundException e) {
            System.out.println("No events found for that Memo");
        }
    }

    /**
     * UI to let user edit a memo
     */
    private void editMemo() {
        List<Memo> memos = currentUser.activeCalendar.getAllMemos();
        Memo m = utilFuncs.pick(memos, "Memos");
        if (m == null)
            return;
        System.out.println("Enter the new memo and press enter: ");
        String text = sc.nextLine();
        currentUser.activeCalendar.editMemo(m, text);
    }

    /**
     * UI to let a user delete memo
     */
    private void deleteMemo() {
        List<Memo> memos = currentUser.activeCalendar.getAllMemos();
        Memo m = utilFuncs.pick(memos, "Memos");
        if (m == null)
            return;
        currentUser.activeCalendar.removeMemo(m);
    }

    /**
     * UI to let user associate a memo to an event
     */
    private void addMemoToEvent() {
        System.out.println("Please note: if the event already contains a memo it will be overwritten and the old memo deleted if it is not used elsewhere.");
        addHelp.addMemoToEvent(currentUser.getUserName());
    }

}
