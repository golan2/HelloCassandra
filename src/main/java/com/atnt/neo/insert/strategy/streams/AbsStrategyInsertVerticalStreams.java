package com.atnt.neo.insert.strategy.streams;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.strategy.StrategyUtil;
import com.atnt.neo.insert.strategy.raw.data.AbsStrategyInsertRawData;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbsStrategyInsertVerticalStreams<T> extends AbsStrategyInsertRawData implements StrategyInsertVerticalStreams<T> {

    private static final int SUFFIX = ThreadLocalRandom.current().nextInt(0, 99999);

    @Override
    public Set<Integer> getHoursArray() {
        return StrategyUtil.generate24hours();
    }

    @Override
    public Set<Integer> getMinutesArray() {
        return StrategyUtil.generateEveryTwoMinutes();
    }

    @Override
    public Set<Integer> getSecondsArray() {
        return StrategyUtil.singleValue();
    }

    @Override
    public String getTableName() {
        return CassandraShared.STREAMS_TABLE;
    }

    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return "device_"+deviceIndex+"_"+ SUFFIX;
    }

    @Override
    public int getBillingPoints(int month, int day, int hour) {
        return -1;
    }

    @Override
    public int getDataPoints(int month, int day, int hour) {
        return -1;
    }

    @Override
    public long getVolumeSize(int month, int day, int hour) {
        return -1;
    }

}