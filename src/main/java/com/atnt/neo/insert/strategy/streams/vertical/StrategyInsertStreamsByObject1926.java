package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertStreamOverTimeByObject;
import com.atnt.neo.insert.strategy.time.EveryXMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Map;

public class StrategyInsertStreamsByObject1926 extends AbsStrategyInsertVerticalStreams {


    private StrategyInsertStreamsByObject1926(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertStreamOverTimeByObject(new StrategyInsertStreamsByObject1926(args)).insert();
    }

    @Override
    public Map<String, Double> createDoubleStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return generateDoubleStreamMap(getConfig().getStreamCount(), deviceIndex, year, month, day);
    }


    @Override
    protected int getDefaultYear() {
        return 1926;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_STREAMS_OT_BY_OBJECT;
    }

    @Override
    public TimePeriod getTimePeriod() {
        return getTimePeriodFromConfig();
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryXMinutesEveryHour(20);
    }
}
