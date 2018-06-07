package com.atnt.neo.insert.strategy.streams;

import com.atnt.neo.insert.strategy.AbsStrategyInsert;

public abstract class AbStrategyInsertStreams extends AbsStrategyInsert {
    public abstract int getPartSelector(int year, int month, int day, int hour, Integer minute, Integer second);
}
