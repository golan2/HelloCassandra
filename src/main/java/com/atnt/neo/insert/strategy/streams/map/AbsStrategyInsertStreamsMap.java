package com.atnt.neo.insert.strategy.streams.map;

import com.atnt.neo.insert.strategy.streams.AbsStrategyInsertStreams;

@Deprecated
public abstract class AbsStrategyInsertStreamsMap extends AbsStrategyInsertStreams {
    protected AbsStrategyInsertStreamsMap(String[] args) {
        super(args);
    }

    @SuppressWarnings("unused")
    public abstract int getPartSelector(int year, int month, int day, int hour, Integer minute, Integer second);

}
