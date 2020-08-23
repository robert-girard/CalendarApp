package Loader;//please note much of the serialization code was flagrantly stolen from https://www.tutorialspoint.com/java/java_serialization.htm

import java.io.*;

/**
 * A loader class that loads information of calendars and users
 */
public class Loader {

    /**
     * Stores objects in serialized format
     *
     * @param path             the file to save to
     * @param persistentObject The object to save
     * @throws IOException throws when file not found
     */
    public static void save(String path, Object persistentObject) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(persistentObject);
        out.close();
        fileOut.close();
    }

    /**
     * Retrieves the repository of calendars
     *
     * @param path The oath of the file
     * @return A Calendar.CalendarManager object created from the file
     * @throws IOException            The exception of not finding the input or output file
     * @throws ClassNotFoundException The exception of not finding the target class
     */
    public static Object load(String path) throws IOException, ClassNotFoundException {
        Object persistentObject;
        FileInputStream fileIn = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        persistentObject = in.readObject();
        in.close();
        fileIn.close();
        return persistentObject;
    }


}
