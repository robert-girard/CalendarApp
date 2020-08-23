package CustomClock;

import java.time.*;

/**
 * A subclass of the built-in clock that allows us to fast forward and backward
 */
public class CustomClock extends Clock {
    //private Clock startClock;
    private final Clock realClock;
    private Clock customClock;
    private Duration duration = Duration.ZERO;

    /**
     * Construct a new CustomClock.CustomClock object
     */
    public CustomClock() {
        realClock = Clock.system(ZoneOffset.of("-05:00"));
        //startClock = Clock.fixed(realClock.instant(),realClock.getZone());
        customClock = Clock.offset(realClock, Duration.ZERO);
        System.out.println("systemDefaultZone()" + LocalDateTime.now(customClock));
    }
    /*
    public static void main(String[] args) {
        CustomClock.CustomClock c = new CustomClock.CustomClock();
        System.out.println(c);
        c.fastForward(10);
        c.fastForward(30);
        System.out.println(c);

        //example interaction between LocalDateTime and Clock
        LocalDateTime datetime = LocalDateTime.now(c);
        System.out.println(datetime.isBefore(datetime));
    }*/

    /**
     * Fast forward the clock by the given time in minute
     *
     * @param minutes The number of minutes to be fast forward
     */
    public void fastForward(long minutes) {
        duration = duration.plusMinutes(minutes);
        customClock = Clock.offset(realClock, duration);
    }

    /**
     * Fast backward the clock by the given time in minute
     *
     * @param minutes The number of minutes to be fast backward
     */
    public void fastBackward(long minutes) {
        fastForward(-minutes);
    }

    /**
     * Turn this clock into a string
     *
     * @return The string representation of this clock
     */
    @Override
    public String toString() {
        return customClock.instant().toString();
    }

    /**
     * Get the zone of this clock
     *
     * @return The zone of this clock
     */
    @Override
    public ZoneId getZone() {
        return customClock.getZone();
    }

    /**
     * Get the instant of this clock
     *
     * @return The instant of this clock
     */
    @Override
    public Instant instant() {
        return customClock.instant();
    }

    /**
     * Set this clock to have the given zone
     *
     * @param zone The zone id of the new zone
     * @return The new Clock with the given zone
     */
    @Override
    public Clock withZone(ZoneId zone) {
        return customClock.withZone(zone);
    }
}
