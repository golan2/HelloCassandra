package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.counters.AbsStrategyInsertCounters;
import com.atnt.neo.insert.strategy.counters.clazz.AbsStrategyInsertCountersByClass;
import com.datastax.driver.core.querybuilder.Insert;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class InsertToCountersByClassTable extends InsertToCountersTable {
    public InsertToCountersByClassTable(AbsStrategyInsertCounters strategyInsert) {
        super(strategyInsert);
    }

    @Override
    protected void appendInsertContextFields(Insert insert, int year, int month, int day, int hour, int minute, int deviceIndex) {
        insert.value("org_id", getStrategy().getOrgId(year, month, day, hour, minute, deviceIndex));
        insert.value("project_id", getStrategy().getProjectId());
        insert.value("environment", getStrategy().getEnvironment());
    }

    protected void appendInsertTimeFields(Insert insert, int year, int month, int day, int hour, Calendar cal, Integer minute, Integer second) {

        cal.set(year, month-1, day, hour, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        insert.value("minute", cal.getTime());
        cal.set(Calendar.MINUTE, 0);
        insert.value("hour", cal.getTime());

        cal.set(Calendar.HOUR_OF_DAY, 0);
        insert.value("day", cal.getTime());

        final Calendar cweek = (Calendar) cal.clone();
        cweek.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        insert.value("week", cweek.getTime());

        cal.set(Calendar.DAY_OF_MONTH, 1);
        insert.value("month", cal.getTime());

        cal.set(Calendar.MONTH, ((month-1)/3)*3);
        insert.value("timestamp_bucket", cal.getTime());

        cal.set(Calendar.MONTH, 0);
        insert.value("year", cal.getTime());




    }

    void appendInsertDeviceInfo(Insert insert, int deviceIndex, int year, int month, int day) {
        insert.value("class_name", getStrategy().getDeviceType(year, month, day, deviceIndex));
    }

    @Override
    protected void appendAdditionalFields(Insert insert, int year, int month, int day, int hour, int minute, int second, int deviceIndex) {
        insert.value("device_count", getStrategy().getBillingPoints(month, day, hour));
        insert.value("message_count", getStrategy().getBillingPoints(month, day, hour));
        insert.value("billing_points_sum", getStrategy().getBillingPoints(month, day, hour));
        insert.value("data_size_sum", getStrategy().getBillingPoints(month, day, hour));
        insert.value("data_size_min", getStrategy().getBillingPoints(month, day, hour));
        insert.value("data_size_max", getStrategy().getBillingPoints(month, day, hour));
        insert.value("data_points_sum", getStrategy().getBillingPoints(month, day, hour));
        insert.value("data_points_min", getStrategy().getBillingPoints(month, day, hour));
        insert.value("data_points_max", getStrategy().getBillingPoints(month, day, hour));
    }

    @Override
    AbsStrategyInsertCountersByClass getStrategy() {
        return (AbsStrategyInsertCountersByClass) super.getStrategy();
    }
}
