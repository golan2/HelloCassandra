package com.atnt.neo.insert.strategy.raw.data;


import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.data.InsertCountersWithTimeBucketToTable;
import com.atnt.neo.insert.strategy.StrategyUtil;

import java.util.Calendar;
import java.util.Set;

/**
 * Insert data to {@link CassandraShared#RAW_DATA_TABLE} for several days in 1976
 * 100 Devices from 10 device types (total of 1000 devices)
 * Every 2 minutes
 * 24 hours a day
 */
public class StrategyInsertCountersTimeBucket1976 extends AbsStrategyInsertCounters {
    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    private StrategyInsertCountersTimeBucket1976(Boolean truncateTableBeforeStart, Integer deviceCountPerDay) {
        this.truncateTableBeforeStart = truncateTableBeforeStart;
        this.deviceCountPerDay = deviceCountPerDay;
    }

    public static void main(String[] args) throws InterruptedException {
        Boolean truncate;
        Integer devicesPerDay;
        try {
            truncate = Boolean.parseBoolean(args[0]);
            devicesPerDay = Integer.parseInt(args[1]);
        } catch (Exception e) {
            truncate = true;
            devicesPerDay = 500;
            System.out.println("Missing command-line-argument. Setting devicesPerDay to ["+devicesPerDay+"]");
        }
        System.out.println("truncate=["+truncate+"] devicesPerDay=["+devicesPerDay+"] ");
        new InsertCountersWithTimeBucketToTable(new StrategyInsertCountersTimeBucket1976(truncate, devicesPerDay)).insert();
    }

    @Override
    public Calendar getFirstDay() {
        final Calendar result = (Calendar) getLastDay().clone();
        result.add(Calendar.DAY_OF_YEAR, -8);
        return result;
    }

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.DECEMBER, 31);
        return cal;
    }

    @Override
    public Set<Integer> getMinutesArray() {
        return StrategyUtil.generateEveryTwoMinutes();
    }

    @Override
    public Set<Integer> getSecondsArray() {
        return StrategyUtil.singleValue();
    }

    @Override
    public int getYear() {
        return 1976;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return this.deviceCountPerDay;
    }

    @Override
    public Set<Integer> getHoursArray() {
        return StrategyUtil.generate24hours();
    }

    @Override
    public String getTableName() {
        return CassandraShared.RAW_DATA_TIME_BUCKET;
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return this.truncateTableBeforeStart;
    }

}