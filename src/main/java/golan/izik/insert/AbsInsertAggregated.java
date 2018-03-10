package golan.izik.insert;

import java.util.Calendar;

public abstract class AbsInsertAggregated implements InsertStrategy {
    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return true;
    }

    @Override
    public Calendar getFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.JANUARY, 1, 1, 0);
        return cal;
    }

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.DECEMBER, 31);
        return cal;
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
    public abstract boolean isHourExist();
}
