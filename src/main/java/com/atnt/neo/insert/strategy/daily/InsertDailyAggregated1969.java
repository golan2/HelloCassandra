package com.atnt.neo.insert.strategy.daily;

import com.atnt.neo.insert.generator.aggregated.InsertToAggregatedTable;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class InsertDailyAggregated1969 extends AbsInsertDailyAggregated {

    private static final int SUFFIX = ThreadLocalRandom.current().nextInt(0, 99999);
    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    private InsertDailyAggregated1969(Boolean truncate, Integer devicesPerDay) {
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
            devicesPerDay = -1;
        }
        System.out.println("truncate=["+truncate+"] devicesPerDay=["+devicesPerDay+"] ");
        new InsertToAggregatedTable(new InsertDailyAggregated1969(truncate, devicesPerDay)).insert();
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return truncateTableBeforeStart;
    }

    @Override
    public int getYear() {
        return 1969;
    }

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.FEBRUARY, 30);
        return cal;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return ( this.deviceCountPerDay==-1 ? 70_000+cal.get(Calendar.DAY_OF_YEAR) : this.deviceCountPerDay );
    }

    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return "device_"+deviceIndex+"_"+ SUFFIX;
//        return super.getDeviceId(month, day, deviceIndex) + "_" + SUFFIX;
    }

}
