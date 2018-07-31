package golan.izik.query;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;

public class CassandraConnection
{
    private static CassandraConfiguration configurations;
    private static Cluster cluster;
    private static Session session;

    public static synchronized Session getSession()
    {
        if(session == null)
        {
            configurations = new CassandraConfiguration();
            initSession();
        }

        return session;
    }

    private static void initSession()
    {
        QueryOptions queryOptions = new QueryOptions().setConsistencyLevel(ConsistencyLevel.LOCAL_ONE);

        cluster = Cluster.builder()
                .addContactPoints(configurations.getHosts())
                .withPort(configurations.getPort())
                .withSocketOptions(configurations.getOptions())
                .withQueryOptions(queryOptions)
                .build();
        session = cluster.connect(configurations.getKeyspaceName());
    }


    public static void close()
    {
        try {
            session.close();
            cluster.close();
        }
        catch (Exception ignore) {}
    }
}