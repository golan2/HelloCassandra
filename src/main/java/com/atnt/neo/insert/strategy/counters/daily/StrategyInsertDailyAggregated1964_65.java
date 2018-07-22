package com.atnt.neo.insert.strategy.counters.daily;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.AbsStrategyInsertCountersAggregated;
import com.atnt.neo.insert.strategy.time.EveryDayDecJanFeb;
import com.atnt.neo.insert.strategy.time.SingleTxn;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;


//1964-1965
public class StrategyInsertDailyAggregated1964_65 extends AbsStrategyInsertCountersAggregated {

    private StrategyInsertDailyAggregated1964_65(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertToCountersTable(new StrategyInsertDailyAggregated1964_65(args)).insert();
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
    public String getTableName() {
        return CassandraShared.T_COUNTERS_DAILY;
    }

    @Override
    public int getYear() {
        return 1965;
    }

    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return String.format("device_%4d_%2d_%d", year, month, deviceIndex);
    }

}
