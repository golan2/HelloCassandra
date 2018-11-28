package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.StrategyInsert;
import com.atnt.neo.insert.strategy.StrategyUtil;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.UserType;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public abstract class AbsInsertToCassandra {
    private final static Logger logger = LoggerFactory.getLogger(AbsInsertToCassandra.class);

    @SuppressWarnings("SpellCheckingInspection")
    private static final SimpleDateFormat DF_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private final StrategyInsert strategy;

    protected AbsInsertToCassandra(StrategyInsert strategyInsert) {
        this.strategy = strategyInsert;
    }

    public void insert() throws InterruptedException {
        List<FutureWrapper> futures = new ArrayList<>();

        try (Cluster cluster = CassandraShared.initCluster(getStrategy().getConfig().getHosts(), getStrategy().getConfig().getKeyspace())) {
            logger.info("Working on [{}] [{}]", getStrategy().getConfig().getKeyspace(), getStrategy().getTableName());
            Session session = cluster.connect(getStrategy().getConfig().getKeyspace());

            if (getStrategy().shouldTruncateTableBeforeStart()) {
                session.execute("truncate table "+ getStrategy().getTableName()+";");
            }

            final Calendar cal = getStrategy().getTimePeriod().getFirstDay();
            final Calendar lastDay = getStrategy().getTimePeriod().getLastDay();

            logger.info("Insert date for days: {} - {}", DF_DATE.format(cal.getTime()), DF_DATE.format(lastDay.getTime()));

            while ( cal.compareTo(lastDay) <= 0 ) {

                final ArrayList<Insert> statements  = new ArrayList<>(CassandraShared.MAX_BATCH_SIZE);
                final long              begin       = System.nanoTime();
                final int               year        = cal.get(Calendar.YEAR);
                final int               month       = cal.get(Calendar.MONTH) + 1;
                final int               day         = cal.get(Calendar.DAY_OF_MONTH);
                final int               deviceCount = getStrategy().getDeviceCountPerDay(cal);
                final AtomicLong        doneCount   = new AtomicLong();

                logger.info("Inserting data for day [{}]...", DF_DATE.format(cal.getTime()));

                for (Integer hour : getStrategy().getTxnPerDay().getHoursArray()) {

                    int records = 0;

                    for (int deviceIndex=0 ; deviceIndex<deviceCount ; deviceIndex++) {
                        final String txnId = UUID.randomUUID().toString();
                        //todo: adapt bulk insert to fit streams raw data with partition less than an hour  (return List<List<Insert>> or something)
                        final Iterable<Insert> queries = createInsertQueries(txnId, deviceIndex, year , month, day, hour);

                        for (Insert query : queries) {
                            records++;
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

                    logger.debug("Inserted [{}] devices ({} records) to day [{}] {}", deviceCount, records, DF_DATE.format(cal.getTime()), (hour!= StrategyUtil.NO_VALUE?"hour ["+hour+"]":""));
                }

                //commit the last batch which is probably partially full
                if (statements.size() > 0) {
                    executeBatchAsync(futures, session, statements, doneCount);
                }

                checkFailures(futures);

                futures.removeIf(FutureWrapper::isDone);
                final long pending = futures.stream().filter(f -> !f.isDone()).count();
                final long end = System.nanoTime();
                logger.info("date=["+day+"/"+month+"] deviceCount=["+deviceCount+"] time=["+(end-begin)/1000000+"] pending=["+ pending +"]");
                getStrategy().getTimePeriod().incrementCalendar(cal);
            }

        }

        logger.info("Done=[{}] Cancelled=[{}]", futures.stream().filter(FutureWrapper::isDone).count(), futures.stream().filter(FutureWrapper::isCancelled).count());
    }

    private static void checkFailures(List<FutureWrapper> futures) throws InterruptedException {
        for (FutureWrapper future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {

                throw new RuntimeException("Failed to execute query: "+ future.getQuery(), e);
            }
        }
    }

    private static void executeBatchAsync(List<FutureWrapper> futures, Session session, ArrayList<Insert> statements, AtomicLong doneCount) {
        statements.forEach( q -> futures.add(new FutureWrapper(session.executeAsync(q), q.getQueryString())) );
        final long pending  = futures.stream().filter(f -> !f.isDone()).count();
        final long canceled = futures.stream().filter(FutureWrapper::isCancelled).count();
        final long failed   = futures.stream().filter(AbsInsertToCassandra::failed).count();
        final List<FutureWrapper> doneList = futures.stream().filter(FutureWrapper::isDone).collect(Collectors.toList());
        final long done     = doneCount.addAndGet(doneList.size());
        futures.removeAll(doneList);
        logger.debug("executeBatchAsync: pending=[{}] canceled=[{}] failed=[{}] done=[{}]", pending, canceled, failed, done);
        statements.clear();
    }

    private static boolean failed(FutureWrapper f) {
        try {
            f.getFuture().get(1, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    Iterable<Insert> createInsertQueries(String txnId, int deviceIndex, int year, int month, int day, int hour){
        final Calendar          cal     = Calendar.getInstance();
        final Set<Integer>      minutes = getStrategy().getTxnPerDay().getMinutesArray();
        final Set<Integer>      seconds = getStrategy().getTxnPerDay().getSecondsArray();
        final ArrayList<Insert> result  = new ArrayList<>(minutes.size() * seconds.size());

        for (Integer minute : minutes) {
            for (Integer second : seconds) {
                final Insert insert = QueryBuilder.insertInto(getStrategy().getConfig().getKeyspace(), getStrategy().getTableName());

                appendInsertContextFields(insert, year, month, day, hour, minute, deviceIndex);

                appendInsertTimeFields(insert, year, month, day, hour, cal, minute, second);

                appendInsertDeviceInfo(insert, deviceIndex, year, month, day);

                appendAdditionalFields(txnId, insert, year, month, day, hour, minute, second, deviceIndex);

                result.add(insert);
            }
        }
        return result;
    }


    @SuppressWarnings("WeakerAccess")
    protected void appendInsertContextFields(Insert insert, int year, int month, int day, int hour, int minute, int deviceIndex) {
        insert.value(CassandraShared.F_ORG_BUCKET, getStrategy().getOrgBucket());
        insert.value(CassandraShared.F_PROJECT_BUCKET, getStrategy().getProjectBucket());
        insert.value(CassandraShared.F_ORG_ID, getStrategy().getOrgId(year, month, day, hour, minute, deviceIndex));
        insert.value(CassandraShared.F_PROJECT_ID, getStrategy().getProjectId());
        insert.value(CassandraShared.F_ENVIRONMENT, getStrategy().getEnvironment());
    }

    protected abstract void appendInsertTimeFields(Insert insert, int year, int month, int day, int hour, Calendar cal, Integer minute, Integer second);

    @SuppressWarnings("WeakerAccess")
    protected Date getTimestamp(Calendar c, int month, int day, int hour, int minute, int second) {
        Calendar cal = (Calendar) c.clone();
        //noinspection MagicConstant
        cal.set(getStrategy().getYear(), month-1, day, hour, minute, second);
        cal.set(Calendar.MILLISECOND, 0);
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        return cal.getTime();
    }

    void appendInsertDeviceInfo(Insert insert, int deviceIndex, int year, int month, int day) {
        insert.value("device_id", getStrategy().getDeviceId(year, month, day, deviceIndex));
        insert.value("device_type", getStrategy().getDeviceType(year, month, day, deviceIndex));
    }

    protected abstract void appendAdditionalFields(String txnId, Insert insert, int year, int month, int day, int hour, int minute, int second, int deviceIndex);


    protected StrategyInsert getStrategy() {
        return strategy;
    }

}
