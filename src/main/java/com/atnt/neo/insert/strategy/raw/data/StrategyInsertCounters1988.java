package com.atnt.neo.insert.strategy.raw.data;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.EveryDayTwoWeeksEndOfYear;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

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
            devicesPerDay = 1;
            System.out.println("Missing command-line-argument. Setting devicesPerDay to ["+devicesPerDay+"]");
        }
        System.out.println("truncate=["+truncate+"] devicesPerDay=["+devicesPerDay+"] ");
        new InsertToCountersTable(new StrategyInsertCounters1988(truncate, devicesPerDay)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDayTwoWeeksEndOfYear(getYear());
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
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
    public String getTableName() {
        return CassandraShared.RAW_DATA_TABLE;
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return this.truncateTableBeforeStart;
    }
}
