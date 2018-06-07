package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.streams.AbStrategyInsertStreams;
import com.atnt.neo.insert.strategy.streams.map.AbsStrategyInsertStreamsMap;
import com.datastax.driver.core.querybuilder.Insert;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
        insert.value("user_param", createStreamMap(deviceIndex, year, month, day, hour,minute, second));
    }

    private static Map<String, String> createStreamMap(int deviceIndex, int year, int month, int day, int hour, int minute, int second) {
        final HashMap<String, String> result = new HashMap<>();
        result.put("days_level", String.valueOf(year*365*12+month*12+day+deviceIndex));
        result.put("seconds_level", String.valueOf(hour*60*60+minute*60+second));
        return result;
    }


    protected AbsStrategyInsertStreamsMap getStrategy() {
        return (AbsStrategyInsertStreamsMap) super.getStrategy();
    }

}
