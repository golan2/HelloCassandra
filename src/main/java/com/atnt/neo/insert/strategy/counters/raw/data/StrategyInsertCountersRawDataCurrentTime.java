package com.atnt.neo.insert.strategy.counters.raw.data;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralDaysBack;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.time.LocalDate;

public class StrategyInsertCountersRawDataCurrentTime extends AbsStrategyInsertCountersRawData {
    private StrategyInsertCountersRawDataCurrentTime(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertToCountersTable(new StrategyInsertCountersRawDataCurrentTime(args)).insert();
    }

    @Override
    protected int getDefaultYear() {
        LocalDate localDate = LocalDate.now();
        return localDate.getYear();
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_COUNTERS_RAW_DATA;
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySeveralDaysBack(getConfig());
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
    }
}
