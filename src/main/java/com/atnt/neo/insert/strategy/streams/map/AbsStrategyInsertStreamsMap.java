package com.atnt.neo.insert.strategy.streams.map;

import com.atnt.neo.insert.strategy.streams.AbStrategyInsertStreams;

public abstract class AbsStrategyInsertStreamsMap extends AbStrategyInsertStreams {
    @SuppressWarnings("unused")
    public abstract int getPartSelector(int year, int month, int day, int hour, Integer minute, Integer second);

}