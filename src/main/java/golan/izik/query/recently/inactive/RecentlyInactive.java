package golan.izik.query.recently.inactive;

import com.atnt.neo.insert.strategy.StrategyConfig;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.atnt.neo.insert.generator.CassandraShared;
import org.apache.commons.cli.ParseException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

class RecentlyInactive {

    private static final int    HOURS_IN_MONTH      = 30 * 24;

    private static final String SELECT_QUERY_TEMPLATE =
            "" +
                    "SELECT device_id " +
                    "FROM " + CassandraShared.T_COUNTERS_RAW_DATA + " " +
                    "WHERE org_bucket='org_bucket' and project_bucket='project_bucket' AND year=%d and month=%d and day=%d and hour=%d " +
                    "GROUP BY org_bucket, project_bucket, year, month, day, hour, org_id, project_id, environment, minutes, seconds, device_id, timestamp";


    public static void main(String[] args) throws ParseException {

        final StrategyConfig config = new StrategyConfig(args);
        System.out.println(config);

        try (Cluster cluster = CassandraShared.initCluster(config.getHosts())) {
            Session session = cluster.connect(config.getKeyspace());
            Calendar cal = Calendar.getInstance();


            //noinspection SpellCheckingInspection
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(cal.getTimeZone());

            System.out.println("Find devices that were active in the last 72 hours");
            final HashMap<String, String> debugMap = new HashMap<>();
            String cql = "[TBD]";
            try {
                for (int i = 0; i < 72; i++) {
                    cql = formatSelectQuery(cal);
                    final ResultSet resultSet = session.execute(cql);
                    for (Row row : resultSet) {
                        debugMap.put(row.getString(0), dateFormat.format(cal.getTime()) + " ["+cql+"]");
                    }
                    cal.add(Calendar.HOUR_OF_DAY, -1);
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                System.err.println("CQL: " + cql);
                throw e;
            }

            System.out.println(debugMap.entrySet().stream().map(e -> e.getKey() + " => " + e.getValue()).collect(Collectors.joining("\n")));


            Set<String> recentlyActive = debugMap.keySet();
            System.out.println("recentlyActive="+ String.join(", ", recentlyActive));

            System.out.println("Find devices that were active before 72 hrs and until 30 days back");
            final HashSet<String> active = new HashSet<>();
            try {
                for (int i = 72; i < HOURS_IN_MONTH; i++) {
                    cql = formatSelectQuery(cal);
                    final ResultSet resultSet = session.execute(cql);
                    for (Row row : resultSet) {
                        active.add(row.getString(0));
                    }
                    cal.add(Calendar.HOUR_OF_DAY, -1);
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                System.err.println("CQL: " + cql);
                throw e;
            }
            System.out.println("active="+ String.join(", ", active));

            //what we want is the list of devices that were active but without those that reported in the last 72 hrs
            active.removeAll(recentlyActive);

            System.out.println("recently inactive="+ String.join(", ", active));


        }
    }

    private static String formatSelectQuery(Calendar c) {
        int mYear  = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH) + 1;
        int mDay   = c.get(Calendar.DAY_OF_MONTH);
        int hr     = c.get(Calendar.HOUR_OF_DAY);
        return String.format(SELECT_QUERY_TEMPLATE, mYear, mMonth, mDay, hr);
    }


}
