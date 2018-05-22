package com.atnt.neo.insert.strategy.daily;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.strategy.AbsStrategyInsertAggregated;

public abstract class AbsStrategyInsertDailyAggregated extends AbsStrategyInsertAggregated {

    @Override
    public String getTableName() {
        return CassandraShared.DAILY_AGGREGATOR;
    }

}
