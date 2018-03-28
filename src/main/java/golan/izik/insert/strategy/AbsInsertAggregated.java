package golan.izik.insert.strategy;

import java.util.Calendar;

public abstract class AbsInsertAggregated extends AbsInsertStrategy {

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.DECEMBER, 31);
        return cal;
    }

}
