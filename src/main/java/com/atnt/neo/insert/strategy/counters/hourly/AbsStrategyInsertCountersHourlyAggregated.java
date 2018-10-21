package com.atnt.neo.insert.strategy.counters.hourly;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.strategy.counters.AbsStrategyInsertCountersAggregated;

public abstract class AbsStrategyInsertCountersHourlyAggregated extends AbsStrategyInsertCountersAggregated {
    AbsStrategyInsertCountersHourlyAggregated(String[] args) {
        super(args);
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_COUNTERS_HOURLY;
    }
}
