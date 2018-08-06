package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.generator.InsertVerticalStreamsLatestValue;

import java.util.Collections;
import java.util.Map;

public class StrategyInsertLatestStreams1937 extends AbsStrategyInsertStreamsVerticalLatest {
    private StrategyInsertLatestStreams1937(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertVerticalStreamsLatestValue(new StrategyInsertLatestStreams1937(args)).insert();
    }


    @Override
    public Map<String, Double> createDoubleStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return generateDoubleStreamMap(getConfig().getStreamCount(), deviceIndex, year, month, day);
    }

    @Override
    public Map<String, String> createGeoLocationStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return Collections.emptyMap();
    }

    @Override
    public int getYear() {
        return 1937;
    }
}
