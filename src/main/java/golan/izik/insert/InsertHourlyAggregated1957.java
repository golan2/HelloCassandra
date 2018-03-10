package golan.izik.insert;

import java.util.Calendar;

/**
 * Insert aggregated data for the year 1957. 6 weeks.
 * Two devices a day.
 * Three transactions per device.
 */
public class InsertHourlyAggregated1957 extends AbsInsertHourlyAggregated {

    public static void main(String[] args) throws InterruptedException {
        new InsertToAggregatedTable(new InsertHourlyAggregated1957()).insert();
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return true;
    }

    @Override
    public int getYear() {
        return 1957;
    }

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.FEBRUARY, 12);
        return cal;
    }

    @Override
    public void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, 7);
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return 2;
    }

    @Override
    public int getDailyRowsCountPerDevice(Calendar cal) {
        return 3;
    }


}
