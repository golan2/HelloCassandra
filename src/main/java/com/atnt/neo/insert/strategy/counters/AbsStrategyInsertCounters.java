package com.atnt.neo.insert.strategy.counters;

import com.atnt.neo.insert.strategy.AbsStrategyInsert;

public abstract class AbsStrategyInsertCounters extends AbsStrategyInsert {
    @Override
    public int getBillingPoints(int month, int day, int hour) {
        return 10;
    }

    @Override
    public int getDataPoints(int month, int day, int hour) {
        return 30;
    }

    @Override
    public long getVolumeSize(int month, int day, int hour) {
        return 40;
    }
}
