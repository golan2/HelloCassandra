package com.atnt.neo.insert.strategy;

public abstract class AbsStrategyInsert implements StrategyInsert {
    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return String.format("device_%4d_%2d_%2d_%d", year, month, day, deviceIndex);
    }

    @Override
    public String getDeviceType(int year, int month, int day, int deviceIndex) {
        return String.format("device_type_%2d", day % 10);
    }


}
