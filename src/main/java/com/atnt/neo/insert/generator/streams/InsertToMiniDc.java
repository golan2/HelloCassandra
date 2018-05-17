package com.atnt.neo.insert.generator.streams;

import com.atnt.neo.insert.generator.AbsInsertToCassandra;
import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.strategy.raw.data.AbsStrategyInsertRawData;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InsertToMiniDc extends AbsInsertToCassandra  {
    public InsertToMiniDc(AbsStrategyInsertRawData strategyInsert) {
        super(strategyInsert);
    }

    @Override
    protected Iterable<Insert> createInsertQueries(int deviceIndex, int year, int month, int day, int hour) {
        final Calendar          cal     = Calendar.getInstance();
        final Set<Integer>      minutes = getStrategy().getMinutesArray();
        final Set<Integer>      seconds = getStrategy().getSecondsArray();
        final ArrayList<Insert> result  = new ArrayList<>(minutes.size() * seconds.size());

        for (Integer minute : minutes) {
            for (Integer second : seconds) {
                final Insert insert = QueryBuilder.insertInto(CassandraShared.KEYSPACE, getStrategy().getTableName());
                insert.value("year", year);
                insert.value("month", month);
                insert.value("day", day);
                insert.value("hour", hour);
                insert.value("minutes", minute);
                insert.value("seconds", second);
                insert.value("timestamp", getTimestamp(cal, month, day, hour, minute, second));
                insert.value("device_id", getStrategy().getDeviceId(year, month, day, deviceIndex));
                insert.value("user_param", createStreamMap(year, month, day, hour,minute, second));
                result.add(insert);
            }
        }
        return result;
    }

    private static Map<String, String> createStreamMap(int year, int month, int day, int hour, int minute, int second) {
        final HashMap<String, String> result = new HashMap<>();
        result.put("days_level", String.valueOf(year*365*12+month*12+day));
        result.put("seconds_level", String.valueOf(hour*60*60+minute*60+second));
        return result;
    }


    @Override
    protected AbsStrategyInsertRawData getStrategy() {
        return (AbsStrategyInsertRawData) super.getStrategy();
    }
}
