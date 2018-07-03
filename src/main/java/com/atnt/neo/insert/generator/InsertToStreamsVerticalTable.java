package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.streams.AbStrategyInsertStreams;
import com.atnt.neo.insert.strategy.streams.vertical.AbsStrategyInsertStreamsVertical;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InsertToStreamsVerticalTable extends AbsInsertToCassandra {


    public InsertToStreamsVerticalTable(AbsStrategyInsertStreamsVertical strategy) {
        super(strategy);
    }

    @Override
    protected Iterable<Insert> createInsertQueries(int deviceIndex, int year, int month, int day, int hour) {
        final Calendar          cal     = Calendar.getInstance();
        final Set<Integer>      minutes = getStrategy().getTxnPerDay().getMinutesArray();
        final Set<Integer>      seconds = getStrategy().getTxnPerDay().getSecondsArray();

        final Map<String, Double> doubleStreamMap = getStrategy().createDoubleStreamMap(deviceIndex, year, month, day, hour);
        final ArrayList<Insert> result1 = createInsertStreamsQuery(deviceIndex, year, month, day, hour, cal, minutes, seconds, doubleStreamMap);

        final Map<String, String> geoStreamMap = getStrategy().createGeoLocationStreamMap(deviceIndex, year, month, day, hour);
        final ArrayList<Insert> result2 = createInsertStreamsQuery(deviceIndex, year, month, day, hour, cal, minutes, seconds, geoStreamMap);

        return Stream.concat(result1.stream(), result2.stream()).collect(Collectors.toList());

    }

    private <T> ArrayList<Insert> createInsertStreamsQuery(int deviceIndex, int year, int month, int day, int hour, Calendar cal, Set<Integer> minutes, Set<Integer> seconds, Map<String, T> doubleStreamMap) {
        final ArrayList<Insert> result  = new ArrayList<>(minutes.size() * seconds.size());
        for (Map.Entry<String, T> entry : doubleStreamMap.entrySet()) {
            for (Integer minute : minutes) {
                for (Integer second : seconds) {

                    final Insert insert = QueryBuilder.insertInto(CassandraShared.KEYSPACE, getStrategy().getTableName());

                    appendInsertContextFields(insert, year, month, day, hour, minute, deviceIndex);

                    appendInsertTimeFields(insert, year, month, day, hour, cal, minute, second);

                    appendInsertDeviceInfo(insert, deviceIndex, year, month, day);

                    appendInsertStreamFields(insert, entry.getKey(), entry.getValue());

                    result.add(insert);

                }
            }
        }
        return result;
    }

    private <T> void appendInsertStreamFields(Insert insert, String streamName, T streamValue) {
        insert.value(CassandraShared.F_VERTICAL_STREAM_NAME, streamName);
        if (streamValue instanceof Double) {
            insert.value(CassandraShared.F_VERTICAL_STREAM_DOUBLE, streamValue);
        }
        else if (streamValue instanceof String) {
            insert.value(CassandraShared.F_VERTICAL_STREAM_TEXT, streamValue);
        }
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
    protected void appendAdditionalFields(Insert insert, int year, int month, int day, int hour, int minute, int second, int deviceIndex) {}

    protected AbStrategyInsertStreams getStrategy() {
        //noinspection unchecked
        return (AbStrategyInsertStreams) super.getStrategy();
    }

}
