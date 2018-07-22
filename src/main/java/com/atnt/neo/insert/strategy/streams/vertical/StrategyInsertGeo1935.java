package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertVerticalStreamsByTime;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralDaysEndOfYear;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Collections;
import java.util.Map;

public class StrategyInsertGeo1935 extends AbsStrategyInsertStreamsVertical {

    private StrategyInsertGeo1935(String[] args) {
        super(args);
    }

    @Override
    public Map<String, Double> createDoubleStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, String> createGeoLocationStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return generateGeoStreamMap(deviceIndex, year, month, day, hour);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertVerticalStreamsByTime(new StrategyInsertGeo1935(args)).insert();
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
    public int getYear() {
        return 1935;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_STREAMS_BY_TIME;
    }

}
