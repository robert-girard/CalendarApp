package UI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

/**
 * A utility class for common UI tasks
 */
public class UtilityUI {
    final Scanner scanner;

    public UtilityUI() {
        scanner = new Scanner(System.in);
    }

    /**
     * lets user select one item from a list
     *
     * @param list the list to select from
     * @param name name of the thing's type to be selected
     * @param <T>  the type of thing to select
     * @return the selected element
     */
    public <T> T pick(List<T> list, String name) {
        int i = 0;
        if (list.size() == 0) {
            System.out.println("There is nothing to be picked from");
            return null;
        }
        System.out.println("Please enter the number of the " + name + ".");
        for (T e : list) {
            System.out.println(i++ + ": " + e.toString());
        }
        do {
            try {
                i = scanner.nextInt();
                scanner.nextLine();  // Consume newline left-over
            } catch (InputMismatchException e) {
                scanner.nextLine();
                return null;
            }
        }
        while (i >= list.size() || i < 0);
        return list.get(i);
    }


    /**
     * Asks yes/no question and gets a users response
     *
     * @param question the yes no question to ask the user
     * @return boolean true for yes and false for no
     */
    public boolean yesNoChecker(String question) {
        System.out.println(question);
        while (true) {
            String response = scanner.nextLine();
            if (response.matches("[yY]")) {
                return true;
            } else if (response.matches("[nN]")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter a 'y' for yes or a 'n' for no");
            }
        }
    }

    /**
     * Gets a user index for selection makes sure the user input is only digits and not greater then maxIndex
     *
     * @param maxIndex the maximum allowed index the user should input
     * @return the index the user input
     */
    public int validIndexSelection(int maxIndex) {
        int index = validPosInt();
        while(index > maxIndex ) {
            System.out.println("Invalid Input. Please try Again");
            index = validPosInt();
        }
        return index;
    }

    public int validPosInt() {
        Scanner sc = new Scanner(System.in);
        Pattern p = Pattern.compile("\\d+");
        Matcher m;
        int index = -1;
        String input;

        input = sc.nextLine();
        m = p.matcher(input);
        if (m.matches()) {
            index = Integer.parseInt(input);
            return index;
        }

        while(!m.matches()) {
            System.out.println("Invalid Input. Please try Again");
            input = sc.nextLine();
            m = p.matcher(input);
            if (m.matches()) {
                index = Integer.parseInt(input);
                return index;
            }
        }
        return index;

    }


    /**
     * validates date time input to proper format
     *
     * @param dateTimeFormatter the format to enforce
     * @return the DateTime entered by user after enforcing format
     */
    public LocalDateTime getLocalDateTime(DateTimeFormatter dateTimeFormatter) {
        System.out.println("Enter the time in the format (yyyy-MM-dd HH:mm): ");
        String startTimeStr = scanner.nextLine();
        try {
            return LocalDateTime.parse(startTimeStr, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            System.out.println("Your format is wrong. Try again.");
            return getLocalDateTime(dateTimeFormatter);
        }
    }
}
