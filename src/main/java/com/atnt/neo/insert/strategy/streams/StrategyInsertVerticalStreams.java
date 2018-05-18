package com.atnt.neo.insert.strategy.streams;

import com.atnt.neo.insert.strategy.StrategyInsert;

public interface StrategyInsertVerticalStreams<T> extends StrategyInsert {
    String getStreamName();

    String getStreamColumnName();

    T getStreamValue(int year, int month, int day, int hour, int deviceIndex);
}
