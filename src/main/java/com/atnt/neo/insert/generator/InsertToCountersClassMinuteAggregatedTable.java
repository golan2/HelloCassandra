package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.counters.clazz.AbsStrategyInsertClassMinuteAggregated;
import com.datastax.driver.core.querybuilder.Insert;

import java.util.Calendar;

public class InsertToCountersClassMinuteAggregatedTable extends InsertToCountersTable {
    public InsertToCountersClassMinuteAggregatedTable(AbsStrategyInsertClassMinuteAggregated strategyInsert) {
        super(strategyInsert);
    }

    protected void appendInsertTimeFields(Insert insert, int year, int month, int day, int hour, Calendar cal, Integer minute, Integer second) {
        insert.value("timestamp_bucket", getTimestamp(cal, month, 1, 0, 0, 0));
        insert.value("timestamp", getTimestamp(cal, month, day, hour, minute, 0));
    }

    void appendInsertDeviceInfo(Insert insert, int deviceIndex, int year, int month, int day) {
        insert.value("device_type", getStrategy().getDeviceType(year, month, day, deviceIndex));
    }

    @Override
    protected void appendAdditionalFields(String txnId, Insert insert, int year, int month, int day, int hour, int minute, int second, int deviceIndex) {
        insert.value("device_count", getStrategy().getBillingPoints(month, day, hour));
        insert.value("message_count", getStrategy().getBillingPoints(month, day, hour));
        insert.value("billing_points_sum", getStrategy().getBillingPoints(month, day, hour));
        insert.value("data_size_sum", getStrategy().getBillingPoints(month, day, hour));
        insert.value("data_size_min", getStrategy().getBillingPoints(month, day, hour));
        insert.value("data_size_max", getStrategy().getBillingPoints(month, day, hour));
        insert.value("data_points_sum", getStrategy().getBillingPoints(month, day, hour));
        insert.value("data_points_min", getStrategy().getBillingPoints(month, day, hour));
        insert.value("data_points_max", getStrategy().getBillingPoints(month, day, hour));
        if (getStrategy().includeTxnId()) insert.value("transaction_id", "transaction_id");
    }

    @Override
    protected AbsStrategyInsertClassMinuteAggregated getStrategy() {
        return (AbsStrategyInsertClassMinuteAggregated) super.getStrategy();
    }
}
