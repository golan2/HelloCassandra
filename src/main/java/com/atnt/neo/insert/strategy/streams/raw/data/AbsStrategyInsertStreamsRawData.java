package com.atnt.neo.insert.strategy.streams.raw.data;

import com.atnt.neo.insert.strategy.streams.AbStrategyInsertStreams;

public abstract class AbsStrategyInsertStreamsRawData extends AbStrategyInsertStreams {
    @Override
    public int getPartSelector(int year, int month, int day, int hour, Integer minute, Integer second) {
        return minute;      //partition per minute
    }
}
