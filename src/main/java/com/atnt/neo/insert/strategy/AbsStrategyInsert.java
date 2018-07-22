package com.atnt.neo.insert.strategy;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbsStrategyInsert implements StrategyInsert {

    private static final String DEVICE_PREFIX = "device_"+ ThreadLocalRandom.current().nextInt(0, 99999) + "_";

    private static final String DEFAULT_ORG_ID         = "org_id";
    private static final String DEFAULT_PROJECT_ID     = "project_id";
    private static final String DEFAULT_ENVIRONMENT    = "environment";


    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCount;
    private final String  org_id;
    private final String  project_id;
    private final String  environment;

    protected AbsStrategyInsert(String[] args) {
        Boolean truncate;
        Integer deviceCount;
        String org_id = DEFAULT_ORG_ID;
        String project_id = DEFAULT_PROJECT_ID;
        String environment = DEFAULT_ENVIRONMENT;

        truncate = Boolean.parseBoolean(args[0]);
        deviceCount = Integer.parseInt(args[1]);
        if (args.length>2) {
            org_id = args[2];
            project_id = args[3];
            environment = args[4];
        }

        System.out.println("truncate=["+truncate+"] deviceCount=["+deviceCount+"] org_id=["+org_id+"] project_id=["+project_id+"] environment=["+environment+"]");

        this.truncateTableBeforeStart = truncate;
        this.deviceCount = deviceCount;
        this.org_id = org_id;
        this.project_id = project_id;
        this.environment = environment;
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
        return this.truncateTableBeforeStart;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return this.deviceCount;
    }

    public final int getDeviceCountPerDay() {
        return this.deviceCount;
    }

    @Override
    public final String getOrgBucket() {
        return this.org_id;
    }

    @Override
    public final String getProjectBucket() {
        return this.project_id;
    }

    @Override
    public String getOrgId(int year, int month, int day, int hour, int minute, int deviceIndex) {
        return this.org_id;
    }

    @Override
    public final String getProjectId() {
        return this.project_id;
    }

    @Override
    public final String getEnvironment() {
        return this.environment;
    }
}
