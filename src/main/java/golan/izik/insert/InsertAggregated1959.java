package golan.izik.insert;

import java.util.Calendar;

/**
 * Insert aggregated data for the year 1959. All days.
 * We want a specifically defined data over the year so we can use it for correctness test.
 * Each day we have "X" devices reporting.
 * Each device reports "Y" hours that day (first "Y" hours)
 * The values of X and Y are date dependant on day and month and we guarantee a different device count every day
 */
public class InsertAggregated1959 implements InsertStrategy {

    public static void main(String[] args) throws InterruptedException {
        new InsertToAggregatedTable(new InsertAggregated1959()).insert();
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return true;
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
    public int getTransactionCountPerDevice(Calendar cal) {
        return 2;
    }

    @Override
    public void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, 1);
    }

    @Override
    public String getDeviceId(int month, int day, int deviceIndex) {
        return "device_"+getYear()+"_"+month+"_"+day+"_"+deviceIndex;
    }

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.DECEMBER, 31);
        return cal;
    }

    @Override
    public Calendar getFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.JANUARY, 1, 1, 0);
        return cal;
    }
}
