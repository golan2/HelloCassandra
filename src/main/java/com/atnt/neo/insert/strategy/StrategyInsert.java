package com.atnt.neo.insert.strategy;

import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

public interface StrategyInsert {
    boolean shouldTruncateTableBeforeStart();

    int getYear();

    String getTableName();

    TimePeriod getTimePeriod();

    TxnPerDay getTxnPerDay();

    int getDeviceCountPerDay(Calendar cal);

    @SuppressWarnings("unused")
    String getDeviceId(int year, int month, int day, int deviceIndex);

    @SuppressWarnings("unused")
    String getDeviceType(int year, int month, int day, int deviceIndex);

    boolean includeTimeStamp();

    boolean includeTxnId();

    String getOrgBucket();

    String getProjectBucket();

    @SuppressWarnings("unused")
    String getOrgId(int year, int month, int day, int hour, int minute, int deviceIndex);

    String getProjectId();

    String getEnvironment();
}
