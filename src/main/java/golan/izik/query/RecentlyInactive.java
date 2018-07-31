package golan.izik.query;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.atnt.neo.insert.generator.CassandraShared;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

class RecentlyInactive {

    private static final int    HOURS_IN_MONTH      = 30 * 24;

    private static final String SELECT_QUERY_TEMPLATE =
            "SELECT device_id from "+CassandraShared.T_COUNTERS_RAW_DATA +" WHERE year=%d and month=%d and day=%d and hour=%d AND user_bucket='user_bucket' and project_bucket='project_bucket' GROUP BY year,month,day,hour,user_bucket,project_bucket,user_id,project_id,environment,minutes,seconds,device_id;";


    public static void main(String[] args) {

        try (Cluster cluster = CassandraShared.initCluster("cassandra")) {
            Session session = cluster.connect("activity");
            Calendar cal = Calendar.getInstance();


            //noinspection SpellCheckingInspection
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(cal.getTimeZone());

            //Find devices that were active ib the last 72 hours
            final HashMap<String, String> debugMap = new HashMap<>();
            for (int i = 0; i < 72; i++) {
                final String cql = formatSelectQuery(cal);
                final ResultSet resultSet = session.execute(cql);
                for (Row row : resultSet) {
                    debugMap.put(row.getString(0), dateFormat.format(cal.getTime()) + " ["+cql+"]");
                }
                cal.add(Calendar.HOUR_OF_DAY, -1);
            }

            System.out.println(debugMap.entrySet().stream().map(e -> e.getKey() + " => " + e.getValue()).collect(Collectors.joining("\n")));


            Set<String> recentlyActive = debugMap.keySet();
            System.out.println("recentlyActive="+recentlyActive.stream().collect(Collectors.joining(", ")));

            //find devices that were active before 72 hrs and until 30 days back
            final HashSet<String> active = new HashSet<>();
            for (int i = 72; i < HOURS_IN_MONTH; i++) {
                final String cql = formatSelectQuery(cal);
                final ResultSet resultSet = session.execute(cql);
                for (Row row : resultSet) {
                    active.add(row.getString(0));
                }
                cal.add(Calendar.HOUR_OF_DAY, -1);
            }
            System.out.println("active="+active.stream().collect(Collectors.joining(", ")));

            //what we want is the list of devices that were active but without those that reported in the last 72 hrs
            active.removeAll(recentlyActive);

            System.out.println("recently inactive="+active.stream().collect(Collectors.joining(", ")));


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
