package com.atnt.neo.insert.strategy.raw.data;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.data.InsertCountersWithTimeBucketToTable;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralDaysEndOfYear;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

/**
 * Insert data to {@link CassandraShared#RAW_DATA_TABLE} for several days in 1975
 * 10,000 Devices
 * Every 2 minutes
 * 24 hours a day
 */
public class StrategyInsertCountersTimeBucket1975 extends AbsStrategyInsertCounters {

    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    private StrategyInsertCountersTimeBucket1975(Boolean truncateTableBeforeStart, Integer deviceCountPerDay) {
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
            truncate = true;
            devicesPerDay = 1;
            System.out.println("Missing command-line-argument. Setting devicesPerDay to ["+devicesPerDay+"]");
        }
        System.out.println("truncate=["+truncate+"] devicesPerDay=["+devicesPerDay+"] ");
        new InsertCountersWithTimeBucketToTable(new StrategyInsertCountersTimeBucket1975(truncate, devicesPerDay)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySeveralDaysEndOfYear(getYear(), 8);
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
    }

    @Override
    public int getYear() {
        return 1975;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return this.deviceCountPerDay;
    }

    @Override
    public String getTableName() {
        return CassandraShared.RAW_DATA_TIME_BUCKET;
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return this.truncateTableBeforeStart;
    }
}
