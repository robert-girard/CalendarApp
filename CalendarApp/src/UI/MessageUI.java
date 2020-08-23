package UI;

import App.*;
import Calendar.CalendarInterface;
import Event.Event;
import Message.Message;
import Message.Shareable;
import Message.ShareableCalendar;
import Message.ShareableEvent;

import java.util.Scanner;

public class MessageUI extends UserAction {
    private final Messaging messaging;
    private final Scanner scanner;
    private final UtilityUI util;

    public MessageUI(CurrentUser user) {
        super(user);
        messaging = new Messaging();
        scanner = new Scanner(System.in);
        util = new UtilityUI();
    }

    /**
     * Allow user to send message
     */
    public void sendMessage(HasUser hasUser) {

        System.out.println("Who would you like to send a message to?");
        String recipientName = scanner.nextLine();
        while (!hasUser.hasUser(recipientName)) {
            System.out.println("There doesn't seem to be a user named: " + recipientName);
            System.out.println("Who you you like to send a message to?");
            recipientName = scanner.nextLine();
        }
        System.out.println("Enter the Message you would like to send.");
        String txt = scanner.nextLine();

        String username = currentUser.getUserName();
        Message msg = new Message(txt, username, recipientName);

        System.out.println("Would you like to share something with this message");
        System.out.println("1: Yes, a calendar\n2: Yes, an Event"); // prepend "0: No\n" for messages without sharing

        int option = scanner.nextInt();
        scanner.nextLine();

        boolean valid = false;

        while (!valid) {
            if (option == 1) {
                CalendarInterface calendarInterface = util.pick(currentUser.getCalendars(), "calendar");
                //todo multiple calendars for users need to happen before this.
                if (calendarInterface != null) {
                    messaging.sendShareable(new ShareableCalendar(msg, calendarInterface));
                }
                valid = true;
            } else if (option == 2) {
                Event event = util.pick(currentUser.getAllEvents(), "event");
                if (event != null) {
                    messaging.sendShareable(new ShareableEvent(msg, event));
                }
                valid = true;
            } else {
                System.out.println("Invalid input. please try again");
            }
        }
    }

    /**
     * Allow user to read the unread message
     */
    public void readUnread() {
        String s;
        int counter = 1;
        messaging.receiveMessages(currentUser);
        final int unread = currentUser.inbox.numUnread();
        boolean cont = true;

        s = String.format("You have %d unread messages.", unread);
        System.out.println(s);

        while (counter <= unread && cont) {
            Shareable m = currentUser.inbox.readFirstUnread();
            s = String.format("Message %d is from %s:", counter, m.sender());
            System.out.println(s);
            System.out.println(m.message() + "\n");
            addShared(m);
            counter++;

            if (counter > unread) {
                break;
            }
            s = String.format("You have %d unread messages remaining.\n Continue to next message (y/n)?", unread - counter + 1);
            cont = util.yesNoChecker(s);
        }
    }


    /**
     * UI to give user option to add a shared item to their account/active calendar
     *
     * @param shared the shared item to add
     */
    private void addShared(Shareable shared) {
        boolean addshared;
        switch (shared.whatAmI()) {
            case CALENDAR:
                addshared = util.yesNoChecker("The message contained a shared calendar would you like to add it to your account (y/n)?");
                break;
            case EVENT:
                addshared = util.yesNoChecker("The message contained a shared event would you like to add it to your current calendar (y/n)?");
                break;
            default:
                return;
        }
        if (addshared) {
            shared.share(currentUser);
        }

    }
}
