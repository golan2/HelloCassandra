package com.atnt.neo.insert.strategy.raw.data;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.data.InsertToCountersTable;
import com.atnt.neo.insert.strategy.StrategyUtil;

import java.util.Calendar;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StrategyInsertCounters1989 extends AbsStrategyInsertCounters {

    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;


    private StrategyInsertCounters1989(Boolean truncate, Integer devicesPerDay) {
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
            devicesPerDay = 1;
            System.out.println("Missing command-line-argument. Setting devicesPerDay to ["+devicesPerDay+"]");
        }
        System.out.println("truncate=["+truncate+"] devicesPerDay=["+devicesPerDay+"] ");
        new InsertToCountersTable(new StrategyInsertCounters1989(truncate, devicesPerDay)).insert();
    }

    @Override
    public Calendar getLastDay() {
        return getFirstDay();
    }

    @Override
    public Set<Integer> getMinutesArray() {
        return StrategyUtil.generateEveryTwoMinutes();
    }

    @Override
    public Set<Integer> getSecondsArray() {
        return StrategyUtil.generateEveryTwoSeconds();
    }

    @Override
    public int getYear() {
        return 1989;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return this.deviceCountPerDay;
    }

    @Override
    public Set<Integer> getHoursArray() {
        return StrategyUtil.generateXhours(3);
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
