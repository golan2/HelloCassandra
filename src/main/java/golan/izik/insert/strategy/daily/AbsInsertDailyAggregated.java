package golan.izik.insert.strategy.daily;

import golan.izik.insert.strategy.AbsInsertAggregated;

public abstract class AbsInsertDailyAggregated extends AbsInsertAggregated {

    @Override
    public String getTableName() {
        return "daily_aggregator";
    }

    @Override
    public boolean isHourExist() {
        return false;
    }
}
