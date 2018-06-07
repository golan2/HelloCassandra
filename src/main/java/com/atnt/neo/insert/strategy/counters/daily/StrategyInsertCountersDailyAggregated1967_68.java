package com.atnt.neo.insert.strategy.counters.daily;

import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.EveryDayDecJanFeb;
import com.atnt.neo.insert.strategy.time.SingleTxn;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

public class StrategyInsertCountersDailyAggregated1967_68 extends AbsStrategyInsertCountersDailyAggregated {
    private final Integer deviceCountPerDay;

    private StrategyInsertCountersDailyAggregated1967_68(Integer devicesPerDay) {
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
        new InsertToCountersTable(new StrategyInsertCountersDailyAggregated1967_68(devicesPerDay)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDayDecJanFeb(getYear());
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new SingleTxn();
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
    public int getDeviceCountPerDay(Calendar cal) {
        return ( this.deviceCountPerDay==-1 ? 50_000+cal.get(Calendar.MONTH)*100 : this.deviceCountPerDay );
    }

    @Override
    public int getBillingPoints(int month, int day, int hour) {
        return day;
    }

    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return "device_"+deviceIndex;
    }
}
