package Series;

import java.io.Serializable;
import java.util.*;

/**
 * Class to manage all operations involving series
 *
 * @author Raymond Liu
 */

public class SeriesManager implements Serializable {
    private final Map<String, List<String>> seriesToEventsMap;
    private final Map<String, Series> seriesMap;

    /**
     * Constructor
     *
     */
    public SeriesManager() {
        seriesToEventsMap = new HashMap<>();
        seriesMap = new HashMap<String, Series>();
    }

    /**
     * Returns all series IDs whose name includes a search term
     *
     * @param searchString the search string
     * @return a list of series whose name includes the search term
     * <p>
     *
     */
    public ArrayList<Series> searchSeries(String searchString) {
        ArrayList<Series> seriesList = new ArrayList<>();

        for (String sUUID : seriesToEventsMap.keySet()) {
            Series series = seriesMap.get(sUUID);
            if (series.getSeriesName().contains(searchString)) {
                seriesList.add(series);
            }
        }

        return seriesList;
    }

    /**
     * Get an ArrayList of series by its name
     *
     * @param seriesName name of the series to be obtained
     * @return an ArrayList of Series
     */
    public ArrayList<Series> getSeriesByName(String seriesName) {
        ArrayList<Series> seriesList = new ArrayList<Series>();

        for (String sUUID : seriesToEventsMap.keySet()) {
            Series series = seriesMap.get(sUUID);
            if (series.getSeriesName().equals(seriesName)) {
                seriesList.add(series);
            }
        }

        return seriesList;
    }

    /**
     * Creates a new series, given a list of events
     *
     * @param eUUIDs the list of event IDs
     * @param name   the name of the series
     * @return The ID of the newly created series
     */
    public Series createSeries(List<String> eUUIDs, String name) {
        Series series = new Series(name);
        seriesToEventsMap.put(series.getId(), eUUIDs);
        seriesMap.put(series.getId(), series);
        return series;
    }

    /**
     * Returns the event IDs in a specific series, or null if the series does not exist
     *
     * @param sUUID the series ID of the specific series
     * @return A list of IDs of the event associated with the series, or null if the series does not exist
     */
    public List<String> getEventIdsFromSeries(String sUUID) {
        return seriesToEventsMap.get(sUUID);
    }

 }
