package com.atnt.neo.insert.strategy.counters.raw.data;


import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertCountersWithTimeBucketToTable;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralDaysEndOfYear;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

/**
 * Insert data to {@link CassandraShared#T_COUNTERS_RAW_DATA} for several days in 1976
 * Diverse data for testing filters
 * Device, DeviceType,
 */
public class StrategyInsertCountersRawDataTimeBucketDiverse1976 extends AbsStrategyInsertCountersRawData {

    private StrategyInsertCountersRawDataTimeBucketDiverse1976(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertCountersWithTimeBucketToTable(new StrategyInsertCountersRawDataTimeBucketDiverse1976(args)).insert();
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
        return 1976;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_RAW_DATA_TIME_BUCKET;
    }

    @Override
    public String getOrgId(int year, int month, int day, int hour, int minute, int deviceIndex) {
        return String.format("org_id_%02d", minute%5);
    }
}
