package golan.izik.insert;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class InsertAggregated1955 implements InsertStrategy {

    private static final int SUFFIX = ThreadLocalRandom.current().nextInt(0, 99999);

    public static void main(String[] args) throws InterruptedException {
        new InsertToAggregatedTable(new InsertAggregated1955()).insert();
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return false;
    }

    @Override
    public int getYear() {
        return 1955;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return 2000;
    }

    @Override
    public int getTransactionCountPerDevice(Calendar cal) {
        return 24;
    }

    @Override
    public void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, 1);
    }

    @Override
    public String getDeviceId(int month, int day, int deviceIndex) {
        return "device_"+getYear()+"_"+month+"_"+day+"_"+deviceIndex+"_"+ SUFFIX;
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
