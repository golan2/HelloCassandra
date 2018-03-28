package golan.izik.insert;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Insert;
import golan.izik.CassandraShared;
import golan.izik.insert.strategy.InsertStrategy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class AbsInsertToCassandra {
    @SuppressWarnings("SpellCheckingInspection")
    private static final SimpleDateFormat DF_DATE = new SimpleDateFormat("YYYY-MM-dd");
    @SuppressWarnings("SpellCheckingInspection")
    private static final SimpleDateFormat DF_LOG  = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss,sss");
    private final InsertStrategy strategy;

    protected AbsInsertToCassandra(InsertStrategy insertStrategy) {
        this.strategy = insertStrategy;
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
                final int               year        = cal.get(Calendar.YEAR);
                final int               month       = cal.get(Calendar.MONTH) + 1;
                final int               day         = cal.get(Calendar.DAY_OF_MONTH);
                final int               deviceCount = getStrategy().getDeviceCountPerDay(cal);

                for (Integer hour : getStrategy().getHoursArray()) {
                    System.out.println(logTimestamp() + " Inserting ["+deviceCount+"] devices for day ["+DF_DATE.format(cal.getTime())+"] and hour ["+hour+"]...");
                    for (int deviceIndex=0 ; deviceIndex<deviceCount ; deviceIndex++) {
                        final Iterable<Insert> queries = createInsertQueries(deviceIndex, year , month, day, hour);

                        for (Insert query : queries) {
                            statements.add(query);
                            if (statements.size() > CassandraShared.MAX_BATCH_SIZE) {
                                executeBatchAsync(futures, session, statements);
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
                    executeBatchAsync(futures, session, statements);
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

    private static  void executeBatchAsync(List<ResultSetFuture> futures, Session session, ArrayList<Insert> statements) {
        statements.forEach( q -> futures.add(session.executeAsync(q)) );
        final long pending  = futures.stream().filter(f -> !f.isDone()).count();
        final long canceled = futures.stream().filter(Future::isCancelled).count         ();
        final long failed   = futures.stream().filter(AbsInsertToCassandra::failed).count();
        final long done     = futures.stream().filter(Future::isDone).count              ();
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

    protected InsertStrategy getStrategy() {
        return strategy;
    }
}