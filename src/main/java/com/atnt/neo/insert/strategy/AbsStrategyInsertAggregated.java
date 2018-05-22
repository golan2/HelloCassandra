package com.atnt.neo.insert.strategy;

public abstract class AbsStrategyInsertAggregated extends AbsStrategyInsert {

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

    @Override
    public boolean includeTimeStamp() {
        return false;
    }

    @Override
    public boolean includeTxnId() {
        return false;
    }
}
