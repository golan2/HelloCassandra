package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.generator.InsertVerticalStreamsLatestValue;

import java.util.Collections;
import java.util.Map;

public class StrategyInsertLatestGeo1936 extends AbsStrategyInsertLatestStreams {

    private StrategyInsertLatestGeo1936(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertVerticalStreamsLatestValue(new StrategyInsertLatestGeo1936(args)).insert();
    }

    @Override
    public int getYear() {
        return 1936;
    }

    @Override
    public Map<String, Double> createDoubleStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, String> createGeoLocationStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return generateGeoStreamMap(deviceIndex, year, month, day, hour);
    }
}
