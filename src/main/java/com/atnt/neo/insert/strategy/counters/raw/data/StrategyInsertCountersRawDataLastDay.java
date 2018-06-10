package com.atnt.neo.insert.strategy.counters.raw.data;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.LastDay;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

public class StrategyInsertCountersRawDataLastDay extends AbsStrategyInsertCountersRawData {

    private static final int THIS_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    private StrategyInsertCountersRawDataLastDay(Boolean truncateTableBeforeStart, Integer deviceCountPerDay) {
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
            devicesPerDay = 1;
            System.out.println("Missing command-line-argument. Setting devicesPerDay to ["+devicesPerDay+"]");
        }
        System.out.println("truncate=["+truncate+"] devicesPerDay=["+devicesPerDay+"] ");
        new InsertToCountersTable(new StrategyInsertCountersRawDataLastDay(truncate, devicesPerDay)).insert();
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public String getOrgBucket() {
        return "yairu";
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public String getProjectBucket() {
        return "quickstart";
    }

    @Override
    public String getOrgId(int year, int month, int day, int hour, int minute, int deviceIndex) {
        return "yairu";
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public String getProjectId() {
        return "quickstart";
    }

    @Override
    public String getEnvironment() {
        return "dev";
    }

    @Override
    public int getYear() {
        return THIS_YEAR;
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new LastDay();
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return this.deviceCountPerDay;
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return this.truncateTableBeforeStart;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_COUNTERS_RAW_DATA;
    }

}
