package golan.izik.insert;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import golan.izik.CassandraShared;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public abstract class AbsHourInjector implements Runnable {
    private final Session session;
    private final String  deviceId;
    private final int     year;
    private final int     month;
    private final int     day;
    private final int     hour;
    private final long    beginOfHour;

    AbsHourInjector(Session session, String deviceId, Calendar c) {
        this.day         = c.get(Calendar.DAY_OF_MONTH);
        this.session     = session;
        this.deviceId    = deviceId;
        this.year        = c.get(Calendar.YEAR        );
        this.hour        = c.get(Calendar.HOUR_OF_DAY );
        this.beginOfHour = c.getTimeInMillis()   - 1000 * (c.get(Calendar.MINUTE) * 60 + c.get(Calendar.SECOND));
        this.month       = c.get(Calendar.MONTH) + 1;
        System.out.println(deviceId + " - Submitted");
    }

    @Override
    public void run() {
        try {
            long ts = System.nanoTime();
            insertHourlyData();
            System.out.println(deviceId + String.format(" - OK - deviceId=[%s] time=[%d-%d-%d %d] perf=[%d]", deviceId, year, month, day, hour, (System.nanoTime()-ts)/ 1_000_000_000));
        } catch (Exception e) {
            System.out.println(deviceId + " - ERR - " + e.getMessage());
        }
    }

    private void insertHourlyData() {
        Batch batch = QueryBuilder.unloggedBatch();
        long timestamp = this.beginOfHour;

        for (int minutes : getMinutesArray(this.hour)) {
            int lastOne = -1;
            for (int seconds : getSecondsArray(minutes)) {
                if (lastOne!=-1) {
                    timestamp += Math.abs(seconds-lastOne)*1000;    //change the timestamp according to the diff in seconds between rounds
                }
                RegularStatement insert = QueryBuilder.insertInto(CassandraShared.KEYSPACE, CassandraShared.RAW_DATA_TABLE)
                        .values(
                                new String[]{"year", "month", "day", "hour", "minutes", "seconds", "user_bucket", "project_bucket", "user_id", "project_id", "environment", "device_id", "timestamp", "device_firmware", "device_type"},
                                new Object[]{year, month, day, hour, minutes, seconds, "user_bucket", "project_bucket", "user_id", "project_id", "environment", deviceId, timestamp, "device_firmware", "device_type"}
                        );
                insert.setConsistencyLevel(ConsistencyLevel.LOCAL_ONE);
                batch.add(insert);
                lastOne = seconds;
            }
        }

        session.execute(batch);
    }

    protected abstract Set<Integer> getMinutesArray(int hour);

    protected abstract Set<Integer> getSecondsArray(int minutes);

    static Set<Integer> generateFullRange() {
        final HashSet<Integer> result = new HashSet<>();
        for (int i = 0; i < 60; i++) {
            result.add(i);
        }
        return result;
    }
}
