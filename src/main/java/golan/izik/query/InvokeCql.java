package golan.izik.query;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.strategy.StrategyConfig;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.apache.commons.cli.ParseException;

public class InvokeCql {

    public static void main(String[] args) throws ParseException {
        Cluster cluster = null;
        Session session = null;
        try {
            final long before = System.nanoTime();
            final StrategyConfig config = new StrategyConfig(args);
            System.out.println(config);
            cluster = CassandraShared.initCluster(config.getHosts());
            session = cluster.connect(config.getKeyspace());

            System.out.println("CQL =====>   \n" + config.getCql());

            final ResultSet resultSet = session.execute(config.getCql());
            final long middle = System.nanoTime();

            for (ColumnDefinitions.Definition def : resultSet.getColumnDefinitions()) {
                System.out.printf("%s, ", def.getName());
            }
            System.out.println();

            System.out.println("======");
            while (resultSet.iterator().hasNext()) {
                Row row = resultSet.iterator().next();
                for (ColumnDefinitions.Definition col: resultSet.getColumnDefinitions()) {
                    System.out.printf("%s, ", String.valueOf(row.getObject(col.getName())));
                }
                System.out.println();
            }
            System.out.println("======");
            final long after = System.nanoTime();
            System.out.println("executeTime=["+((middle-before)/1_000_000_000)+"] fetchTime=["+((after-middle)/1_000_000_000)+"] ");
        } finally {
            if (cluster!=null) cluster.close();
            if (session!=null) session.close();
        }


    }
}
