package com.atnt.neo.insert.strategy.raw.data;

public abstract class AbsStrategyInsertCounters extends AbsStrategyInsertRawData {

    @Override
    public int getBillingPoints(int month, int day, int hour) {
        return 10;
    }

    @Override
    public int getCounter(int month, int day, int hour) {
        return 20;
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
