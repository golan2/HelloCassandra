package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.strategy.streams.AbsStrategyInsertStreams;

public abstract class AbsStrategyInsertVerticalStreams extends AbsStrategyInsertStreams {

    AbsStrategyInsertVerticalStreams(String[] args) {
        super(args);
    }

    @Override
    public boolean includeTimeStamp() {
        return false;
    }

    @Override
    public boolean includeTxnId() {
        return false;
    }

}