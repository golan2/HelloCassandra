package golan.izik.query;


import com.datastax.driver.core.SocketOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CassandraConfiguration {
    private String [] hosts;
    private String keyspaceName;
    private int port;
    private SocketOptions options;
    private static final Logger logger = LoggerFactory.getLogger(CassandraConfiguration.class);

    CassandraConfiguration() {
        final String hosts = System.getProperty("hosts");
        this.hosts = hosts.split(",");
        this.port = 9042;
        this.keyspaceName = "activity";

        int ca_connectionTimeout = 30000;
        int ca_readTimeout = 300000;
        boolean ca_isTcpNoDelay = true;

        SocketOptions optionsVals = new SocketOptions();
        optionsVals.setConnectTimeoutMillis(ca_connectionTimeout);
        optionsVals.setReadTimeoutMillis(ca_readTimeout);
        optionsVals.setTcpNoDelay(ca_isTcpNoDelay);
        this.options = optionsVals;
        logger.info("------------ CONFIGURABLE VALUES -------------  \n - CASSANDRA_CONN_TIMEOUT: {}  \n - CASSANDRA_READ_TIMEOUT: {} \n - CASSANDRA_TCP_NO_DELAY: {} \n - CASSANDRA_HOSTS: {} \n - CASSANDRA_PORT: {} \n - CASSANDRA_KEYSPACE: {} \n--------------------------------------",
                ca_connectionTimeout, ca_readTimeout, ca_isTcpNoDelay, this.hosts, port, keyspaceName);
    }

    SocketOptions getOptions() {
        return this.options;
    }

    String getKeyspaceName() {
        return this.keyspaceName;
    }

    String [] getHosts() {
        return this.hosts;
    }

    int getPort() {
        return port;
    }


}
