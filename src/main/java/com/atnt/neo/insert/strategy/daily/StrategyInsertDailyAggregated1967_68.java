package com.atnt.neo.insert.strategy.daily;

import com.atnt.neo.insert.generator.aggregated.InsertToAggregatedTable;

import java.util.Calendar;

public class StrategyInsertDailyAggregated1967_68 extends AbsStrategyInsertDailyAggregated {
    private final Integer deviceCountPerDay;

    private StrategyInsertDailyAggregated1967_68(Integer devicesPerDay) {
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
            devicesPerDay = -1;
        }
        System.out.println("truncate=["+truncate+"] devicesPerDay=["+devicesPerDay+"] ");
        new InsertToAggregatedTable(new StrategyInsertDailyAggregated1967_68(devicesPerDay)).insert();
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return false;       //we insert the same values so they override
    }

    @Override
    public int getYear() {
        return 1968;
    }

    @Override
    public Calendar getFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear()-1, Calendar.DECEMBER, 15);
        return cal;
    }

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.MARCH, 15);
        return cal;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return ( this.deviceCountPerDay==-1 ? 50_000+cal.get(Calendar.MONTH)*100 : this.deviceCountPerDay );
    }

    @Override
    public int getBillingPoints(int month, int day, int hour) {
        return day;
    }

    @Override
    public int getCounter(int month, int day, int hour) {
        return month;
    }

    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return "device_"+deviceIndex;
    }
}
