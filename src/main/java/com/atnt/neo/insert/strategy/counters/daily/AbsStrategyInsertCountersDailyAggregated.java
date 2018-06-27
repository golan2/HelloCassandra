package com.atnt.neo.insert.strategy.counters.daily;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.strategy.AbsStrategyInsertCountersAggregated;

public abstract class AbsStrategyInsertCountersDailyAggregated extends AbsStrategyInsertCountersAggregated {

    @Override
    public String getTableName() {
        return CassandraShared.T_COUNTERS_DAILY;
    }

}