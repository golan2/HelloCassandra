package com.atnt.old.insert.generator;

import com.atnt.neo.insert.generator.AbsInsertToCassandra;
import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.old.insert.strategy.streams.map.AbsStrategyInsertStreamsMap;
import com.datastax.driver.core.querybuilder.Insert;

import java.util.Calendar;

public class InsertToStreamsMapTable extends AbsInsertToCassandra {

    public InsertToStreamsMapTable(AbsStrategyInsertStreamsMap strategyInsert) {
        super(strategyInsert);
    }

    @Override
    protected void appendInsertTimeFields(Insert insert, int year, int month, int day, int hour, Calendar cal, Integer minute, Integer second) {
        insert.value(CassandraShared.F_TIMESTAMP, getTimestamp(cal, month, day, hour, minute, second));
        insert.value(CassandraShared.F_YEAR, year);
        insert.value(CassandraShared.F_MONTH, month);
        insert.value(CassandraShared.F_DAY, day);
        insert.value(CassandraShared.F_HOUR, hour);
        insert.value(CassandraShared.F_MINUTES, minute);
        insert.value(CassandraShared.F_SECONDS, second);
    }

    @Override
    protected void appendAdditionalFields(String txnId, Insert insert, int year, int month, int day, int hour, int minute, int second, int deviceIndex) {
        insert.value(CassandraShared.F_PART_SELECTOR, getStrategy().getPartSelector(year, month, day, hour, minute, second));
        insert.value(CassandraShared.F_USER_PARAM, getStrategy().createDoubleStreamMap(deviceIndex, year, month, day, hour));
    }

    protected AbsStrategyInsertStreamsMap getStrategy() {
        return (AbsStrategyInsertStreamsMap) super.getStrategy();
    }

}
