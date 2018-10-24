package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertObjectStreamsByClass;
import com.atnt.neo.insert.strategy.time.EveryHour;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Collections;
import java.util.Map;

public class StrategyInsertObjectStreams1924 extends AbsStrategyInsertVerticalStreams {

    StrategyInsertObjectStreams1924(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertObjectStreamsByClass(new StrategyInsertObjectStreams1924(args)).insert();
    }

    @Override
    public Map<String, Double> createDoubleStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return generateDoubleStreamMap(getConfig().getStreamCount(), deviceIndex, year, month, day);
    }

    @Override
    public Map<String, Double> createRandomStreamMap() {
        return Collections.emptyMap();
    }

    @Override
    public int getDefaultYear() {
        return 1924;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_OBJECT_STREAMS_BY_CLASS;
    }

    @Override
    public TimePeriod getTimePeriod() {
        return getTimePeriodFromConfig();
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryHour();
    }

}
