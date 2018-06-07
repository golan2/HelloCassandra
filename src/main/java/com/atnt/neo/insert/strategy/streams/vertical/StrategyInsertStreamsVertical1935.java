package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertToStreamsVerticalTable;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.EveryDaySingleMonth;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

/**
 * Insert 3 months of data (Jan01 - Feb01)
 *  - devices count - program argument
 *  - Each device reports every 2 minutes
 *  - Single stream: "bogus_stream"
 *
 */
public class StrategyInsertStreamsVertical1935 extends AbsStrategyInsertStreamsVertical<Double> {
    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    private StrategyInsertStreamsVertical1935(Boolean truncate, Integer devicesPerDay) {
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
        new InsertToStreamsVerticalTable<Double>(new StrategyInsertStreamsVertical1935(truncate, devicesPerDay)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySingleMonth(getYear(), Calendar.JANUARY);
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
    public String getStreamName() {
        return "bogus_stream";
    }

    @Override
    public String getStreamColumnName() {
        return CassandraShared.NUMERIC_STREAM_FIELD_NAME;
    }

    @Override
    public Double getStreamValue(int year, int month, int day, int hour, int deviceIndex) {
        return (double) ((month * 31 + day) * 24 + hour + deviceIndex);
    }

    @Override
    public int getYear() {
        return 1935;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return this.deviceCountPerDay;
    }
}
