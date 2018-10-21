package com.atnt.neo.insert.strategy.counters.daily;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.strategy.counters.AbsStrategyInsertCountersAggregated;

public abstract class AbsStrategyInsertCountersDailyAggregated extends AbsStrategyInsertCountersAggregated {

    AbsStrategyInsertCountersDailyAggregated(String[] args) {
        super(args);
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_COUNTERS_DAILY;
    }

}
