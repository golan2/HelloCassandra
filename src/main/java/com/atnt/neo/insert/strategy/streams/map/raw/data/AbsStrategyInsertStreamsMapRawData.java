package com.atnt.neo.insert.strategy.streams.map.raw.data;

import com.atnt.neo.insert.strategy.streams.map.AbsStrategyInsertStreamsMap;

@Deprecated
public abstract class AbsStrategyInsertStreamsMapRawData extends AbsStrategyInsertStreamsMap {
    AbsStrategyInsertStreamsMapRawData(String[] args) {
        super(args);
    }

    @Override
    public int getPartSelector(int year, int month, int day, int hour, Integer minute, Integer second) {
        return (int) Math.floor(minute/3.0);
    }

    @Override
    public boolean includeTimeStamp() {
        return true;
    }

    @Override
    public boolean includeTxnId() {
        return true;
    }

}
