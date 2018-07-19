package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertVerticalStreamsLatestValue;
import com.atnt.neo.insert.strategy.time.SingleDay;
import com.atnt.neo.insert.strategy.time.SingleTxn;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

public class StrategyInsertLatestGeo1936 extends AbsStrategyInsertStreamsVertical {
    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCount;
    private final String org_id;
    private final String project_id;
    private final String environment;

    public StrategyInsertLatestGeo1936(Boolean truncateTableBeforeStart, Integer deviceCount, String org_id, String project_id, String environment) {
        this.truncateTableBeforeStart = truncateTableBeforeStart;
        this.deviceCount = deviceCount;
        this.org_id = org_id;
        this.project_id = project_id;
        this.environment = environment;
    }

    public static void main(String[] args) throws InterruptedException {
        Boolean truncate;
        Integer deviceCount;
        String org_id = null;
        String project_id = null;
        String environment = null;

        try {
            truncate = Boolean.parseBoolean(args[0]);
            deviceCount = Integer.parseInt(args[1]);
            if (args.length>2) {
                org_id = args[2];
                project_id = args[3];
                environment = args[4];
            }
        } catch (Exception e) {
            truncate = false;
            deviceCount = 1;
        }
        System.out.println("truncate=["+truncate+"] deviceCount=["+deviceCount+"] org_id=["+org_id+"] project_id=["+project_id+"] environment=["+environment+"]");

        new InsertVerticalStreamsLatestValue(new StrategyInsertLatestGeo1936(truncate, deviceCount, org_id, project_id, environment)).insert();
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return this.truncateTableBeforeStart;
    }

    @Override
    public int getYear() {
        return 1936;
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new SingleDay(getYear());
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new SingleTxn();
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return this.deviceCount;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_STREAMS_LATEST;
    }

    @Override
    public String getOrgBucket() {
        return (this.org_id!=null ? this.org_id : super.getOrgBucket());
    }

    @Override
    public String getProjectBucket() {
        return (this.project_id !=null ? this.project_id : super.getProjectBucket());
    }

    @Override
    public String getOrgId(int year, int month, int day, int hour, int minute, int deviceIndex) {
        return (this.org_id!=null ? this.org_id : super.getOrgId(year, month, day, hour, minute, deviceIndex));
    }

    @Override
    public String getProjectId() {
        return (this.project_id !=null ? this.project_id : super.getProjectBucket());
    }

    @Override
    public String getEnvironment() {
        return (this.environment!=null ? this.environment : super.getEnvironment());
    }
}
