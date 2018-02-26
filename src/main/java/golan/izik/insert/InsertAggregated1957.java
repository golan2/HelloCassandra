package golan.izik.insert;

import java.util.Calendar;

/**
 * Insert aggregated data for the year 1957. 6 weeks.
 * One device in the first week, two devices in the second ...
 * A single transaction per device.
 */
public class InsertAggregated1957 implements InsertStrategy {

    public static void main(String[] args) throws InterruptedException {
        new InsertToAggregatedTable(new InsertAggregated1957()).insert();
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
    public int getDeviceCountPerDay(Calendar cal) {
        return 1;//cal.get(Calendar.DAY_OF_MONTH)/7 + 1;    // 1 device in the first week, 2 in the second, ...
    }

    @Override
    public int getTransactionCountPerDevice(Calendar cal) {
        return 1;
    }

    @Override
    public void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, 7);
    }

    @Override
    public String getDeviceId(int month, int day, int deviceIndex) {
        return "device_"+getYear()+"_"+month+"_"+day+"_"+deviceIndex;
    }

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.FEBRUARY, 12);
        return cal;
    }

    @Override
    public Calendar getFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.JANUARY, 1, 1, 0);
        return cal;
    }
}
