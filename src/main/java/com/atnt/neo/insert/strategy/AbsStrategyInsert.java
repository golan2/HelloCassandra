package com.atnt.neo.insert.strategy;

public abstract class AbsStrategyInsert implements StrategyInsert {
    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return String.format("device_%4d_%2d_%2d_%d", year, month, day, deviceIndex);
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
