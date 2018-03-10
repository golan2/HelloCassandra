package golan.izik.insert;

import java.util.Calendar;

/**
 * Insert aggregated data for the year 1959. All days.
 * We want a specifically defined data over the year so we can use it for correctness test.
 * Each day we have "X" devices reporting.
 * Each device reports "Y" hours that day (first "Y" hours)
 * The values of X and Y are date dependant on day and month and we guarantee a different device count every day
 */
public class InsertHourlyAggregated1959 extends AbsInsertHourlyAggregated {

    public static void main(String[] args) throws InterruptedException {
        new InsertToAggregatedTable(new InsertHourlyAggregated1959()).insert();
    }

    @Override
    public int getYear() {
        return 1959;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    @Override
    public int getDailyRowsCountPerDevice(Calendar cal) {
        return 2;
    }

}
