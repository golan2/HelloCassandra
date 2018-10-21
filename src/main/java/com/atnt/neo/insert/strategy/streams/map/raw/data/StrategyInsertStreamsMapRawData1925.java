package com.atnt.neo.insert.strategy.streams.map.raw.data;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertToStreamsMapTable;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralMonthsBeginOfYear;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

@Deprecated
public class StrategyInsertStreamsMapRawData1925 extends AbsStrategyInsertStreamsMapRawData {

    private StrategyInsertStreamsMapRawData1925(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertToStreamsMapTable(new StrategyInsertStreamsMapRawData1925(args)).insert();
    }

    @Override
    public int getDefaultYear() {
        return 1925;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_STREAMS_MAP_RAW_DATA;
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySeveralMonthsBeginOfYear(getYear(), 2);
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
    }

}
