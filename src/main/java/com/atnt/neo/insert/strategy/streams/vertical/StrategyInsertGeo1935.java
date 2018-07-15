package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertVerticalStreamsByTime;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralDaysEndOfYear;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;
import java.util.Collections;
import java.util.Map;

/**
 */
public class StrategyInsertGeo1935 extends AbsStrategyInsertStreamsVertical {
    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    private StrategyInsertGeo1935(Boolean truncate, Integer devicesPerDay) {
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
        }
        System.out.println("truncate=["+truncate+"] devicesPerDay=["+devicesPerDay+"] ");
        new InsertVerticalStreamsByTime(new StrategyInsertGeo1935(truncate, devicesPerDay)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySeveralDaysEndOfYear(getYear(), 5);
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return this.truncateTableBeforeStart;
    }


    @Override
    public int getYear() {
        return 1935;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_STREAMS_BY_TIME;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return this.deviceCountPerDay;
    }

    @Override
    public Map<String, Double> createDoubleStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return Collections.emptyMap();
    }
}
