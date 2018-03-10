package golan.izik.insert.raw.data;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import golan.izik.CassandraShared;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

class InsertTonsOfData {

    public static void main(String[] args) throws InterruptedException {
        try (Cluster cluster = CassandraShared.initCluster()) {
            Session session = cluster.connect(CassandraShared.KEYSPACE);
            final ExecutorService tpe = Executors.newFixedThreadPool(4);


            Calendar cal = Calendar.getInstance();
            cal.set(2018, Calendar.JANUARY, 27, 12, 12, 1);



            for (int hour = 0 ; hour<1                                     /*32*24*/ ; hour++) {
                for (int i=0 ; i<250 ; i++) {
                    tpe.submit(new PartialMinuteFullHourInjector(session, "hr_"+i+"_"+hour, cal, ThreadLocalRandom.current().nextInt(1,60)));
                }
                cal.add(Calendar.HOUR_OF_DAY, -1);
            }

            tpe.shutdown();
            tpe.awaitTermination(30, TimeUnit.MINUTES);
        }
    }
}
