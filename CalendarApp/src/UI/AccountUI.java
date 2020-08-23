package UI;

import App.AccountAccess;
import App.CurrentUser;
import App.HasUser;
import Calendar.CalendarInterface;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

/**
 * A class for user interface actions related to Accounts
 */
public class AccountUI extends Observable {
    private final Scanner scanner;
    private final AccountAccess Accounts;
    private final UtilityUI utilUI;

    /**
     *
     */
    public AccountUI() {
        Accounts = new AccountAccess();
        scanner = new Scanner(System.in);
        utilUI = new UtilityUI();
    }

    /**
     * User interface for logging in
     * notifies other UI classes of the current user so that their actions are based on that user
     *
     * @return an instance of current user gotten by logging in
     */
    public CurrentUser logIn() {
        CurrentUser currentUser = null;
        boolean valid = false;
        while (!valid) {
            System.out.println("Welcome to csc207 Calendar.Calendar.\n" +
                    "If you would like to create an account, input 1;\n" +
                    "To log into an existing account, input 2.");
            String in = scanner.nextLine();
            String userName, passWord;
            System.out.println("Please enter your username: ");
            userName = scanner.nextLine();
            System.out.println("Please enter your password: ");
            passWord = scanner.nextLine();
            if (in.equals("1")) {
                currentUser = newAccountLogIn(userName, passWord);
                if (currentUser != null) {
                    valid = true;
                }
            } else if (in.equals("2")) {
                currentUser = existingAccountLogIn(userName, passWord);
                if (currentUser != null) {
                    valid = true;
                }
            } else {
                System.out.println("Seems you entered invalid input, please try again :)");
            }
        }
        setChanged();
        notifyObservers(currentUser);
        return currentUser;
    }

    /**
     * UI to create a new account including default calendar
     * default calendar will be te active one
     *
     * @param userName username of the new account to be created
     * @param password password of the new account to be created
     * @return a current user object of the new user
     */
    private CurrentUser newAccountLogIn(String userName, String password) {
        System.out.println("You will be creating a new account");
        System.out.println("Enter the name of your first calendar: ");
        String calendarName = scanner.nextLine();
        return Accounts.createNewAccount(userName, password, calendarName);
    }

    /**
     * UI to log into an existing account
     * User must also select which calendar will be active upon login
     *
     * @param username username of the account to be logged into
     * @param password password of the account to be logged into
     * @return the current account object of the existing user if login was successful or a null object if not
     */
    private CurrentUser existingAccountLogIn(String username, String password) {
        System.out.println("You will be logging to an existing account");
        CurrentUser currentUser = Accounts.Login(username, password);

        if (currentUser == null) {
            System.out.println("Login Failed. Username and/or password invalid\n");
            return null;
        } else {
            System.out.println("Alright, choose a calendar to load by entering the index");
            CCDisplayHelper disphelp = new CCDisplayHelper(currentUser);
            List<CalendarInterface> cals = currentUser.getCalendars();
            disphelp.displayCalendars(cals);
            int index = utilUI.validIndexSelection(cals.size()-1);
            String userID = currentUser.getUserId();
            currentUser.LoadUserCalendar(userID, index);
            return currentUser;
        }
    }

    /**
     * Logs a current user out and stops any time based activities
     *
     * @param currentUser the user to logout
     */
    public void logOut(CurrentUser currentUser) {
        System.out.println("You are logging out");
        currentUser.stopCalendar();
        Accounts.logOut(currentUser);
    }

    /**
     * Deletes a user account
     *
     * @param currentUser the account to delete
     */
    public void deleteAccount(CurrentUser currentUser) {
        currentUser.stopCalendar();
        Accounts.logOut(currentUser);
        Accounts.deleteCurrentUserAccount(currentUser.getUserId());
    }

    /**
     * UI to let the user create a new account
     *
     * @param currentUser the current user
     */
    public void createNewCalendar(CurrentUser currentUser) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter a name for your new calendar");
        String calendarName = sc.nextLine();
        Accounts.createNewCalendar(calendarName, currentUser);
        System.out.println("Your new calendar has been created");
    }

    /**
     * provides an interface for checking account existence
     *
     * @return interface for checking account existence
     */
    public HasUser userChecker() {
        return Accounts;
    }

}
