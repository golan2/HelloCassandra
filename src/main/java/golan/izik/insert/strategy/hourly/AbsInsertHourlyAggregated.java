package golan.izik.insert.strategy.hourly;

import golan.izik.insert.strategy.AbsInsertAggregated;

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
