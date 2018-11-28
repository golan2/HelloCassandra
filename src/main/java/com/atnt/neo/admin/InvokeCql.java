package com.atnt.neo.admin;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.strategy.StrategyConfig;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class InvokeCql {

    private final static Logger logger = LoggerFactory.getLogger(InvokeCql.class);


    public static void main(String[] args) throws ParseException {

        Cluster cluster = null;
        Session session = null;
        try {
            final long before = System.nanoTime();
            final StrategyConfig config = new StrategyConfig(args);
            logger.info("Configuration: {}", config);
            cluster = CassandraShared.initCluster(config.getHosts(), "activity");
            session = cluster.connect(config.getKeyspace());

            logger.debug("CQL =====>   \n" + config.getCql());

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
            logger.info("executeTime=[{}] fetchTime=[{}] ", (middle-before)/1_000_000_000, (after-middle)/1_000_000_000);
        } finally {
            if (cluster!=null) cluster.close();
            if (session!=null) session.close();
        }


    }
}
