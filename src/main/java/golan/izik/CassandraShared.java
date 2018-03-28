package golan.izik;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.SocketOptions;

public class CassandraShared {

    public  static final String HOST                      = "iot-toolbox";
    public  static final String KEYSPACE                  = "bactivity";
    public  static final String CASSANDRA_HOST_NAME       = "cassandra";
    public  static final String RAW_DATA_TABLE            = "data_collector2";
    public  static final int    MAX_BATCH_SIZE            =     100;
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
