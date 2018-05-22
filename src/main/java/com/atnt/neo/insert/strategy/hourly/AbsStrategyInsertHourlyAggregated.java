package com.atnt.neo.insert.strategy.hourly;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.strategy.AbsStrategyInsertAggregated;

public abstract class AbsStrategyInsertHourlyAggregated extends AbsStrategyInsertAggregated {
    @Override
    public String getTableName() {
        return CassandraShared.HOURLY_AGGREGATOR;
    }
}
