package com.atnt.neo.insert.strategy.hourly;

import com.atnt.neo.insert.strategy.AbsInsertAggregated;

public abstract class AbsInsertHourlyAggregated extends AbsInsertAggregated {

    @Override
    public String getTableName() {
        return "hourly_aggregator";
    }

    @Override
    public boolean isHourExist() {
        return true;
    }
}
