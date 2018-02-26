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
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

public class InsertToAggregatedTable {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd HH:MM:ss,sss");

    private final InsertStrategy strategy;

    public InsertToAggregatedTable(InsertStrategy insertStrategy) {
        this.strategy = insertStrategy;
    }

    protected void insert() throws InterruptedException{
        List<ResultSetFuture> futures = new ArrayList<>();

        try (Cluster cluster = CassandraShared.initCluster()) {

            Session session = cluster.connect(CassandraShared.KEYSPACE);
            if (strategy.shouldTruncateTableBeforeStart()) {
                session.execute("truncate table data_aggregator;");
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
                final int   txnPerDevice  = strategy.getTransactionCountPerDevice(cal);
                if (txnPerDevice<=0 || txnPerDevice>24) throw new IllegalArgumentException("The value of [getTransactionCountPerDevice] is ["+txnPerDevice+"] and should be between 1 and 24 (including both)");

                for (int deviceIndex=0 ; deviceIndex<deviceCount ; deviceIndex++) {
                    String deviceId = getDeviceId(month, day, deviceIndex);
                    for (int txn = 0; txn<txnPerDevice ; txn++) {
                        final Insert insert = createInsertQuery();
                        insert.value("month", month);
                        insert.value("day", day);
                        insert.value("hour", txn);
                        insert.value("device_id", deviceId);
                        batch.add(insert);
                        batchSize++;
                    }
                    //avoid overwhelming Cassandra
                    long pending = futures.stream().filter(f -> !f.isDone()).count();
                    while (pending > CassandraShared.MAX_PARALLELISM_CASSANDRA) {
                        Thread.sleep(500);
                        pending = futures.stream().filter(f -> !f.isDone()).count();
                    }
                    if (batchSize> CassandraShared.MAX_BATCH_SIZE) {
                        futures.add(session.executeAsync(batch));
                        batch = QueryBuilder.unloggedBatch();
                        batchSize=0;
                    }

                }

                if (batchSize>0) {
                    futures.add(session.executeAsync(batch));
                    batch = QueryBuilder.unloggedBatch();
                }



                strategy.incrementCalendar(cal);
                final long pending = futures.stream().filter(f -> !f.isDone()).count();
                final long end = System.nanoTime();
                System.out.println(SIMPLE_DATE_FORMAT.format( new Date(System.currentTimeMillis()) ) + " - date=["+day+"/"+month+"] deviceCount=["+deviceCount+"] txnPerDevice=["+txnPerDevice+"] time=["+(end-begin)/1000000+"] pending=["+ pending +"]");


            }

        }

        System.out.println(SIMPLE_DATE_FORMAT.format( new Date(System.currentTimeMillis()) ) + " - Done=["+futures.stream().filter(Future::isDone).count()+"] Cancelled=["+futures.stream().filter(Future::isCancelled).count()+"]");
    }

    String getDeviceId(int month, int day, int deviceIndex) {
        return strategy.getDeviceId(month, day, deviceIndex);
    }

//    protected abstract Calendar getLastDay();
//
//    protected abstract Calendar getFirstDay();
//
//    /**
//     * We iterate the  calendar and this is where  we decide if  we jump 1 day every iteration or two or a week...
//     */
//    protected abstract void incrementCalendar(Calendar cal);
//
//    protected abstract boolean shouldTruncateTableBeforeStart();
//
//    /**
//     * Which year to add rows to
//     * This class is all about inserting data for a single year
//     */
//    protected abstract int getYear();
//
//    /**
//     * How many devices reporting in the given day
//     */
//    protected abstract int getDeviceCountPerDay(Calendar cal);
//
//    /**
//     * How many transaction per device in the given day.
//     * Value should be between 1 and 24 (including both)
//     * (the table holds one row per hour)
//     */
//    protected abstract int getTransactionCountPerDevice(Calendar cal);

    private Insert createInsertQuery() {
        final Insert insert = QueryBuilder.insertInto(CassandraShared.KEYSPACE, CassandraShared.DAILY_AGGREGATE_TABLE);
        insert.value("user_id", "user_id");
        insert.value("project_id", "project_id");
        insert.value("environment", "environment");
        insert.value("year", strategy.getYear());
        insert.value("counter", 10);
        insert.value("data_points", 20);
        insert.value("volume_size", 30);
        return insert;
    }
}
