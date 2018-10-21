package com.atnt.neo.insert.strategy.counters.daily;

import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.time.EveryDaySingleMonth;
import com.atnt.neo.insert.strategy.time.SingleTxn;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

public class StrategyInsertCountersDailyAggregated1969 extends AbsStrategyInsertCountersDailyAggregated {

    private StrategyInsertCountersDailyAggregated1969(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertToCountersTable(new StrategyInsertCountersDailyAggregated1969(args)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySingleMonth(getYear(), Calendar.JANUARY);
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new SingleTxn();
    }

    @Override
    public int getDefaultYear() {
        return 1969;
    }

}
