package com.atnt.neo.insert.strategy.hourly;

import com.atnt.neo.insert.generator.aggregated.InsertToAggregatedTable;

import java.util.Calendar;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Insert aggregated data for the year 1957. 6 weeks.
 * Two devices a day.
 * Three transactions per device.
 */
public class StrategyInsertHourlyAggregated1957 extends AbsStrategyInsertHourlyAggregated {

    public static void main(String[] args) throws InterruptedException {
        new InsertToAggregatedTable(new StrategyInsertHourlyAggregated1957()).insert();
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
        return IntStream.range(0,3).boxed().collect(Collectors.toSet());       // //[0,1,2]
    }


}
