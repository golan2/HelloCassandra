package com.atnt.neo.insert.strategy.counters.daily;

import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralWeeksSinceNow;
import com.atnt.neo.insert.strategy.time.SingleTxn;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

public class StrategyInsertCountersDailyAggregatedNowSixWeeks extends AbsStrategyInsertCountersDailyAggregated {

    private static final int YEAR = Calendar.getInstance().get(Calendar.YEAR);

    private StrategyInsertCountersDailyAggregatedNowSixWeeks(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertToCountersTable(new StrategyInsertCountersDailyAggregatedNowSixWeeks(args)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySeveralWeeksSinceNow(6);
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new SingleTxn();
    }

    @Override
    public int getDefaultYear() {
        return YEAR;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return (super.getDeviceCountPerDay() == -1 ? 500 + 5*cal.get(Calendar.DAY_OF_MONTH) : super.getDeviceCountPerDay());
    }
}