package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.counters.raw.data.AbsStrategyInsertCountersRawData;
import com.datastax.driver.core.querybuilder.Insert;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class InsertCountersWithTimeBucketToTable extends AbsInsertToCassandra {
    public InsertCountersWithTimeBucketToTable(AbsStrategyInsertCountersRawData strategyInsert) {
        super(strategyInsert);
    }

    protected void appendInsertTimeFields(Insert insert, int year, int month, int day, int hour, Calendar cal, Integer minute, Integer second) {
        insert.value("time_bucket", getHourTimeBucket(cal, month, day, hour));
        insert.value("timestamp", getTimestamp(cal, month, day, hour, minute, second));
    }

    @Override
    protected void appendAdditionalFields(Insert insert, int year, int month, int day, int hour, int minute, int second, int deviceIndex) { }

    private Date getHourTimeBucket(Calendar cal, int month, int day, int hour) {
        //noinspection MagicConstant
        cal.set(getStrategy().getYear(), month-1, day, hour, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        return cal.getTime();
    }

}
