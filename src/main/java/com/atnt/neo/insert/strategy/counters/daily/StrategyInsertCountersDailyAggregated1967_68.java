package com.atnt.neo.insert.strategy.counters.daily;

import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.StrategyConfig;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.EveryDayDecJanFeb;
import com.atnt.neo.insert.strategy.time.SingleTxn;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

public class StrategyInsertCountersDailyAggregated1967_68 extends AbsStrategyInsertCountersDailyAggregated {


    private StrategyInsertCountersDailyAggregated1967_68(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertToCountersTable(new StrategyInsertCountersDailyAggregated1967_68(args)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDayDecJanFeb(getYear());
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new SingleTxn();
    }

    @Override
    public int getDefaultYear() {
        return 1968;
    }


    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return (super.getDeviceCountPerDay() == StrategyConfig.NOT_PROVIDED ? 50_000 + cal.get(Calendar.MONTH) * 100 : super.getDeviceCountPerDay());
    }

    @Override
    public int getBillingPoints(int month, int day, int hour) {
        return day;
    }

}