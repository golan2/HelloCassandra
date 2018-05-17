package com.atnt.neo.insert.generator.data;

import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.AbsInsertToCassandra;
import com.atnt.neo.insert.strategy.raw.data.AbsStrategyInsertCounters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

public class InsertToCountersTable extends AbsInsertToCassandra {
    public InsertToCountersTable(AbsStrategyInsertCounters insertStrategy) {
        super(insertStrategy);
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

                result.add(insert);
            }
        }
        return result;
    }

    protected AbsStrategyInsertCounters getStrategy() {
        return (AbsStrategyInsertCounters) super.getStrategy();
    }

}
