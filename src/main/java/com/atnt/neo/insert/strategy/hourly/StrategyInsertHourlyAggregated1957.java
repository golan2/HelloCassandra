package com.atnt.neo.insert.strategy.hourly;

import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.StrategyUtil;

import java.util.Calendar;
import java.util.Set;

/**
 * Insert aggregated data for the year 1957. 6 weeks.
 * Two devices a day.
 * Three transactions per device.
 */
public class StrategyInsertHourlyAggregated1957 extends AbsStrategyInsertHourlyAggregated {

    public static void main(String[] args) throws InterruptedException {
        new InsertToCountersTable(new StrategyInsertHourlyAggregated1957()).insert();
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
    public Set<Integer> getHoursArray() {
        return StrategyUtil.generateXminutes(3);
    }


}
