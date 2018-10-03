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
        if (getStrategy().includeTimeStamp()) insert.value("timestamp", getTimestamp(cal, month, day, hour, minute, second));
        insert.value("year", year);
        insert.value("month", month);
        insert.value("day", day);

        //some tables don't have hour/minutes/seconds so the strategy should return "-1" and we skip the field

        if (hour!= StrategyUtil.NO_VALUE) insert.value("hour", hour);
        if (minute!=StrategyUtil.NO_VALUE) insert.value("minutes", minute);
        if (second!=StrategyUtil.NO_VALUE) insert.value("seconds", second);
    }


    @Override
    protected void appendAdditionalFields(Insert insert, int year, int month, int day, int hour, int minute, int second, int deviceIndex) {
        insert.value("billing_points", getStrategy().getBillingPoints(month, day, hour));
        insert.value("data_points", getStrategy().getDataPoints(month, day, hour));
        insert.value("volume_size", getStrategy().getDataSize(month, day, hour));
        if (getStrategy().includeTxnId()) insert.value("transaction_id", "transaction_id");
    }

    @Override
    AbsStrategyInsertCounters getStrategy() {
        return (AbsStrategyInsertCounters) super.getStrategy();
    }
}
