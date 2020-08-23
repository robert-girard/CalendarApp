package UI;

import App.CurrentUser;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Abstract class to create a backbone for all SubMenus
 */
public abstract class SubMenu extends UserAction {
    protected final Scanner sc;
    protected final CCDisplayHelper dispHelp;
    protected final CCAddHelper addHelp;
    protected final CCSearchHelper searchHelp;
    protected final UtilityUI utilFuncs;
    protected final String[] options;
    protected final int exitIndex;
    protected final DateTimeFormatter dateTimeFormatter;


    public SubMenu(CurrentUser currentUser, String[] options) {
        super(currentUser);
        sc = new Scanner(System.in);
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        dispHelp = new CCDisplayHelper(currentUser);
        addHelp = new CCAddHelper(currentUser, dateTimeFormatter);
        searchHelp = new CCSearchHelper(currentUser, dateTimeFormatter);
        utilFuncs = new UtilityUI();
        this.options = options;
        exitIndex = options.length;
    }

    /**
     * Displays the menu options
     */
    protected void displayMenu() {
        for (int i = 0; i < options.length / 2 * 2; i += 2) {
            System.out.printf("%-40s %-40s\n", options[i], options[i + 1]);
        }
        if (options.length % 2 == 1) {
            System.out.println(options[options.length - 1]);
        }
    }

    /**
     * gets the user selection of the menu options
     *
     * @return index of the menu option selection
     */
    protected int getMenuOption() {
        int selection = sc.nextInt();
        sc.nextLine();  // Consume newline left-over
        return selection;
    }

    /**
     * Abstract method to be implemented that will call the action selected by the user
     *
     * @param selection the index of the user selected menu option
     */
    abstract protected void doOption(int selection);

    /**
     * Calls the menu UI and performs the action until exit requested from submenu
     */
    public void menu() {
        int selection;
        do {
            displayMenu();
            selection = getMenuOption();
            doOption(selection);
        } while (selection != exitIndex);
    }

}
