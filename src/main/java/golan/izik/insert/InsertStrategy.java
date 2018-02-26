package golan.izik.insert;

import java.util.Calendar;

public interface InsertStrategy {
    boolean shouldTruncateTableBeforeStart();

    /**
     *
     */
    String getDeviceId(int month, int day, int deviceIndex);

    /**
     * Which year to add rows to
     * This class is all about inserting data for a single year
     */
    int getYear();

    /**
     * The last day for which we want to insert data to.
     * Should be some day in the year as returned from {@link #getYear()}
     */
    Calendar getLastDay();

    /**
     * The first day for which we want to insert data to.
     * Should be some day in the year as returned from {@link #getYear()}
     */
    Calendar getFirstDay();

    /**
     * We iterate the  calendar and this is where  we decide if  we jump 1 day every iteration or two or a week...
     */
    void incrementCalendar(Calendar cal);

    /**
     * How many devices reporting in the given day
     */
    int getDeviceCountPerDay(Calendar cal);

    /**
     * How many transaction per device in the given day.
     * Value should be between 1 and 24 (including both)
     * (the table holds one row per hour)
     */
    int getTransactionCountPerDevice(Calendar cal);
}
