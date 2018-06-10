package com.atnt.neo.insert.strategy.counters.hourly;

import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.time.EveryAggregatedHour;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralMonthsBeginOfYear;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

/**
 * Insert data for the first 6 weeks in 1955
 */
public class StrategyInsertCountersHourlyAggregated1955 extends AbsStrategyInsertCountersHourlyAggregated {

    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    private StrategyInsertCountersHourlyAggregated1955(Boolean truncate, Integer devicesPerDay) {
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
            devicesPerDay = 20_000;
        }
        System.out.println("truncate=["+truncate+"] devicesPerDay=["+devicesPerDay+"] ");
        new InsertToCountersTable(new StrategyInsertCountersHourlyAggregated1955(truncate, devicesPerDay)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySeveralMonthsBeginOfYear(getYear(), 2);
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryAggregatedHour();
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return this.truncateTableBeforeStart;
    }

    @Override
    public int getYear() {
        return 1955;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return this.deviceCountPerDay;
    }

}
