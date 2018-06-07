package com.atnt.neo.insert.strategy.streams.map.raw.data;

import com.atnt.neo.insert.strategy.streams.map.AbsStrategyInsertStreamsMap;

public abstract class AbsStrategyInsertStreamsMapRawData extends AbsStrategyInsertStreamsMap {
    @Override
    public int getPartSelector(int year, int month, int day, int hour, Integer minute, Integer second) {
        return minute;      //partition per minute
    }
}
