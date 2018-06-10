package com.atnt.neo.insert.generator;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.SocketOptions;

public class CassandraShared {

    public  static final String HOST                      = "iot-toolbox";
    public  static final String KEYSPACE                  = "activity";
    public  static final String CASSANDRA_HOST_NAME       = "cassandra";
    public  static final String RAW_DATA_TIME_BUCKET      = "message_info_time_bucket";
    public  static final String T_STREAMS_VERTICAL        = "streams_data_by_time";
    public  static final String T_STREAMS_MAP_RAW_DATA    = "data_collector";
    public  static final String T_COUNTERS_RAW_DATA       = "message_info_by_type";
    public  static final String T_COUNTERS_HOURLY         = "hourly_aggregator";
    public  static final String T_COUNTERS_DAILY          = "daily_aggregator";
    public  static final String F_VERTICAL_STREAM_NAME    = "stream_name";
    public  static final String F_VERTICAL_STREAM_NUMBER  = "value_number";
    public  static final String TEXT_STREAM_FIELD_NAME    = "value_text";
    public  static final String BOOLEAN_STREAM_FIELD_NAME = "value_boolean";
    public  static final int    MAX_BATCH_SIZE            =   1_000;
    public  static final int    MAX_PARALLELISM_CASSANDRA =      10;
    private static final int    CLIENT_TIMEOUT            = 300_000;

    public static Cluster initCluster() {
        return Cluster.builder()
                .withSocketOptions( new SocketOptions().setConnectTimeoutMillis(CLIENT_TIMEOUT) )
                .withSocketOptions( new SocketOptions().setReadTimeoutMillis(CLIENT_TIMEOUT) )
                .withSocketOptions( new SocketOptions().setTcpNoDelay(true) )
                .addContactPoint(CASSANDRA_HOST_NAME).build();
    }
}
