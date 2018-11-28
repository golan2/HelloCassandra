package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.streams.vertical.AbsStrategyInsertVerticalStreams;
import com.datastax.driver.core.querybuilder.Insert;

import java.util.Calendar;

public class InsertStreamOverTimeByObject extends AbsInsertVerticalStreams {

    public InsertStreamOverTimeByObject(AbsStrategyInsertVerticalStreams strategyInsert) {
        super(strategyInsert);
    }

    @Override
    protected void appendInsertContextFields(Insert insert, int year, int month, int day, int hour, int minute, int deviceIndex) {
        insert.value(CassandraShared.F_ENV_UUID, getStrategy().getEnvUuid());
    }

    @Override
    protected void appendInsertTimeFields(Insert insert, int year, int month, int day, int hour, Calendar cal, Integer minute, Integer second) {
        insert.value(CassandraShared.F_BUCKET_TIMESTAMP, getTimestamp(cal, 1, 1, 0, 0, 0));  //year bucket
        insert.value(CassandraShared.F_TIMESTAMP, getTimestamp(cal, month, day, hour, minute, second));
    }

    @Override
    void appendInsertDeviceInfo(Insert insert, int deviceIndex, int year, int month, int day) {
        insert.value(CassandraShared.F_OBJECT_ID, getStrategy().getDeviceId(year, month, day, deviceIndex));
        insert.value(CassandraShared.F_CLASS_ID, getStrategy().getDeviceType(year, month, day, deviceIndex));
    }

    @Override
    protected void appendAdditionalFields(String txnId, Insert insert, int year, int month, int day, int hour, int minute, int second, int deviceIndex) {
        insert.value(CassandraShared.F_TRANSACTION_ID, txnId);
    }

    @Override
    String getStreamNameField() {
        return CassandraShared.F_VERTICAL_STREAM_ID;
    }

    @Override
    String getTextStreamField() {
        return CassandraShared.F_VERTICAL_TEXT_STREAM;
    }

    @Override
    String getDoubleStreamField() {
        return CassandraShared.F_VERTICAL_DOUBLE_STREAM;
    }
}
