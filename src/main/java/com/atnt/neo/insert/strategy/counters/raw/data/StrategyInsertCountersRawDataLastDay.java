package com.atnt.neo.insert.strategy.counters.raw.data;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.LastDay;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

public class StrategyInsertCountersRawDataLastDay extends AbsStrategyInsertCountersRawData {

    private static final int THIS_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    private StrategyInsertCountersRawDataLastDay(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertToCountersTable(new StrategyInsertCountersRawDataLastDay(args)).insert();
    }

    @Override
    public int getYear() {
        return THIS_YEAR;
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new LastDay();
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_COUNTERS_RAW_DATA;
    }

}
