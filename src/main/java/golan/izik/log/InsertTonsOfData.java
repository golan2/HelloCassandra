package golan.izik.log;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InsertTonsOfData {
    private static final String   KEYSPACE         = "bactivity";

    public static void main(String[] args) throws InterruptedException {
        try (Cluster cluster = initCluster()) {
            Session session = cluster.connect(KEYSPACE);
            session.execute("truncate table data_collector;");

            final ExecutorService tpe = Executors.newFixedThreadPool(4);
            Calendar cal = Calendar.getInstance();
            for (int hour = 0 ; hour<32*24 ; hour++) {
                tpe.submit(new HourlyInjector(session, "hr_"+hour, cal));
                cal.add(Calendar.HOUR_OF_DAY, -1);
            }

            tpe.shutdown();
            tpe.awaitTermination(30, TimeUnit.MINUTES);
        }
    }

    private static Cluster initCluster() {
        return Cluster.builder()
                .addContactPoint("localhost")
                .build();
    }


    /**
     * Inserts one hour of data for a single device (device_id)
     * One hour of data ==> one data point per second (i.e. 3600 data points)
     */
    private static class HourlyInjector implements Runnable {
        private final Session session;
        private final String  deviceId;
        private final int     year;
        private final int     month;
        private final int     day;
        private final int     hour;
        private final long    beginOfHour;

        private HourlyInjector(Session session, String deviceId, Calendar c) {
            System.out.println(deviceId + " - Submitted");

            this.session  = session;
            this.deviceId = deviceId;
            this.year     = c.get(Calendar.YEAR        );
            this.month    = c.get(Calendar.MONTH) + 1;
            this.day      = c.get(Calendar.DAY_OF_MONTH);
            this.hour     = c.get(Calendar.HOUR_OF_DAY );
            beginOfHour   = c.getTimeInMillis()   - 1000 * (c.get(Calendar.MINUTE) * 60 + c.get(Calendar.SECOND));
        }


        @Override
        public void run() {
            try {
                long ts = System.nanoTime();
//                System.out.println(deviceId + String.format(" - BEGIN - deviceId=[%s] time=[%d-%d-%d %d]", deviceId, year, month, day, hour));
                insertHourlyData();
                System.out.println(deviceId + String.format(" - OK - deviceId=[%s] time=[%d-%d-%d %d] perf=[%d]", deviceId, year, month, day, hour, (System.nanoTime()-ts)/ 1_000_000_000));
//                System.out.println(deviceId + " - END ("+(System.nanoTime()-ts)/ 1_000_000_000 +" seconds)");
            } catch (Exception e) {
                System.out.println(deviceId + " - ERR - " + e.getMessage());
            }
        }

        private void insertHourlyData() {

            Batch batch = QueryBuilder.unloggedBatch();

            long timestamp = this.beginOfHour;
            for (int minutes = 0 ; minutes<60 ; minutes++) {
                for (int seconds = 0 ; seconds<60 ; seconds++) {
                    RegularStatement insert = QueryBuilder.insertInto(KEYSPACE, "data_collector")
                            .values(
                                    new String[] {"year", "month", "day", "hour", "minutes", "seconds", "user_bucket",   "project_bucket",   "user_id",   "project_id",   "environment", "device_id", "timestamp", "device_firmware",   "device_type"} ,
                                    new Object[] { year,   month,   day,   hour,   minutes,   seconds,  "user_bucket",   "project_bucket",   "user_id",   "project_id",   "environment",  deviceId,    timestamp,  "device_firmware",   "device_type"}
                            );
                    insert.setConsistencyLevel(ConsistencyLevel.LOCAL_ONE);

                    batch.add(insert);

//                    final String cql = String.format(INSERT_QUERY_TEMPLATE, year, month, day, hour, minutes, seconds, deviceId, timestamp);
//                    session.execute(cql);
                    timestamp +=1000;
                }
            }
            session.execute(batch);



        }
    }

}
