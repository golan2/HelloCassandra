package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.streams.AbsStrategyInsertStreams.GeoLocation;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.UDTValue;
import com.datastax.driver.core.UserType;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class CassandraShared {

    public static final String KEYSPACE_                 = "activity";
    public static final String CASSANDRA_HOST_NAME_      = "cassandra";
    public static final String T_RAW_DATA_TIME_BUCKET    = "message_info_time_bucket";
    public static final String T_STREAMS_BY_TIME         = "streams";
    public static final String T_STREAMS_LATEST          = "latest_streams_value";
    public static final String T_STREAMS_OVER_TIME       = "object_streams_by_time";
    public static final String T_OBJECT_STREAMS_BY_CLASS = "object_streams_by_class";
    public static final String T_STREAMS_OT_BY_OBJECT    = "single_stream_ot_by_object";
    public static final String T_STREAMS_MAP_RAW_DATA    = "data_collector";
    public static final String T_COUNTERS_RAW_DATA       = "message_info_by_type";
    public static final String T_COUNTERS_MINUTE         = "message_info_per_class_every_minute";
    public static final String T_COUNTERS_BY_CLASS       = "message_info_by_class";
    public static final String T_COUNTERS_HOURLY         = "hourly_aggregator";
    public static final String T_COUNTERS_DAILY          = "daily_aggregator";

    public static final String F_ORG_BUCKET     = "org_bucket";
    public static final String F_PROJECT_BUCKET = "project_bucket";
    public static final String F_ORG_ID         = "org_id";
    public static final String F_PROJECT_ID     = "project_id";
    public static final String F_ENVIRONMENT    = "environment";
    public static final String F_ENV_UUID       = "env_uuid";

    public static final String F_TIMESTAMP        = "timestamp";
    public static final String F_BUCKET_TIMESTAMP = "bucket_timestamp";
    public static final String F_PART_SELECTOR    = "part_selector";
    public static final String F_TIME_BUCKET      = "time_bucket";

    public static final String F_YEAR    = "year";
    public static final String F_MONTH   = "month";
    public static final String F_DAY     = "day";
    public static final String F_HOUR    = "hour";
    public static final String F_MINUTES = "minutes";
    public static final String F_SECONDS = "seconds";

    public static final String F_OBJECT_ID      = "object_id";
    public static final String F_CLASS_ID       = "class_id";
    public static final String F_TRANSACTION_ID = "transaction_id";

    public static final String F_VERTICAL_STREAM_NAME   = "stream_name";
    public static final String F_VERTICAL_STREAM_ID     = "stream_id";
    public static final String F_VERTICAL_STREAM_DOUBLE = "double_value";
    public static final String F_VERTICAL_BOOL_STREAM   = "value_boolean";
    public static final String F_VERTICAL_DOUBLE_STREAM = "value_double";
    public static final String F_VERTICAL_GEO_STREAM    = "value_geopoint";
    public static final String F_VERTICAL_STREAM_TEXT   = "string_value";
    public static final String F_VERTICAL_TEXT_STREAM   = "value_text";
    public static final String F_USER_PARAM             = "user_param";

    public static final String F_BILLING_POINTS = "billing_points";
    public static final String F_DATA_POINTS    = "data_points";
    public static final String F_VOLUME_SIZE    = "volume_size";

    public static final String TYPE_GEOPOINT    = "geopoint";

    public  static final int MAX_BATCH_SIZE            =   1_000;
    public  static final int MAX_PARALLELISM_CASSANDRA =      10;
    private static final int CLIENT_TIMEOUT            = 300_000;

    private static final Map<String, UserType> userDefinedTypes = new HashMap<>();

    public static Cluster initCluster(String hostName, String keyspace) {
        final Cluster cluster = createCluster(hostName);
        initUserDefinedTypes(keyspace, cluster);
        return cluster;
    }

    public static Cluster createCluster(String hostName) {
        return Cluster.builder()
                .withSocketOptions(new SocketOptions().setConnectTimeoutMillis(CLIENT_TIMEOUT))
                .withSocketOptions(new SocketOptions().setReadTimeoutMillis(CLIENT_TIMEOUT))
                .withSocketOptions(new SocketOptions().setTcpNoDelay(true))
                .addContactPoint(hostName).build();
    }

    private static void initUserDefinedTypes(String keyspace, Cluster cluster) {
        final KeyspaceMetadata ksmd = cluster.getMetadata().getKeyspace(keyspace);
        final UserType geopoint = ksmd.getUserType(TYPE_GEOPOINT);
        userDefinedTypes.put("geopoint", geopoint);
    }


    public static UDTValue createGeoPoint(GeoLocation geo) {
        final UDTValue udt = userDefinedTypes.get(TYPE_GEOPOINT).newValue();
        udt.setDouble("longitude", geo.getLongitude());
        udt.setDouble("latitude", geo.getLatitude());
        udt.setDouble("elevation", geo.getElevation());
        return udt;
    }


}
