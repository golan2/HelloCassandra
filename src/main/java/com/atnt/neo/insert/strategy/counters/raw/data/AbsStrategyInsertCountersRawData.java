package com.atnt.neo.insert.strategy.counters.raw.data;

public abstract class AbsStrategyInsertCountersRawData extends AbsStrategyInsertRawData {

    AbsStrategyInsertCountersRawData(String[] args) {
        super(args);
    }

    @Override
    public int getBillingPoints(int month, int day, int hour) {
        return super.getBillingPoints(0, 0, 0);
    }

    @Override
    public int getDataPoints(int month, int day, int hour) {
        return super.getDataPoints(0, 0, 0);
    }

    @Override
    public long getVolumeSize(int month, int day, int hour) {
        return super.getVolumeSize(0, 0, 0);
    }

}
