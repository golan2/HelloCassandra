package com.atnt.neo.insert.strategy.streams.map.raw.data;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertToStreamsMapTable;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralMonthsBeginOfYear;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class StrategyInsertStreamsMapRawData1925 extends AbsStrategyInsertStreamsMapRawData {


    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    private StrategyInsertStreamsMapRawData1925(Boolean truncateTableBeforeStart, Integer deviceCountPerDay) {
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
        new InsertToStreamsMapTable(new StrategyInsertStreamsMapRawData1925(truncate, devicesPerDay)).insert();
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return this.truncateTableBeforeStart;
    }

    @Override
    public int getYear() {
        return 1925;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_STREAMS_MAP_RAW_DATA;
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySeveralMonthsBeginOfYear(getYear(), 2);
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return this.deviceCountPerDay;
    }

}
