package com.atnt.neo.insert.strategy.counters.raw.data;

import com.atnt.neo.insert.strategy.counters.AbsStrategyInsertCounters;

public abstract class AbsStrategyInsertRawData extends AbsStrategyInsertCounters {

    AbsStrategyInsertRawData(String[] args) {
        super(args);
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
