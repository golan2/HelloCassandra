package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.streams.vertical.AbsStrategyInsertVerticalStreams;
import com.datastax.driver.core.querybuilder.Insert;

import java.util.Calendar;

public class InsertVerticalStreamsOverTime extends AbsInsertVerticalStreams {


    public InsertVerticalStreamsOverTime(AbsStrategyInsertVerticalStreams strategy) {
        super(strategy);
    }


    @Override
    protected void appendInsertTimeFields(Insert insert, int year, int month, int day, int hour, Calendar cal, Integer minute, Integer second) {
        insert.value("timestamp", getTimestamp(cal, month, day, hour, minute, second));
        insert.value("year", year);
        insert.value("month", month);
        insert.value("day", day);
        insert.value(CassandraShared.F_HOUR, hour);
        insert.value(CassandraShared.F_MINUTES, minute);
        insert.value(CassandraShared.F_SECONDS, second);
    }

}
