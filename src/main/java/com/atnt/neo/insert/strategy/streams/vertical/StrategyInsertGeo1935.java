package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertVerticalStreamsOverTime;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralDaysEndOfYear;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

public class StrategyInsertGeo1935 extends AbsStrategyInsertVerticalStreams {

    private StrategyInsertGeo1935(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertVerticalStreamsOverTime(new StrategyInsertGeo1935(args)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySeveralDaysEndOfYear(getYear(), 5);
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
    }

    @Override
    public int getDefaultYear() {
        return 1935;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_STREAMS_BY_TIME;
    }

}
