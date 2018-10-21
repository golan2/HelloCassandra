package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.streams.map.AbsStrategyInsertStreamsMap;
import com.datastax.driver.core.querybuilder.Insert;

import java.util.Calendar;

@Deprecated
public class InsertToStreamsMapTable extends AbsInsertToCassandra {

    public InsertToStreamsMapTable(AbsStrategyInsertStreamsMap strategyInsert) {
        super(strategyInsert);
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
        insert.value("part_selector", getStrategy().getPartSelector(year, month, day, hour, minute, second));
        insert.value("user_param", getStrategy().createDoubleStreamMap(deviceIndex, year, month, day, hour));
    }

    AbsStrategyInsertStreamsMap getStrategy() {
        return (AbsStrategyInsertStreamsMap) super.getStrategy();
    }

}
