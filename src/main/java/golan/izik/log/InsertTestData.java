package golan.izik.log;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import java.util.Calendar;

public class InsertTestData {
    private static final String   KEYSPACE         = "bactivity";

    private static final String INSERT_QUERY_TEMPLATE =
            "INSERT INTO "+ KEYSPACE +".data_collector " +
                    "(year, month, day, hour, minutes, seconds, user_bucket,   project_bucket,   user_id,   project_id,   environment, device_id, timestamp, device_firmware,   device_type,   user_param) " +
                    "VALUES " +
                    "(%d,   %d,    %d,  %d,   %d,      %d,      'user_bucket', 'project_bucket', 'user_id', 'project_id', 'environment',   '%s',      %d,    'device_firmware', 'device_type', {'eventType': 'Flow','name': 'Calamp'}  );";



    /**
     *  Create one device of each type:
     *  [1] Always Active - devices that report data all the time
     *  [2] Recently Active - devices only  report in the last 72 hours
     *  [3] Recently Inactive - devices that reported in the last month but not in the last 72 hours
     *  [4] Always Inactive - devices that didn't report at all in the last month
     */
    public static void main(String[] args) {
        try (Cluster cluster = initCluster()) {

            Session session = cluster.connect(KEYSPACE);

            session.execute("truncate table "+KEYSPACE+".data_collector ;");

            //Always Active
            Calendar cal = Calendar.getInstance();
            for (int hour = 0 ; hour<32*24 ; hour++) {
                String cql = formatInsertQuery(cal, "always_active_1");
                session.execute(cql);
                cal.add(Calendar.HOUR_OF_DAY, -1);
            }

            //Recently Active
            cal = Calendar.getInstance();
            for (int hour = 0 ; hour<72 ; hour++) {
                String cql = formatInsertQuery(cal, "recently_active_1");
                session.execute(cql);
                cal.add(Calendar.HOUR_OF_DAY, -1);
            }

            //Recently Inactive
            cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, -72);
            for (int hour = 73 ; hour<32*24 ; hour++) {
                String cql = formatInsertQuery(cal, "recently_inactive_1");
                session.execute(cql);
                cal.add(Calendar.HOUR_OF_DAY, -1);
            }

            //Always Inactive
            cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, -30*24);
            for (int hour = 30*24 ; hour<32*24 ; hour++) {
                String cql = formatInsertQuery(cal, "always_inactive_1");
                session.execute(cql);
                cal.add(Calendar.HOUR_OF_DAY, -1);
            }
        }
    }


    private static String formatInsertQuery(Calendar c, String deviceId) {
        int year   = c.get(Calendar.YEAR        );
        int month  = c.get(Calendar.MONTH) + 1;
        int day    = c.get(Calendar.DAY_OF_MONTH);
        int hour   = c.get(Calendar.HOUR_OF_DAY );
        int minute = c.get(Calendar.MINUTE      );
        int second = c.get(Calendar.SECOND      );

        return String.format(INSERT_QUERY_TEMPLATE, year, month, day, hour, minute, second, deviceId, c.getTimeInMillis());
    }

    private static Cluster initCluster() {
        return Cluster.builder().addContactPoint("localhost").build();
    }


}
