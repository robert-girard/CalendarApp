package UI;

import App.CurrentUser;
import java.util.Observable;
import java.util.Observer;

/**
 * Super class for any UI selected action.
 * Observer pattern keeps the class apprised of changing users
 */
public class UserAction implements Observer {
    protected CurrentUser currentUser;

    public UserAction(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.currentUser = (CurrentUser) arg;
    }
}
