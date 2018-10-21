package com.atnt.neo.insert.strategy.counters.clazz;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.strategy.counters.AbsStrategyInsertCountersAggregated;

public abstract class AbsStrategyInsertClassMinuteAggregated extends AbsStrategyInsertCountersAggregated {
    AbsStrategyInsertClassMinuteAggregated(String[] args) {
        super(args);
    }

    @Override
    public String getDeviceType(int year, int month, int day, int deviceIndex) {
        return String.format("device_type_%02d", deviceIndex);
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_COUNTERS_MINUTE;
    }


}
