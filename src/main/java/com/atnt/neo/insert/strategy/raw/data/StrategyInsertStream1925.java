package com.atnt.neo.insert.strategy.raw.data;

import com.atnt.neo.insert.generator.streams.InsertToMiniDc;
import com.atnt.neo.insert.strategy.StrategyUtil;

import java.util.Calendar;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StrategyInsertStream1925 extends AbsStrategyInsertCounters {

    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    private StrategyInsertStream1925(Boolean truncateTableBeforeStart, Integer deviceCountPerDay) {
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
            truncate = false;
            devicesPerDay = 20;
            System.out.println("Missing command-line-argument. Setting devicesPerDay to ["+devicesPerDay+"]");
        }
        System.out.println("truncate=["+truncate+"] devicesPerDay=["+devicesPerDay+"] ");
        new InsertToMiniDc(new StrategyInsertStream1925(truncate, devicesPerDay)).insert();

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
        return 1925;
    }

    @Override
    public Calendar getLastDay() {
        final Calendar firstDay = getFirstDay();
        firstDay.add(Calendar.DAY_OF_YEAR, 20);
        return firstDay;
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
        return "mini_dc";
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return this.truncateTableBeforeStart;
    }
}
