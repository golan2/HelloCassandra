package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertVerticalStreamsOverTime;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

public class StrategyInsertAnomalyStreams1991 extends AbsStrategyInsertVerticalStreams {

    private StrategyInsertAnomalyStreams1991(String[] args) {
        super(args);

    }

    public static void main(String[] args) throws InterruptedException {
        new InsertVerticalStreamsOverTime(new StrategyInsertAnomalyStreams1991(args)).insert();
    }

    @Override
    protected int getDefaultYear() {
        return 1991;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_STREAMS_OVER_TIME;
    }

    @Override
    public TimePeriod getTimePeriod() {
        return getTimePeriodFromConfig();
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
    }
}
