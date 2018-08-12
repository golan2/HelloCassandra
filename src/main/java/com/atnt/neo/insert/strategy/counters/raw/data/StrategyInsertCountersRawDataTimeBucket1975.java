package com.atnt.neo.insert.strategy.counters.raw.data;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertCountersWithTimeBucketToTable;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralDaysEndOfYear;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

/**
 * Insert data to {@link CassandraShared#T_COUNTERS_RAW_DATA} for several days in 1975
 * A lot of data for several days
 * Every 2 minutes
 * 24 hours a day
 */
public class StrategyInsertCountersRawDataTimeBucket1975 extends AbsStrategyInsertCountersRawData {
    private StrategyInsertCountersRawDataTimeBucket1975(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertCountersWithTimeBucketToTable(new StrategyInsertCountersRawDataTimeBucket1975(args)).insert();
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
    public int getDefaultYear() {
        return 1975;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_RAW_DATA_TIME_BUCKET;
    }
}
