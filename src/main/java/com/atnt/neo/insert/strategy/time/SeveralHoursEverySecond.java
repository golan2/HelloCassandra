package com.atnt.neo.insert.strategy.time;

import com.atnt.neo.insert.strategy.StrategyUtil;

import java.util.Set;

public class SeveralHoursEverySecond implements TxnPerDay {
    private final int hours;

    public SeveralHoursEverySecond(int hours) {
        this.hours = hours;
    }

    @Override
    public Set<Integer> getHoursArray() {
        return StrategyUtil.generateXhours(this.hours);
    }

    @Override
    public Set<Integer> getMinutesArray() {
        return StrategyUtil.generateEveryMinute();
    }

    @Override
    public Set<Integer> getSecondsArray() {
        return StrategyUtil.generateEverySecond();
    }

}
