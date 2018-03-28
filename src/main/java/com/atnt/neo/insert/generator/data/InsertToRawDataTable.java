package com.atnt.neo.insert.generator.data;

import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.AbsInsertToCassandra;
import com.atnt.neo.insert.strategy.raw.data.AbsInsertRawDataStrategy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

public class InsertToRawDataTable extends AbsInsertToCassandra {
    public InsertToRawDataTable(AbsInsertRawDataStrategy insertStrategy) {
        super(insertStrategy);
    }

    @Override
    protected Iterable<Insert> createInsertQueries(int deviceId, int year, int month, int day, int hour) {
        final Calendar          cal     = Calendar.getInstance();
        final Set<Integer>      minutes = getStrategy().getMinutesArray();
        final Set<Integer>      seconds = getStrategy().getSecondsArray();
        final ArrayList<Insert> result  = new ArrayList<>(minutes.size() * seconds.size());

        for (Integer minute : minutes) {
            for (Integer second : seconds) {
                final Insert insert = QueryBuilder.insertInto(CassandraShared.KEYSPACE, getStrategy().getTableName());
                insert.value("org_bucket", "org_bucket");
                insert.value("project_bucket", "project_bucket");
                insert.value("org_id", "org_id");
                insert.value("project_id", "project_id");
                insert.value("environment", "environment");

                insert.value("year", year);
                insert.value("month", month);
                insert.value("day", day);
                insert.value("hour", hour);
                insert.value("minutes", minute);
                insert.value("seconds", second);

                insert.value("device_id", deviceId);
                insert.value("device_type", "device_type");
                insert.value("device_firmware", "device_firmware");
                insert.value("transaction_id", "transaction_id");
                insert.value("timestamp", getTimestamp(cal, month, day, hour, minute, second));

                insert.value("data_points", getStrategy().getDataPoints(month, day, hour));
                insert.value("volume_size", getStrategy().getVolumeSize(month, day, hour));
                insert.value("billing_points", getStrategy().getBillingPoints(month, day, hour));
//                user_param map<text, text>,
                result.add(insert);
            }
        }
        return result;
    }

    private Date getTimestamp(Calendar cal, int month, int day, int hour, Integer minute, Integer second) {
        //noinspection MagicConstant
        cal.set(getStrategy().getYear(), month-1, day, hour, minute, second);
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        return cal.getTime();
    }

    protected AbsInsertRawDataStrategy getStrategy() {
        return (AbsInsertRawDataStrategy) super.getStrategy();
    }

}
