package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.StrategyUtil;
import com.atnt.neo.insert.strategy.counters.AbsStrategyInsertCounters;
import com.datastax.driver.core.querybuilder.Insert;

import java.util.Calendar;

public class InsertToCountersTable extends AbsInsertToCassandra {

    public InsertToCountersTable(AbsStrategyInsertCounters strategyInsert) {
        super(strategyInsert);
    }

    @Override
    protected void appendInsertTimeFields(Insert insert, int year, int month, int day, int hour, Calendar cal, Integer minute, Integer second) {
        if (getStrategy().includeTimeStamp()) insert.value(CassandraShared.F_TIMESTAMP, getTimestamp(cal, month, day, hour, minute, second));
        insert.value(CassandraShared.F_YEAR, year);
        insert.value(CassandraShared.F_MONTH, month);
        insert.value(CassandraShared.F_DAY, day);

        //some tables don't have hour/minutes/seconds so the strategy should return "-1" and we skip the field

        if (hour!= StrategyUtil.NO_VALUE) insert.value(CassandraShared.F_HOUR, hour);
        if (minute!=StrategyUtil.NO_VALUE) insert.value(CassandraShared.F_MINUTES, minute);
        if (second!=StrategyUtil.NO_VALUE) insert.value(CassandraShared.F_SECONDS, second);
    }


    @Override
    protected void appendAdditionalFields(String txnId, Insert insert, int year, int month, int day, int hour, int minute, int second, int deviceIndex) {
        insert.value(CassandraShared.F_BILLING_POINTS, getStrategy().getBillingPoints(month, day, hour));
        insert.value(CassandraShared.F_DATA_POINTS, getStrategy().getDataPoints(month, day, hour));
        insert.value(CassandraShared.F_VOLUME_SIZE, getStrategy().getDataSize(month, day, hour));
        if (getStrategy().includeTxnId()) insert.value(CassandraShared.F_TRANSACTION_ID, "transaction_id");
    }

    @Override
    protected AbsStrategyInsertCounters getStrategy() {
        return (AbsStrategyInsertCounters) super.getStrategy();
    }
}
