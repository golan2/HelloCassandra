package com.atnt.neo.insert.strategy.daily;

import com.atnt.neo.insert.strategy.AbsStrategyInsertAggregated;
import com.atnt.neo.insert.strategy.StrategyUtil;

import java.util.Set;

public abstract class AbsStrategyInsertDailyAggregated extends AbsStrategyInsertAggregated {

    @Override
    public String getTableName() {
        return "daily_aggregator";
    }

    @Override
    public Set<Integer> getHoursArray() {
        return StrategyUtil.generateNotApplicable();
    }
}
