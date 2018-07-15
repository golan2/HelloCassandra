package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.streams.vertical.AbsStrategyInsertStreamsVertical;
import com.datastax.driver.core.querybuilder.Insert;

import java.util.Calendar;

public class InsertVerticalStreamsByTime extends AbsInsertVerticalStreams {


    public InsertVerticalStreamsByTime(AbsStrategyInsertStreamsVertical strategy) {
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

}
