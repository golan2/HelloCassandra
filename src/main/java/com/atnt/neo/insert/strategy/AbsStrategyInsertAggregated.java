package com.atnt.neo.insert.strategy;

import java.util.Calendar;

public abstract class AbsStrategyInsertAggregated extends AbsStrategyInsert {

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.DECEMBER, 31);
        return cal;
    }

}
