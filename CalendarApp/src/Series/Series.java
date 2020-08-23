package Series;

import AppID.KeyedEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Class for the series Entity
 * <p>
 * Series.Series contains a list of events, associated together by a series name
 *
 * @author Raymond Liu
 */
public class Series extends KeyedEntity implements Serializable {
    private String seriesName;
    private final LocalDateTime creationTime;

    /**
     * creates a series object
     *
     * @param seriesName the name of the series to be created
     */
    public Series(String seriesName) {
        this.seriesName = seriesName;
        this.creationTime = LocalDateTime.now();
    }

    /**
     * convert the series into a String to be displayed when needed
     *
     * @return a series in String form
     */
    @Override
    public String toString() {
        return "Series.Series{" +
                "seriesName='" + seriesName + '\'' +
                ", creationTime=" + creationTime +
                '}';
    }


    /**
     * Getter for seriesName
     *
     * @return series name
     */
    public String getSeriesName() {
        return seriesName;
    }

    /**
     * Setter for the series name
     *
     * @param seriesName the new name of the series
     */
    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }
}
