package com.atnt.neo.insert.strategy.counters.raw.data;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.time.SeveralHoursEverySecond;
import com.atnt.neo.insert.strategy.time.SingleDay;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

public class StrategyInsertCountersRawData1989 extends AbsStrategyInsertCountersRawData {
    private StrategyInsertCountersRawData1989(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertToCountersTable(new StrategyInsertCountersRawData1989(args)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new SingleDay(getYear());
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new SeveralHoursEverySecond(3);
    }

    @Override
    public int getYear() {
        return 1989;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_COUNTERS_RAW_DATA;
    }
}
