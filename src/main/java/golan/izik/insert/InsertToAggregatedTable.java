package golan.izik.insert;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import golan.izik.CassandraShared;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Future;

public class InsertToAggregatedTable {
    private static final SimpleDateFormat DF_DATE = new SimpleDateFormat("YYYY-MM-dd"             );
    private static final SimpleDateFormat DF_LOG  = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss,sss");

    private final InsertStrategy strategy;

    InsertToAggregatedTable(InsertStrategy insertStrategy) {
        this.strategy = insertStrategy;
    }

    static String logTimestamp() {
        return DF_LOG.format(System.currentTimeMillis());
    }

    protected void insert() throws InterruptedException{
        List<ResultSetFuture> futures = new ArrayList<>();

        try (Cluster cluster = CassandraShared.initCluster()) {
            System.out.println(logTimestamp() + " Working on ["+CassandraShared.KEYSPACE+"] ["+strategy.getTableName()+"]");
            Session session = cluster.connect(CassandraShared.KEYSPACE);
            if (strategy.shouldTruncateTableBeforeStart()) {
                session.execute("truncate table "+strategy.getTableName()+";");
            }

            Batch batch = QueryBuilder.unloggedBatch();
            int batchSize = 0;
            Calendar cal = strategy.getFirstDay();
            final Calendar lastDay = strategy.getLastDay();
            while ( cal.compareTo(lastDay) <= 0 ) {

                final long  begin         = System.nanoTime();
                final int   month         = cal.get(Calendar.MONTH) + 1;
                final int   day           = cal.get(Calendar.DAY_OF_MONTH);
                final int   deviceCount   = strategy.getDeviceCountPerDay(cal);
                final int   rowsPerDay    = strategy.getDailyRowsCountPerDevice(cal);

                if ((strategy.isHourExist()) && (rowsPerDay  <=0 || rowsPerDay  >24)) throw new IllegalArgumentException("The value of [getDailyRowsCountPerDevice] is ["+rowsPerDay  +"] and should be between 1 and 24 (including both)");

                System.out.println(logTimestamp() + " Inserting ["+deviceCount+"] devices for day ["+DF_DATE.format(cal.getTime())+"]...");

                for (int deviceIndex=0 ; deviceIndex<deviceCount ; deviceIndex++) {
                    String deviceId = getDeviceId(month, day, deviceIndex);
                    for (int txn = 0; txn<rowsPerDay ; txn++) {
                        final Insert insert = createInsertQuery();
                        insert.value("month", month);
                        insert.value("day", day);
                        if (strategy.isHourExist()) {
                            insert.value("hour", txn);
                        }
                        insert.value("device_id", deviceId);
                        batch.add(insert);
                        batchSize++;
                    }
                    //avoid overwhelming Cassandra
                    long done = futures.stream().filter(Future::isDone).count();
                    long pending = futures.stream().filter(f -> !f.isDone()).count();
                    while (pending > CassandraShared.MAX_PARALLELISM_CASSANDRA) {
                        Thread.sleep(500);
                        pending = futures.stream().filter(f -> !f.isDone()).count();
                    }
                    if (batchSize > CassandraShared.MAX_BATCH_SIZE) {
                        futures.add(session.executeAsync(batch));
                        batch = QueryBuilder.unloggedBatch();
                        batchSize=0;
                        System.out.printf("\tBatches: Pending=[%d] Done=[%d] \n", pending, done);
                    }
                }

                if (batchSize>0) {
                    futures.add(session.executeAsync(batch));
                    batch = QueryBuilder.unloggedBatch();
                }

                futures.removeIf(Future::isDone);
                final long pending = futures.stream().filter(f -> !f.isDone()).count();
                final long end = System.nanoTime();
                System.out.println(logTimestamp() + " - date=["+day+"/"+month+"] deviceCount=["+deviceCount+"] rowsPerDay  =["+rowsPerDay  +"] time=["+(end-begin)/1000000+"] pending=["+ pending +"]");
                strategy.incrementCalendar(cal);
            }

        }

        System.out.println(logTimestamp() + " - Done=["+futures.stream().filter(Future::isDone).count()+"] Cancelled=["+futures.stream().filter(Future::isCancelled).count()+"]");
    }

    String getDeviceId(int month, int day, int deviceIndex) {
        return strategy.getDeviceId(month, day, deviceIndex);
    }

    private Insert createInsertQuery() {
//new ContextFields("m", "a", "m", "a", "n");

        final Insert insert = QueryBuilder.insertInto(CassandraShared.KEYSPACE, strategy.getTableName());

//        insert.value("org_bucket", "org_bucket");
//        insert.value("project_bucket", "project_bucket");
//        insert.value("org_id", "org_id");
//        insert.value("project_id", "project_id");
//        insert.value("environment", "environment");

        insert.value("org_bucket", "m");
        insert.value("project_bucket", "a");
        insert.value("org_id", "m");
        insert.value("project_id", "a");
        insert.value("environment", "n");

        insert.value("year", strategy.getYear());
        insert.value("counter", 10);
        insert.value("data_points", 20);
        insert.value("volume_size", 30);
        return insert;
    }
}
