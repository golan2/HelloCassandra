package com.atnt.neo.insert.strategy;

import com.atnt.neo.insert.generator.CassandraShared;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class StrategyConfig {

    public static final int NOT_PROVIDED = -1;

    private final String  keyspace;
    private final String  hosts;
    private final Boolean truncate;
    private final Integer deviceCount;
    private final Integer year;
    private final Integer month;
    private final Integer days;
    private final Integer streamCount;
    private final String  orgId;
    private final String  projectId;
    private final String  environment;
    private final String  cql;
    private final Double  streamLowerBound;
    private final Double  streamUpperBound;
    private final Integer streamOutlierPercent;

    public StrategyConfig(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("k", "keyspace", true, "");
        options.addOption("h", "hosts", true, "Comma separated list of cassandra hosts");
        options.addOption("t", "truncate", true, "If to truncate table before start");
        options.addOption("d", "devices", true, "How many devices to insert");
        options.addOption("y", "year", true, "Which year to insert the date to");
        options.addOption("m", "month", true, "Which month to insert the date to");
        options.addOption("da", "days", true, "How many days to insert data to");
        options.addOption("s", "streams", true, "How many streams to insert");
        options.addOption("o", "org_id", true, "Organization id");
        options.addOption("p", "project_id", true, "Project id");
        options.addOption("e", "environment", true, "Environment name");
        options.addOption("c", "cql", true, "CQL statement to invoke");
        options.addOption("slb", "stream-lower-bound", true, "Lower bound for the stream value (inclusive)");
        options.addOption("sub", "stream-upper-bound", true, "Upper bound for the stream value (inclusive)");
        options.addOption("sop", "stream-outlier-percent", true, "How many outlier points we want (given in percent [0-100] )");


        CommandLineParser parser = new BasicParser();
        final CommandLine commandLine = parser.parse(options, args);
        this.keyspace = commandLine.getOptionValue("keyspace", CassandraShared.KEYSPACE_);
        this.hosts = commandLine.getOptionValue("hosts", CassandraShared.CASSANDRA_HOST_NAME_);
        this.truncate = Boolean.valueOf(commandLine.getOptionValue("truncate", Boolean.FALSE.toString()));
        this.deviceCount = Integer.valueOf(commandLine.getOptionValue("devices", "1"));
        this.year = Integer.valueOf(commandLine.getOptionValue("year", ""+NOT_PROVIDED));
        this.month = Integer.valueOf(commandLine.getOptionValue("month", ""+NOT_PROVIDED));
        this.days = Integer.valueOf(commandLine.getOptionValue("days", ""+NOT_PROVIDED));
        this.streamCount = Integer.valueOf(commandLine.getOptionValue("streams", "1"));
        this.orgId = commandLine.getOptionValue("org_id", "org_id");
        this.projectId = commandLine.getOptionValue("project_id", "project_id");
        this.environment = commandLine.getOptionValue("environment", "environment");
        this.cql  = commandLine.getOptionValue("cql", "N/A");
        this.streamLowerBound = Double.valueOf(commandLine.getOptionValue("slb", "0"));
        this.streamUpperBound = Double.valueOf(commandLine.getOptionValue("sub", "100"));
        this.streamOutlierPercent = Integer.valueOf(commandLine.getOptionValue("sop", "0"));

    }


    public String getKeyspace() {
        return keyspace;
    }

    public String getHosts() {
        return hosts;
    }

    public Boolean getTruncate() {
        return truncate;
    }

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDays() {
        return days;
    }

    public Integer getStreamCount() {
        return streamCount;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getCql() {
        return cql;
    }

    public Double getStreamLowerBound() {
        return streamLowerBound;
    }

    public Double getStreamUpperBound() {
        return streamUpperBound;
    }

    public Integer getStreamOutlierPercent() {
        return streamOutlierPercent;
    }

    @Override
    public String toString() {
        return "StrategyConfig{" +
                "keyspace='" + keyspace + '\'' +
                ", hosts='" + hosts + '\'' +
                ", truncate=" + truncate +
                ", deviceCount=" + deviceCount +
                ", year=" + year +
                ", month=" + month +
                ", days=" + days +
                ", streamCount=" + streamCount +
                ", orgId='" + orgId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", environment='" + environment + '\'' +
                ", cql='" + cql + '\'' +
                ", streamLowerBound=" + streamLowerBound +
                ", streamUpperBound=" + streamUpperBound +
                ", streamOutlierPercent=" + streamOutlierPercent +
                '}';
    }
}
