package com.atnt.neo.insert.strategy;

import com.atnt.neo.insert.generator.AbsInsertToCassandra;
import com.atnt.neo.insert.strategy.time.EveryDayForPeriod;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralDaysEndOfYear;
import com.atnt.neo.insert.strategy.time.EveryDayWholeMonth;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbsStrategyInsert implements StrategyInsert {

    private final static Logger logger = LoggerFactory.getLogger(AbsInsertToCassandra.class);

    private static final String DEVICE_PREFIX = "device_"+ ThreadLocalRandom.current().nextInt(0, 99999);

    private final StrategyConfig config;

    protected AbsStrategyInsert(String[] args) {
        try {
            this.config = new StrategyConfig(args);
            logger.info("Configuration: {}", config);
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
        return String.format("%s_%02d", DEVICE_PREFIX, deviceIndex);
    }

    @Override
    public String getDeviceType(int year, int month, int day, int deviceIndex) {
        return String.format("device_type_%02d", deviceIndex % 3);
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
    public final UUID getEnvUuid() {
        return this.config.getEnvUuid();
    }

    @Override
    public StrategyConfig getConfig() {
        return this.config;
    }

    protected TimePeriod getTimePeriodFromConfig() {
        final Integer month = getConfig().getMonth();
        final Integer days = getConfig().getDays();
        if (month>-1) {
            if (days>-1) {
                final Calendar from = createTimestamp(getYear(), month, 1);
                final Calendar to = (Calendar)from.clone();
                to.add(Calendar.DAY_OF_YEAR, days);
                return new EveryDayForPeriod(from, to);
            }
            else {
                return new EveryDayWholeMonth(getYear(), month);
            }
        }
        else {
            return new EveryDaySeveralDaysEndOfYear(getYear(), getConfig().getDays());
        }
    }

    static Calendar createTimestamp(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        //noinspection MagicConstant
        cal.set(year, month-1, day, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        return cal;
    }
}
