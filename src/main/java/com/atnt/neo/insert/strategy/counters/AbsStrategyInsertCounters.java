package com.atnt.neo.insert.strategy.counters;

import com.atnt.neo.insert.strategy.AbsStrategyInsert;

public abstract class AbsStrategyInsertCounters extends AbsStrategyInsert {
    public int getBillingPoints(int month, int day, int hour) {
        return 10;
    }

    public int getDataPoints(int month, int day, int hour) {
        return 30;
    }

    public long getVolumeSize(int month, int day, int hour) {
        return 40;
    }
}
