package com.atnt.neo.insert.generator.aggregated;

import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.AbsInsertToCassandra;
import com.atnt.neo.insert.strategy.AbsInsertAggregated;

import java.util.Collections;

public class InsertToAggregatedTable extends AbsInsertToCassandra {

    public InsertToAggregatedTable(AbsInsertAggregated insertStrategy) {
        super(insertStrategy);
    }

    @Override
    protected AbsInsertAggregated getStrategy() {
        return (AbsInsertAggregated) super.getStrategy();
    }

    @Override
    protected Iterable<Insert> createInsertQueries(int deviceIndex, int year, int month, int day, int hour) {
        final Insert insert = QueryBuilder.insertInto(CassandraShared.KEYSPACE, getStrategy().getTableName());
        insert.value("org_bucket", "org_bucket");
        insert.value("project_bucket", "project_bucket");
        insert.value("org_id", "org_id");
        insert.value("project_id", "project_id");
        insert.value("environment", "environment");

        insert.value("year", year);
        insert.value("month", month);
        insert.value("day", day);
        if (getStrategy().isHourExist()) insert.value("hour", hour);

        insert.value("device_id", getStrategy().getDeviceId(year, month, day, deviceIndex));
        insert.value("device_type", getStrategy().getDeviceType(year, month, day, deviceIndex));
        insert.value("billing_points", getStrategy().getBillingPoints(month, day, hour));
        insert.value("counter", getStrategy().getCounter(month, day, hour));
        insert.value("data_points", getStrategy().getDataPoints(month, day, hour));
        insert.value("volume_size", getStrategy().getVolumeSize(month, day, hour));

        return Collections.singleton(insert);
    }

}
