package com.atnt.neo.insert.strategy.raw.data;

import com.atnt.neo.insert.generator.streams.InsertToDataCollector;
import com.atnt.neo.insert.strategy.StrategyUtil;

import java.util.Calendar;
import java.util.Set;

public class StrategyInsertStream1927 extends AbsStrategyInsertRawData {

    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    StrategyInsertStream1927(Boolean truncateTableBeforeStart, Integer deviceCountPerDay) {
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
        new InsertToDataCollector(new StrategyInsertStream1927
                (truncate, devicesPerDay)).insert();

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
        return 1927;
    }

    @Override
    public Calendar getLastDay() {
        final Calendar firstDay = getFirstDay();
        firstDay.add(Calendar.DAY_OF_YEAR, 1);
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
        return "data_collector";
    }

    @Override
    public int getBillingPoints(int month, int day, int hour) {
        return 0;
    }

    @Override
    public int getCounter(int month, int day, int hour) {
        return 0;
    }

    @Override
    public int getDataPoints(int month, int day, int hour) {
        return 0;
    }

    @Override
    public long getVolumeSize(int month, int day, int hour) {
        return 0;
    }

    @Override
    public int getPartSelector(int year, int month, int day, int hour, Integer minute, Integer second) {
        return minute;      //partition per minute
    }
}
