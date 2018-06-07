package com.atnt.neo.insert.strategy.counters.daily;

import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.EveryDaySingleMonth;
import com.atnt.neo.insert.strategy.time.SingleTxn;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class StrategyInsertCountersDailyAggregated1969 extends AbsStrategyInsertCountersDailyAggregated {

    private static final int SUFFIX = ThreadLocalRandom.current().nextInt(0, 99999);
    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    private StrategyInsertCountersDailyAggregated1969(Boolean truncate, Integer devicesPerDay) {
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
        new InsertToCountersTable(new StrategyInsertCountersDailyAggregated1969(truncate, devicesPerDay)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySingleMonth(getYear(), Calendar.JANUARY);
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new SingleTxn();
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
    public int getDeviceCountPerDay(Calendar cal) {
        return ( this.deviceCountPerDay==-1 ? 70_000+cal.get(Calendar.DAY_OF_YEAR) : this.deviceCountPerDay );
    }

    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return "device_"+deviceIndex+"_"+ SUFFIX;
//        return super.getDeviceId(month, day, deviceIndex) + "_" + SUFFIX;
    }

}
