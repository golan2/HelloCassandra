package com.atnt.neo.insert.strategy;

import org.apache.commons.cli.ParseException;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbsStrategyInsert implements StrategyInsert {

    private static final String DEVICE_PREFIX = "device_"+ ThreadLocalRandom.current().nextInt(0, 99999) + "_";

    private final StrategyConfig config;

    protected AbsStrategyInsert(String[] args) {

        try {
            this.config = new StrategyConfig(args);
            System.out.println(config);
        } catch (ParseException e) {
            throw new RuntimeException("Error " + e.getMessage(), e);
        }




    }

    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return DEVICE_PREFIX + deviceIndex;
    }

    @Override
    public String getDeviceType(int year, int month, int day, int deviceIndex) {
        return String.format("device_type_%2d", day % 10);
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
}
