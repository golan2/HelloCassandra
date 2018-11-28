package com.atnt.old.insert.strategy.streams.map.raw.data;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.old.insert.generator.InsertToStreamsMapTable;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.SingleDay;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StrategyInsertStreamsMapRawData1927 extends AbsStrategyInsertStreamsMapRawData {

    private StrategyInsertStreamsMapRawData1927(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertToStreamsMapTable(new StrategyInsertStreamsMapRawData1927(args)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new SingleDay(getYear());
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
    }

    @Override
    public int getDefaultYear() {
        return 1927;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_STREAMS_MAP_RAW_DATA;
    }

    @Override
    public boolean includeTimeStamp() {
        return false;
    }

    @Override
    public boolean includeTxnId() {
        return false;
    }

    @Override
    public Map<String, Double> createDoubleStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        final HashMap<String, Double> result = new HashMap<>();
        result.put("days_level", (double) (year*365*12+month*12+day+deviceIndex));
        result.put("hours_level", (double) hour);
        return result;
    }

    @Override
    public Map<String, String> createStringStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return Collections.emptyMap();
    }

}
