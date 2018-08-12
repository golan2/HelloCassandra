package com.atnt.neo.insert.strategy.counters.hourly;

import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.time.EveryAggregatedHour;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralMonthsBeginOfYear;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

/**
 * Insert data for the first 6 weeks in 1955
 */
public class StrategyInsertCountersHourlyAggregated1955 extends AbsStrategyInsertCountersHourlyAggregated {

    private StrategyInsertCountersHourlyAggregated1955(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertToCountersTable(new StrategyInsertCountersHourlyAggregated1955(args)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySeveralMonthsBeginOfYear(getYear(), 2);
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryAggregatedHour();
    }

    @Override
    public int getDefaultYear() {
        return 1955;
    }
}
