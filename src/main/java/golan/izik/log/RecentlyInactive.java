package golan.izik.log;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.Calendar;
import java.util.HashSet;
import java.util.stream.Collectors;

public class RecentlyInactive {

    private static final int    HOURS_IN_MONTH        = 30 * 24;
    private static final String CASSANDRA_HOST_NAME   = "localhost";
    private static final String SELECT_QUERY_TEMPLATE =
            "SELECT device_id from activity.data_collector WHERE year=%d and month=%d and day=%d and hour=%d AND user_bucket='user_bucket' and project_bucket='project_bucket' GROUP BY year,month,day,hour,user_bucket,project_bucket,user_id,project_id,environment,device_id;";


    public static void main(String[] args) {

        try (Cluster cluster = initCluster()) {
            Session session = cluster.connect("activity");
            Calendar cal = Calendar.getInstance();

            //Find devices that were active ib the last 72 hours
            final HashSet<String> recentlyActive = new HashSet<>();
            for (int i = 0; i < 72; i++) {
                final String cql = formatSelectQuery(cal);
                final ResultSet resultSet = session.execute(cql);
                for (Row row : resultSet) {
                    recentlyActive.add(row.getString(0));
                }
                cal.add(Calendar.HOUR_OF_DAY, -1);
            }
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

    private static Cluster initCluster() {
        return Cluster.builder().addContactPoint(CASSANDRA_HOST_NAME).build();
    }


}
