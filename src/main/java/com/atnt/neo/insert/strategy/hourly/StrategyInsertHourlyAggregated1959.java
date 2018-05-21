package com.atnt.neo.insert.strategy.hourly;

import com.atnt.neo.insert.generator.InsertToCountersTable;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Insert aggregated data for the year 1959. All days.
 * We want a specifically defined data over the year so we can use it for correctness test.
 * Each day we have "X" devices reporting.
 * Each device reports "Y" hours that day (first "Y" hours)
 * The values of X and Y are date dependant on day and month and we guarantee a different device count every day
 */
public class StrategyInsertHourlyAggregated1959 extends AbsStrategyInsertHourlyAggregated {

    public static void main(String[] args) throws InterruptedException {
        new InsertToCountersTable(new StrategyInsertHourlyAggregated1959()).insert();
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
    public Set<Integer> getHoursArray() {
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        return set;
    }

}
