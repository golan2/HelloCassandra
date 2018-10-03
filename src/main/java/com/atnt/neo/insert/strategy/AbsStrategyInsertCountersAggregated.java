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


    public int getMessageCount(int month, int day, int hour) {
        return 1;
    }

    public int getBillingPointsSum(int month, int day, int hour) {
        return super.getBillingPoints(month, day, hour);
    }

    public long getDataSizeSum(int month, int day, int hour) {
        return super.getDataSize(month, day, hour);
    }

    public long getDataSizeMin(int month, int day, int hour) {
        return super.getDataSize(month, day, hour);
    }

    public long getDataSizeMax(int month, int day, int hour) {
        return super.getDataSize(month, day, hour);
    }

    public int getDataPointsSum(int month, int day, int hour) {
        return super.getDataPoints(month, day, hour);
    }

    public int getDataPointsMin(int month, int day, int hour) {
        return super.getDataPoints(month, day, hour);
    }

    public int getDataPointsMax(int month, int day, int hour) {
        return super.getDataPoints(month, day, hour);
    }

}
