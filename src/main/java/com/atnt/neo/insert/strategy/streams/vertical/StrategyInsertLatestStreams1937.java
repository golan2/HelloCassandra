package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.generator.InsertVerticalStreamsLatestValue;

import java.util.Map;

public class StrategyInsertLatestStreams1937 extends AbsStrategyInsertLatestStreams {
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
    public int getDefaultYear() {
        return 1937;
    }
}
