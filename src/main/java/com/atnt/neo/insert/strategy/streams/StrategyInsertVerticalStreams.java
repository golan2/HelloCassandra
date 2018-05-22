package com.atnt.neo.insert.strategy.streams;

import com.atnt.neo.insert.strategy.StrategyInsert;

public interface StrategyInsertVerticalStreams<T> extends StrategyInsert {
    @SuppressWarnings("SameReturnValue")
    String getStreamName();

    @SuppressWarnings("SameReturnValue")
    String getStreamColumnName();

    @SuppressWarnings("unused")
    T getStreamValue(int year, int month, int day, int hour, int deviceIndex);
}
