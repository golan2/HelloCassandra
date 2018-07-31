package com.atnt.neo.insert.strategy;

import com.atnt.neo.insert.generator.CassandraShared;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class StrategyConfig {

    private final String  keyspace;
    private final String  hosts;
    private final Boolean truncate;
    private final Integer deviceCount;
    private final String  orgId;
    private final String  projectId;
    private final String  environment;
    private final String  cql;

    public StrategyConfig(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("k", "keyspace", true, "");
        options.addOption("h", "hosts", true, "Comma separated list of cassandra hosts");
        options.addOption("t", "truncate", true, "If to truncate table before start");
        options.addOption("d", "devices", true, "How many devices to insert");
        options.addOption("o", "org_id", true, "Organization id");
        options.addOption("p", "project_id", true, "Project id");
        options.addOption("e", "environment", true, "Environment name");
        options.addOption("c", "cql", true, "CQL statement to invoke");


        CommandLineParser parser = new BasicParser();
        final CommandLine commandLine = parser.parse(options, args);
        this.keyspace = commandLine.getOptionValue("keyspace", CassandraShared.KEYSPACE_);
        this.hosts = commandLine.getOptionValue("hosts", CassandraShared.CASSANDRA_HOST_NAME_);
        this.truncate = Boolean.valueOf(commandLine.getOptionValue("truncate", Boolean.FALSE.toString()));
        this.deviceCount = Integer.valueOf(commandLine.getOptionValue("devices", "1"));
        this.orgId = commandLine.getOptionValue("org_id", "org_id");
        this.projectId = commandLine.getOptionValue("project_id", "project_id");
        this.environment = commandLine.getOptionValue("environment", "environment");
        this.cql  = commandLine.getOptionValue("cql", "N/A");

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

    @Override
    public String toString() {
        return "StrategyConfig{" +
                "keyspace='" + keyspace + '\'' +
                ", hosts='" + hosts + '\'' +
                ", truncate=" + truncate +
                ", deviceCount=" + deviceCount +
                ", orgId='" + orgId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", environment='" + environment + '\'' +
                ", cql='" + cql + '\'' +
                '}';
    }
}
