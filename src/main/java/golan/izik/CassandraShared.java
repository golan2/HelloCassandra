package golan.izik;

import com.datastax.driver.core.Cluster;

public class CassandraShared {
    public static final String HOST                = "iot-toolbox";
    public static final String KEYSPACE            = "activity";
    public static final String CASSANDRA_HOST_NAME = "localhost";
    public static final String TABLE               = "data_collector2";
    public static final String AGGR_TABLE          = "data_aggregator";

    public static Cluster initCluster() {
        return Cluster.builder().addContactPoint(CASSANDRA_HOST_NAME).build();
    }
}
