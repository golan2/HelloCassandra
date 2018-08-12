package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.strategy.streams.AbStrategyInsertStreams;

public abstract class AbsStrategyInsertStreams extends AbStrategyInsertStreams {

    AbsStrategyInsertStreams(String[] args) {
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