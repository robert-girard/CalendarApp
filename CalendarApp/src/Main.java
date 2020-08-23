import UI.CalendarConsole;

/**
 * The main class that only contains the main method
 */
public class Main {

    public static void main(String[] arg) {
        CalendarConsole calendarConsole = new CalendarConsole();
        try {
            calendarConsole.run();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
