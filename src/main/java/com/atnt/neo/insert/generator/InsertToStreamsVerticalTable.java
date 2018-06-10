package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.streams.AbStrategyInsertStreams;
import com.atnt.neo.insert.strategy.streams.vertical.AbsStrategyInsertStreamsVertical;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

public class InsertToStreamsVerticalTable extends AbsInsertToCassandra {


    public InsertToStreamsVerticalTable(AbsStrategyInsertStreamsVertical strategy) {
        super(strategy);
    }

    @Override
    protected Iterable<Insert> createInsertQueries(int deviceIndex, int year, int month, int day, int hour) {
        final Calendar          cal     = Calendar.getInstance();
        final Set<Integer> minutes = getStrategy().getTxnPerDay().getMinutesArray();
        final Set<Integer>      seconds = getStrategy().getTxnPerDay().getSecondsArray();
        final ArrayList<Insert> result  = new ArrayList<>(minutes.size() * seconds.size());

        final Map<String, Double> doubleStreamMap = getStrategy().createDoubleStreamMap(deviceIndex, year, month, day, hour);

        for (Map.Entry<String, Double> entry : doubleStreamMap.entrySet()) {
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

    private void appendInsertStreamFields(Insert insert, String streamName, Double streamValue) {
        insert.value(CassandraShared.F_VERTICAL_STREAM_NAME, streamName);
        insert.value(CassandraShared.F_VERTICAL_STREAM_NUMBER, streamValue);
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
