package com.atnt.neo.insert.strategy.daily;

import com.atnt.neo.insert.strategy.AbsInsertAggregated;

import java.util.Collections;
import java.util.Set;

public abstract class AbsInsertDailyAggregated extends AbsInsertAggregated {

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
