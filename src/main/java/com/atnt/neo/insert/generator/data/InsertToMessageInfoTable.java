package com.atnt.neo.insert.generator.data;

import com.atnt.neo.insert.strategy.raw.data.AbsStrategyInsertCounters;
import com.datastax.driver.core.querybuilder.Insert;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class InsertToMessageInfoTable extends InsertToCountersTable {
    public InsertToMessageInfoTable(AbsStrategyInsertCounters strategyInsert) {
        super(strategyInsert);
    }

    protected void appendInsertTimeFields(Insert insert, int year, int month, int day, int hour, Calendar cal, Integer minute, Integer second) {
        insert.value("time_bucket", getMinuteTimeBucket(cal, month, day, hour, minute));
        insert.value("timestamp", getTimestamp(cal, month, day, hour, minute, second));
    }

    private Date getMinuteTimeBucket(Calendar cal, int month, int day, int hour, int minute) {
        //noinspection MagicConstant
        cal.set(getStrategy().getYear(), month-1, day, hour, minute, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        return cal.getTime();
    }

}
