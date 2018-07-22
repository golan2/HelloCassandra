package com.atnt.neo.insert.strategy;

import com.atnt.neo.insert.strategy.counters.AbsStrategyInsertCounters;

public abstract class AbsStrategyInsertCountersAggregated extends AbsStrategyInsertCounters {

    protected AbsStrategyInsertCountersAggregated(String[] args) {
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
