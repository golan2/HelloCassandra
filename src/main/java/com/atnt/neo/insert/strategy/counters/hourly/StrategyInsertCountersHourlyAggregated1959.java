package com.atnt.neo.insert.strategy.counters.hourly;

import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralMonthsBeginOfYear;
import com.atnt.neo.insert.strategy.time.SeveralHours;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

/**
 * Insert aggregated data for the year 1959. All days.
 * We want a specifically defined data over the year so we can use it for correctness test.
 * Each day we have "X" devices reporting.
 * Each device reports "Y" hours that day (first "Y" hours)
 * The values of X and Y are date dependant on day and month and we guarantee a different device count every day
 */
public class StrategyInsertCountersHourlyAggregated1959 extends AbsStrategyInsertCountersHourlyAggregated {

    private StrategyInsertCountersHourlyAggregated1959(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertToCountersTable(new StrategyInsertCountersHourlyAggregated1959(args)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySeveralMonthsBeginOfYear(getYear(), 12);
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new SeveralHours(2);
    }

    @Override
    public int getDefaultYear() {
        return 1959;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return cal.get(Calendar.DAY_OF_YEAR);
    }
}
