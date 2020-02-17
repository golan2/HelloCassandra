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



    //--env_uuid c96e338a-5288-488c-b78e-9d4c29c7f31c --year 2018 --month 12 --days 40 --devices 6 --streams 5 --truncate true
    //--env_uuid c96e338a-5288-488c-b78e-9d4c29c7f31c --year 2019 --month 1 --days 10 --devices 30 --streams 30 --truncate false

    public static void main(String[] args) throws InterruptedException {
        new InsertStreamOverTimeByObject(new StrategyInsertStreamsByObject1926(args)).insert();
    }

    @Override
    public Map<String, Double> createDoubleStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return generateDoubleStreamMap(getConfig().getStreamCount(), deviceIndex, year, month, day);
    }

    @Override
    public Map<String, String> createStringStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return generateStringStreamMap(getConfig().getStreamCount(), deviceIndex, year, month, day, hour);
    }

    @Override
    public Map<String, GeoLocation> createGeoStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return generateGeoStreamMap(deviceIndex, year, month, day, hour);
    }

    @Override
    public Map<String, Boolean> createBooleanStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return generateBooleanStreamMap(deviceIndex, year, month, day, hour);
    }

    @Override
    public String getDeviceType(int year, int month, int day, int deviceIndex) {
        return "piper";
    }

    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return String.format("pipe_%02d", deviceIndex);
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
