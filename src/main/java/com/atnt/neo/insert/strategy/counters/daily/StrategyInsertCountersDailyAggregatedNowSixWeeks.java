package com.atnt.neo.insert.strategy.counters.daily;

import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralDaysEndOfYear;
import com.atnt.neo.insert.strategy.time.SingleTxn;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

public class StrategyInsertCountersDailyAggregatedNowSixWeeks extends AbsStrategyInsertCountersDailyAggregated {

    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    private StrategyInsertCountersDailyAggregatedNowSixWeeks(Boolean truncateTableBeforeStart, Integer deviceCountPerDay) {
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
            devicesPerDay = -1;
        }
        System.out.println("truncate=[" + truncate + "] devicesPerDay=[" + devicesPerDay + "] ");
        new InsertToCountersTable(new StrategyInsertCountersDailyAggregatedNowSixWeeks(truncate, devicesPerDay)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySeveralDaysEndOfYear(getYear(), 45);
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
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return (this.deviceCountPerDay == -1 ? 70_000 + cal.get(Calendar.DAY_OF_YEAR) : this.deviceCountPerDay);
    }
}