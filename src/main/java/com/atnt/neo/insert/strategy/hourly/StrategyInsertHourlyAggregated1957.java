package com.atnt.neo.insert.strategy.hourly;

import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.EveryWeekSeveralMonthsBeginOfYear;
import com.atnt.neo.insert.strategy.time.SeveralHours;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

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
    public TimePeriod getTimePeriod() {
        return new EveryWeekSeveralMonthsBeginOfYear(getYear(), 2);
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new SeveralHours(3);
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
    public int getDeviceCountPerDay(Calendar cal) {
        return 2;
    }

}
