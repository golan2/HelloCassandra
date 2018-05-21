package com.atnt.neo.insert.generator.streams;

import com.atnt.neo.insert.generator.AbsInsertToCassandra;
import com.atnt.neo.insert.strategy.streams.StrategyInsertVerticalStreams;
import com.datastax.driver.core.querybuilder.Insert;

import java.util.Calendar;

public class InsertToVerticalStreamsTable<T> extends AbsInsertToCassandra {


    public InsertToVerticalStreamsTable(StrategyInsertVerticalStreams strategy) {
        super(strategy);
    }

    @Override
    protected void appendInsertTimeFields(Insert insert, int year, int month, int day, int hour, Calendar cal, Integer minute, Integer second) {
        insert.value("timestamp", getTimestamp(cal, month, day, hour, minute, second));
        insert.value("year", year);
        insert.value("month", month);
        insert.value("day", day);
        insert.value("hour", hour);
        insert.value("minutes", minute);
        insert.value("seconds", second);
    }

    @Override
    protected void appendAdditionalFields(Insert insert, int year, int month, int day, int hour, int minute, int second, int deviceIndex) {
        insert.value("stream_name", getStrategy().getStreamName());
        insert.value(getStrategy().getStreamColumnName(), getStrategy().getStreamValue(year, month, day, hour, deviceIndex));
    }

    protected StrategyInsertVerticalStreams<T> getStrategy() {
        //noinspection unchecked
        return (StrategyInsertVerticalStreams<T>) super.getStrategy();
    }

}
