package com.atnt.neo.insert.generator.usage;

import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.AbsInsertToCassandra;
import com.atnt.neo.insert.strategy.usage.InsertUsageDailyAggregated1964_65;

import java.util.Collections;

public class InsertToUsageDailyTable extends AbsInsertToCassandra {

    public InsertToUsageDailyTable(InsertUsageDailyAggregated1964_65 insertStrategy) {
        super(insertStrategy);
    }

    @Override
    protected Iterable<Insert> createInsertQueries(int deviceIndex, int year, int month, int day, int hour) {
        final Insert insert = QueryBuilder.insertInto(CassandraShared.KEYSPACE, getStrategy().getTableName());
        insert.value("org_bucket", "org_bucket");
        insert.value("project_bucket", "project_bucket");
        insert.value("org_id", "org_id");
        insert.value("project_id", "project_id");

        insert.value("year", year);
        insert.value("month", month);
        insert.value("day", day);

        insert.value("device_id", getStrategy().getDeviceId(year, month, day, deviceIndex));
        insert.value("device_type", getStrategy().getDeviceType(year, month, day, deviceIndex));
        insert.value("billing_points", getStrategy().getBillingPoints(month, day, hour));
        insert.value("counter", getStrategy().getCounter(month, day, hour));

        return Collections.singleton(insert);
    }
}
