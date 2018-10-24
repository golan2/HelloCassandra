package com.atnt.neo.insert.strategy;

import com.atnt.neo.insert.generator.AbsInsertToCassandra;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralDaysEndOfYear;
import com.atnt.neo.insert.strategy.time.EveryDaySingleMonth;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbsStrategyInsert implements StrategyInsert {

    private final static Logger logger = LoggerFactory.getLogger(AbsInsertToCassandra.class);

    private static final String DEVICE_PREFIX = "device_"+ ThreadLocalRandom.current().nextInt(0, 99999) + "_";

    private final StrategyConfig config;

    protected AbsStrategyInsert(String[] args) {
        try {
            this.config = new StrategyConfig(args);
            logger.info("Configuration: ", config);
        } catch (ParseException e) {
            throw new RuntimeException("Error " + e.getMessage(), e);
        }
    }

    @Override
    public final int getYear() {
        final Integer year = getConfig().getYear();
        if (year != StrategyConfig.NOT_PROVIDED) {
            return year;
        }
        else {
            return getDefaultYear();
        }
    }

    protected abstract int getDefaultYear();

    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return DEVICE_PREFIX + deviceIndex;
    }

    @Override
    public String getDeviceType(int year, int month, int day, int deviceIndex) {
        return String.format("device_type_%02d", day % 10);
    }

    @Override
    public final boolean shouldTruncateTableBeforeStart() {
        return this.config.getTruncate();
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return this.config.getDeviceCount();
    }

    protected final int getDeviceCountPerDay() {
        return this.config.getDeviceCount();
    }

    @Override
    public final String getOrgBucket() {
        return this.config.getOrgId();
    }

    @Override
    public final String getProjectBucket() {
        return this.config.getProjectId();
    }

    @Override
    public String getOrgId(int year, int month, int day, int hour, int minute, int deviceIndex) {
        return this.config.getOrgId();
    }

    @Override
    public final String getProjectId() {
        return this.config.getProjectId();
    }

    @Override
    public final String getEnvironment() {
        return this.config.getEnvironment();
    }

    @Override
    public StrategyConfig getConfig() {
        return this.config;
    }

    protected TimePeriod getTimePeriodFromConfig() {
        final Integer month = getConfig().getMonth();
        if (month>-1) {
            return new EveryDaySingleMonth(getYear(), month);
        }
        else {
            return new EveryDaySeveralDaysEndOfYear(getYear(), 1);
        }
    }
}
