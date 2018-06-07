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

    String getDeviceId(int year, int month, int day, int deviceIndex);

    @SuppressWarnings("unused")
    String getDeviceType(int year, int month, int day, int deviceIndex);

    @SuppressWarnings("unused")
    int getBillingPoints(int month, int day, int hour);

    @SuppressWarnings("unused")
    int getDataPoints(int month, int day, int hour);

    @SuppressWarnings("unused")
    long getVolumeSize(int month, int day, int hour);

    boolean includeTimeStamp();

    boolean includeTxnId();

    String getOrgBucket();

    String getProjectBucket();

    String getOrgId(int year, int month, int day, int hour, int minute, int deviceIndex);

    String getProjectId();

    String getEnvironment();
}
