package com.atnt.neo.insert.strategy.hourly;

import com.atnt.neo.insert.strategy.AbsStrategyInsertAggregated;

public abstract class AbsStrategyInsertHourlyAggregated extends AbsStrategyInsertAggregated {

    @Override
    public String getTableName() {
        return "hourly_aggregator";
    }

    @Override
    public boolean isHourExist() {
        return true;
    }
}
