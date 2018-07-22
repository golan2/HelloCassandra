package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.strategy.time.SingleDay;
import com.atnt.neo.insert.strategy.time.SingleTxn;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

public abstract class AbsStrategyInsertStreamsVerticalLatest extends AbsStrategyInsertStreamsVertical {
    AbsStrategyInsertStreamsVerticalLatest(String[] args) {
        super(args);
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new SingleDay(getYear());
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new SingleTxn();
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_STREAMS_LATEST;
    }
}
