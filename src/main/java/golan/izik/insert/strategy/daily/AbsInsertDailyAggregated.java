package golan.izik.insert.strategy.daily;

import golan.izik.insert.strategy.AbsInsertAggregated;

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
