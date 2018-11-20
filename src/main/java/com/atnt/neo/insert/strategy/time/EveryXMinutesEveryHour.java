package com.atnt.neo.insert.strategy.time;

import com.atnt.neo.insert.strategy.StrategyUtil;

import java.util.Set;

public class EveryXMinutesEveryHour implements TxnPerDay {

    private final int minutes;

    public EveryXMinutesEveryHour(int minutes) {
        this.minutes = minutes;
    }

    @Override
    public Set<Integer> getHoursArray() {
        return StrategyUtil.generate24hours();
    }

    @Override
    public Set<Integer> getMinutesArray() {
        return StrategyUtil.generateEveryXMinutes(minutes);
    }

    @Override
    public Set<Integer> getSecondsArray() {
        return StrategyUtil.singleValue();
    }
}
