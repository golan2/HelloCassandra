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

public class InsertToDataCollector extends AbsInsertToCassandra {
    public InsertToDataCollector(AbsStrategyInsertRawData strategyInsert) {
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

                appendInsertContextFields(insert);

                appendInsertTimeFields(insert, year, month, day, hour, cal, minute, second);

                appendInsertDeviceInfo(deviceIndex, year, month, day, insert);

                appendInsertUsageFields(month, day, hour, insert);

                insert.value("part_selector", getStrategy().getPartSelector(year, month, day, hour, minute, second));

                insert.value("user_param", createStreamMap(deviceIndex, year, month, day, hour,minute, second));

                result.add(insert);
            }
        }
        return result;
    }

    private static Map<String, String> createStreamMap(int deviceIndex, int year, int month, int day, int hour, int minute, int second) {
        final HashMap<String, String> result = new HashMap<>();
        result.put("days_level", String.valueOf(year*365*12+month*12+day+deviceIndex));
        result.put("seconds_level", String.valueOf(hour*60*60+minute*60+second));
        return result;
    }


    protected AbsStrategyInsertRawData getStrategy() {
        return (AbsStrategyInsertRawData) super.getStrategy();
    }

}
