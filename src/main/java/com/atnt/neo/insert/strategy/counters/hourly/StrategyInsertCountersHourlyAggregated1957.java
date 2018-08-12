package com.atnt.neo.insert.strategy.counters.hourly;

import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.time.EveryWeekSeveralMonthsBeginOfYear;
import com.atnt.neo.insert.strategy.time.SeveralHours;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

/**
 * Insert aggregated data for the year 1957. 6 weeks.
 * Two devices a day.
 * Three transactions per device.
 */
public class StrategyInsertCountersHourlyAggregated1957 extends AbsStrategyInsertCountersHourlyAggregated {

    private StrategyInsertCountersHourlyAggregated1957(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertToCountersTable(new StrategyInsertCountersHourlyAggregated1957(args)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryWeekSeveralMonthsBeginOfYear(getYear(), 2);
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new SeveralHours(3);
    }

    @Override
    public int getDefaultYear() {
        return 1957;
    }

}
