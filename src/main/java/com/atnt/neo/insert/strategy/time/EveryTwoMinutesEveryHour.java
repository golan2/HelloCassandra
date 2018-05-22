package com.atnt.neo.insert.strategy.time;

import com.atnt.neo.insert.strategy.StrategyUtil;

import java.util.Set;

public class EveryTwoMinutesEveryHour implements TxnPerDay {

    @Override
    public Set<Integer> getHoursArray() {
        return StrategyUtil.generate24hours();
    }

    @Override
    public Set<Integer> getMinutesArray() {
        return StrategyUtil.generateEveryTwoMinutes();
    }

    @Override
    public Set<Integer> getSecondsArray() {
        return StrategyUtil.singleValue();
    }
}
