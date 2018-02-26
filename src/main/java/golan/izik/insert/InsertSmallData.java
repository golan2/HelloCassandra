package golan.izik.insert;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import golan.izik.CassandraShared;

import java.util.Calendar;

public class InsertSmallData {
    private static final String[] ACTIVE_DEVICES   = {"active1", "active2", "active3"      };
    private static final String[] INACTIVE_DEVICES = {"inactive1", "inactive2", "inactive3"};

    private static final String SELECT_QUERY_TEMPLATE =
            "SELECT device_id from activity.data_collector WHERE year=%d and month=%d and day=%d and hour=%d AND user_bucket='user_bucket' and project_bucket='project_bucket' GROUP BY year,month,day,hour,user_bucket,project_bucket,user_id,project_id,environment,device_id;";
    private static final String INSERT_QUERY_TEMPLATE =
            "INSERT INTO "+ CassandraShared.KEYSPACE +".data_collector " +
                    "(year, month, day, hour, minutes, seconds, user_bucket,   project_bucket,   user_id,   project_id,   environment, device_id, timestamp, device_firmware,   device_type,   user_param) " +
                    "VALUES " +
                    "(%d,   %d,    %d,  %d,   %d,      %d,      'user_bucket', 'project_bucket', 'user_id', 'project_id', 'environment',   '%s',      %d,    'device_firmware', 'device_type', {'eventType': 'Flow','name': 'Calamp'}  );";


    public static void main(String[] args) {
        insertSmallData();
    }

    private static void insertSmallData() {
        long startTime = System.nanoTime();

        try (Cluster cluster = CassandraShared.initCluster()) {

            Session session = cluster.connect("activity");
            Calendar now = Calendar.getInstance();

            long milliseconds;

            session.execute("truncate table data_collector ;");

            //Events for current time
            milliseconds = now.getTimeInMillis();
            for (int i = 0; i < ACTIVE_DEVICES.length; i++) {
                final String deviceId = ACTIVE_DEVICES[i%(ACTIVE_DEVICES.length )];
                final String cql = formatInsertQuery(++milliseconds, deviceId);
                session.execute(cql);
            }
            System.out.println(formatSelectQuery(milliseconds));

            //Events for 2 weeks back (for both active and inactive)
            now.add(Calendar.DATE, -14);
            milliseconds = now.getTimeInMillis();
            for (int i = 0; i < INACTIVE_DEVICES.length; i++) {
                final String deviceId = INACTIVE_DEVICES[i%(INACTIVE_DEVICES.length)];
                final String cql = formatInsertQuery(++milliseconds, deviceId);
                session.execute(cql);
            }
            for (int i = 0; i < ACTIVE_DEVICES.length; i++) {
                final String deviceId = ACTIVE_DEVICES[i%(ACTIVE_DEVICES.length)];
                final String cql = formatInsertQuery(++milliseconds, deviceId);
                session.execute(cql);
            }
            System.out.println(formatSelectQuery(milliseconds));


            System.out.println("insertion took "+((System.nanoTime() - startTime)/ 1_000_000_000) + " seconds ...");

        }
    }

    private static void insert3records(Session session, Calendar cal, int day, String devicePrefix) {
        //avoid overflow to next day
        if (cal.get(Calendar.HOUR_OF_DAY)>10) {
            cal.add(Calendar.HOUR_OF_DAY, -6);
        }

        String cql = formatInsertQuery(cal, devicePrefix + "_1_" + day);
        System.out.println(cql);
        session.execute(cql);

        cal.add(Calendar.HOUR_OF_DAY, 2);

        cql = formatInsertQuery(cal, devicePrefix + "_2_" + day);
        System.out.println(cql);
        session.execute(cql);

        cal.add(Calendar.HOUR_OF_DAY, 2);

        cql = formatInsertQuery(cal, devicePrefix + "_3_" + day);
        System.out.println(cql);
        session.execute(cql);
    }


    private static String formatInsertQuery(long milliseconds, String deviceId) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milliseconds);
        return formatInsertQuery(c, deviceId);
    }

    private static String formatInsertQuery(Calendar c, String deviceId) {
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int hr = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);

        return String.format(INSERT_QUERY_TEMPLATE, mYear, mMonth, mDay, hr, min, sec, deviceId, c.getTimeInMillis());
    }

    private static String formatSelectQuery(long milliseconds) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milliseconds);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int hr = c.get(Calendar.HOUR_OF_DAY);

        return String.format(SELECT_QUERY_TEMPLATE, mYear, mMonth, mDay, hr);
    }

}


