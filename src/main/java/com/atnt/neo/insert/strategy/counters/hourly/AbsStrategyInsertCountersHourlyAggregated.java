package com.atnt.neo.insert.strategy.counters.hourly;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.strategy.AbsStrategyInsertCountersAggregated;

public abstract class AbsStrategyInsertCountersHourlyAggregated extends AbsStrategyInsertCountersAggregated {
    @Override
    public String getTableName() {
        return CassandraShared.HOURLY_AGGREGATOR;
    }
}
