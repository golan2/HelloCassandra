package com.atnt.neo.insert.strategy.daily;

import com.atnt.neo.insert.strategy.AbsStrategyInsertAggregated;

import java.util.Collections;
import java.util.Set;

public abstract class AbsStrategyInsertDailyAggregated extends AbsStrategyInsertAggregated {

    @Override
    public String getTableName() {
        return "daily_aggregator";
    }

    @Override
    public boolean isHourExist() {
        return false;
    }

    @Override
    public Set<Integer> getHoursArray() {
        return Collections.singleton(1);
    }
}
