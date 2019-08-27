package com.github.knoweth.client.util;

import org.threeten.bp.Duration;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility functions to deal with Dates and Durations.
 *
 * This is tragically necessary because Java 8's time API is not implemented
 * by TeaVM, and other similar implementations (e.g. threetenbp, Joda Time) use
 * ConcurrentHashMap, which is also not implemented in TeaVM. Thus, we're stuck
 * with the good old Java Date.
 */
public class DateUtil {
    public static boolean sameDay(Date one, Date two) {
        // This may seem horrible, but according to StackOverflow, this is
        // actually faster than the Calendar implementation.
        // https://stackoverflow.com/questions/2517709/comparing-two-java-util-dates-to-see-if-they-are-in-the-same-day
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(one).equals(fmt.format(two));
    }

    public static Date addDuration(Date date, Duration duration) {
        return new Date(date.getTime() + duration.toMillis());
    }

    public static Date subDuration(Date date, Duration duration) {
        return new Date(date.getTime() - duration.toMillis());
    }
}
