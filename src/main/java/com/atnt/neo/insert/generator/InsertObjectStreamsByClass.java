package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.streams.vertical.AbsStrategyInsertVerticalStreams;
import com.datastax.driver.core.querybuilder.Insert;

import java.util.Calendar;
import java.util.UUID;

public class InsertObjectStreamsByClass extends AbsInsertVerticalStreams {

    public InsertObjectStreamsByClass(AbsStrategyInsertVerticalStreams strategyInsert) {
        super(strategyInsert);
    }

    @Override
    protected void appendInsertTimeFields(Insert insert, int year, int month, int day, int hour, Calendar cal, Integer minute, Integer second) {
        insert.value("hour_bucket", getTimestamp(cal, month, day, hour, 0, 0));
        insert.value("timestamp", getTimestamp(cal, month, day, hour, minute, second));
    }

    @Override
    protected void appendInsertContextFields(Insert insert, int year, int month, int day, int hour, int minute, int deviceIndex) {
        insert.value("env_id", getStrategy().getEnvironment());

    }

    @Override
    void appendInsertDeviceInfo(Insert insert, int deviceIndex, int year, int month, int day) {
        insert.value("object_id", getStrategy().getDeviceId(year, month, day, deviceIndex));
        insert.value("class_name", getStrategy().getDeviceType(year, month, day, deviceIndex));
    }

    @Override
    protected void appendAdditionalFields(String txnId, Insert insert, int year, int month, int day, int hour, int minute, int second, int deviceIndex) {
        insert.value("txn_id", txnId);
    }


}
