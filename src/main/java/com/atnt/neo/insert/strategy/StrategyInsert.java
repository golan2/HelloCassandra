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

    String getDeviceType(int year, int month, int day, int deviceIndex);

    int getBillingPoints(int month, int day, int hour);

    int getDataPoints(int month, int day, int hour);

    long getVolumeSize(int month, int day, int hour);

    boolean includeTimeStamp();

    boolean includeTxnId();

}
