package com.atnt.neo.insert.strategy.raw.data;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.data.InsertToCountersTable;
import com.atnt.neo.insert.strategy.StrategyUtil;

import java.util.Calendar;
import java.util.Set;

public class StrategyInsertCounters1988 extends AbsStrategyInsertCounters {

    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;


    private StrategyInsertCounters1988(Boolean truncate, Integer devicesPerDay) {
        this.truncateTableBeforeStart = truncate;
        this.deviceCountPerDay = devicesPerDay;
    }

    public static void main(String[] args) throws InterruptedException {
        Boolean truncate;
        Integer devicesPerDay;
        try {
            truncate = Boolean.parseBoolean(args[0]);
            devicesPerDay = Integer.parseInt(args[1]);
        } catch (Exception e) {
            truncate = false;
            devicesPerDay = 1_000;
            System.out.println("Missing command-line-argument. Setting devicesPerDay to ["+devicesPerDay+"]");
        }
        System.out.println("truncate=["+truncate+"] devicesPerDay=["+devicesPerDay+"] ");
        new InsertToCountersTable(new StrategyInsertCounters1988(truncate, devicesPerDay)).insert();
    }

    @Override
    public Calendar getFirstDay() {
        final Calendar result = (Calendar) getLastDay().clone();
        result.add(Calendar.DAY_OF_YEAR, -15);
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
        return 1988;
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
        return CassandraShared.RAW_DATA_TABLE;
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return this.truncateTableBeforeStart;
    }
}
