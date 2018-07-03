package com.atnt.neo.insert.strategy;

import java.util.concurrent.ThreadLocalRandom;

public abstract class AbsStrategyInsert implements StrategyInsert {

    private static final String DEVICE_PREFIX = "device_"+ ThreadLocalRandom.current().nextInt(0, 99999) + "_";

    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return DEVICE_PREFIX + deviceIndex;
    }

    @Override
    public String getDeviceType(int year, int month, int day, int deviceIndex) {
        return String.format("device_type_%2d", day % 10);
    }

    @Override
    public String getOrgBucket() {
        return "org_bucket";
    }

    @Override
    public String getProjectBucket() {
        return "project_bucket";
    }

    @Override
    public String getOrgId(int year, int month, int day, int hour, int minute, int deviceIndex) {
        return "org_id";
    }

    @Override
    public String getProjectId() {
        return "project_id";
    }

    @Override
    public String getEnvironment() {
        return "environment";
    }
}
