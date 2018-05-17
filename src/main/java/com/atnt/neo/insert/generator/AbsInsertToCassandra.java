package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.StrategyInsert;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Insert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public abstract class AbsInsertToCassandra {
    @SuppressWarnings("SpellCheckingInspection")
    private static final SimpleDateFormat DF_DATE = new SimpleDateFormat("YYYY-MM-dd");
    @SuppressWarnings("SpellCheckingInspection")
    private static final SimpleDateFormat DF_LOG  = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss,sss");
    private final StrategyInsert strategy;

    protected AbsInsertToCassandra(StrategyInsert strategyInsert) {
        this.strategy = strategyInsert;
    }

    private static String logTimestamp() {
        return DF_LOG.format(System.currentTimeMillis());
    }

    public void insert() throws InterruptedException {
        List<ResultSetFuture> futures = new ArrayList<>();

        try (Cluster cluster = CassandraShared.initCluster()) {
            System.out.println(logTimestamp() + " Working on ["+CassandraShared.KEYSPACE+"] ["+ getStrategy().getTableName()+"]");
            Session session = cluster.connect(CassandraShared.KEYSPACE);

            if (getStrategy().shouldTruncateTableBeforeStart()) {
                session.execute("truncate table "+ getStrategy().getTableName()+";");
            }

            final Calendar cal = getStrategy().getFirstDay();
            final Calendar lastDay = getStrategy().getLastDay();
            while ( cal.compareTo(lastDay) <= 0 ) {

                final ArrayList<Insert> statements  = new ArrayList<>(CassandraShared.MAX_BATCH_SIZE);
                final long              begin       = System.nanoTime();
                final int               year        = cal.get(Calendar.YEAR        );
                final int               month       = cal.get(Calendar.MONTH) + 1;
                final int               day         = cal.get(Calendar.DAY_OF_MONTH);
                final int               deviceCount = getStrategy().getDeviceCountPerDay(cal);
                final AtomicLong        doneCount   = new AtomicLong();

                for (Integer hour : getStrategy().getHoursArray()) {
                    System.out.println(logTimestamp() + " Inserting ["+deviceCount+"] devices to day ["+DF_DATE.format(cal.getTime())+"] hour ["+hour+"]...");

                    for (int deviceIndex=0 ; deviceIndex<deviceCount ; deviceIndex++) {

                        final Iterable<Insert> queries = createInsertQueries(deviceIndex, year , month, day, hour);

                        for (Insert query : queries) {
                            statements.add(query);
                            if (statements.size() > CassandraShared.MAX_BATCH_SIZE) {
                                executeBatchAsync(futures, session, statements, doneCount);
                            }
                            checkFailures(futures);
                            //avoid overwhelming Cassandra
                            long pending = futures.stream().filter(f -> !f.isDone()).count();
                            while (pending > CassandraShared.MAX_PARALLELISM_CASSANDRA) {
                                Thread.sleep(500);
                                pending = futures.stream().filter(f -> !f.isDone()).count();
                            }
                        }
                    }
                }

                //commit the last batch which is probably partially full
                if (statements.size() > 0) {
                    executeBatchAsync(futures, session, statements, doneCount);
                }

                checkFailures(futures);

                futures.removeIf(Future::isDone);
                final long pending = futures.stream().filter(f -> !f.isDone()).count();
                final long end = System.nanoTime();
                System.out.println(logTimestamp() + " - date=["+day+"/"+month+"] deviceCount=["+deviceCount+"] time=["+(end-begin)/1000000+"] pending=["+ pending +"]");
                getStrategy().incrementCalendar(cal);
            }

        }

        System.out.println(logTimestamp() + " - Done=["+futures.stream().filter(Future::isDone).count()+"] Cancelled=["+futures.stream().filter(Future::isCancelled).count()+"]");
    }

    private void checkFailures(List<ResultSetFuture> futures) throws InterruptedException {
        for (ResultSetFuture future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                throw new RuntimeException("Failed to execute query ", e);
            }
        }
    }

    private static  void executeBatchAsync(List<ResultSetFuture> futures, Session session, ArrayList<Insert> statements, AtomicLong doneCount) {
        statements.forEach( q -> futures.add(session.executeAsync(q)) );
        final long pending  = futures.stream().filter(f -> !f.isDone()).count();
        final long canceled = futures.stream().filter(Future::isCancelled).count         ();
        final long failed   = futures.stream().filter(AbsInsertToCassandra::failed).count();
        final List<ResultSetFuture> doneList = futures.stream().filter(Future::isDone).collect(Collectors.toList());
        final long done     = doneCount.addAndGet(doneList.size());
        futures.removeAll(doneList);
        System.out.printf("\texecuteBatchAsync: pending=[%d] canceled=[%d] failed=[%d] done=[%d] \n", pending, canceled, failed, done);
        statements.clear();
    }

    private static boolean failed(ResultSetFuture resultSetFuture) {
        try {
            resultSetFuture.get(1, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    protected abstract Iterable<Insert> createInsertQueries(int deviceIndex, int year, int month, int day, int hour);

    protected Date getTimestamp(Calendar cal, int month, int day, int hour, Integer minute, Integer second) {
        //noinspection MagicConstant
        cal.set(getStrategy().getYear(), month-1, day, hour, minute, second);
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        return cal.getTime();
    }

    protected void appendInsertContextFields(Insert insert) {
        insert.value("org_bucket", "org_bucket");
        insert.value("project_bucket", "project_bucket");
        insert.value("org_id", "org_id");
        insert.value("project_id", "project_id");
        insert.value("environment", "environment");
    }

    protected void appendInsertTimeFields(Insert insert, int year, int month, int day, int hour, Calendar cal, Integer minute, Integer second) {
        insert.value("timestamp", getTimestamp(cal, month, day, hour, minute, second));
        insert.value("year", year);
        insert.value("month", month);
        insert.value("day", day);
        insert.value("hour", hour);
        insert.value("minutes", minute);
        insert.value("seconds", second);
    }

    protected void appendInsertDeviceInfo(int deviceId, int year, int month, int day, Insert insert) {
        insert.value("device_id", getStrategy().getDeviceId(year, month, day, deviceId));
        insert.value("device_type", "device_type");
        insert.value("device_firmware", "device_firmware");
    }

    protected void appendInsertUsageFields(int month, int day, int hour, Insert insert) {
        insert.value("transaction_id", "transaction_id");
        insert.value("data_points", getStrategy().getDataPoints(month, day, hour));
        insert.value("volume_size", getStrategy().getVolumeSize(month, day, hour));
        insert.value("billing_points", getStrategy().getBillingPoints(month, day, hour));
    }

    protected StrategyInsert getStrategy() {
        return strategy;
    }
}
