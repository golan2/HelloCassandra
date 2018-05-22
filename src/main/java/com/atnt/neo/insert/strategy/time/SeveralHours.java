package com.atnt.neo.insert.strategy.time;

import com.atnt.neo.insert.strategy.StrategyUtil;
import java.util.Set;

public class SeveralHours implements TxnPerDay {
    private final int hours;

    public SeveralHours(int howManyHours) {
        this.hours = howManyHours;
    }

    @Override
    public Set<Integer> getHoursArray() {
        return StrategyUtil.generateXhours(this.hours);
    }

    @Override
    public Set<Integer> getMinutesArray() {
        return StrategyUtil.generateNotApplicable();
    }

    @Override
    public Set<Integer> getSecondsArray() {
        return StrategyUtil.generateNotApplicable();
    }
}
