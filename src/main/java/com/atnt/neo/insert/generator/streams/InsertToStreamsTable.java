package com.atnt.neo.insert.generator.streams;

import com.atnt.neo.insert.generator.AbsInsertToCassandra;
import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.strategy.streams.AbsStrategyInsertVerticalStreams;
import com.atnt.neo.insert.strategy.streams.StrategyInsertVerticalStreams;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

public class InsertToStreamsTable<T> extends AbsInsertToCassandra {


    public InsertToStreamsTable(StrategyInsertVerticalStreams StrategyInsert) {
        super(StrategyInsert);
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

                insert.value("org_bucket", "org_bucket");
                insert.value("project_bucket", "project_bucket");
                insert.value("stream_name", getStrategy().getStreamName());

                insert.value("year", year);
                insert.value("month", month);
                insert.value("day", day);
                insert.value("hour", hour);

                insert.value("org_id", "org_id");
                insert.value("project_id", "project_id");
                insert.value("environment", "environment");

                insert.value("minutes", minute);
                insert.value("seconds", second);
                insert.value("timestamp", getTimestamp(cal, month, day, hour, minute, second));

                insert.value("device_id", getStrategy().getDeviceId(year, month, day, deviceIndex));
                insert.value("device_type", "device_type");
                insert.value("transaction_id", "transaction_id");

                insert.value(getStrategy().getStreamColumnName(), getStrategy().getStreamValue(year, month, day, hour, deviceIndex));
                result.add(insert);
            }
        }
        return result;
    }

    protected StrategyInsertVerticalStreams<T> getStrategy() {
        //noinspection unchecked
        return (StrategyInsertVerticalStreams<T>) super.getStrategy();
    }

}
