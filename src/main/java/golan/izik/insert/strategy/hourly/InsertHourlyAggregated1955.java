package golan.izik.insert.strategy.hourly;

import golan.izik.insert.aggregated.InsertToAggregatedTable;

import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Insert data for the first 6 weeks in 1955
 */
public class InsertHourlyAggregated1955 extends AbsInsertHourlyAggregated {

    private static final int SUFFIX = ThreadLocalRandom.current().nextInt(0, 99999);

    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    private InsertHourlyAggregated1955(Boolean truncate, Integer devicesPerDay) {
        this.truncateTableBeforeStart = truncate;
        this.deviceCountPerDay = devicesPerDay;
    }

    public static void main(String[] args) throws InterruptedException {
        Boolean truncate;
        Integer devicesPerDay;
        try {
            truncate = Boolean.parseBoolean(args[0]);
            devicesPerDay = Integer.parseInt(args[1]);
        } catch (Exception e) {
            truncate = false;
            devicesPerDay = -1;
        }
        System.out.println("truncate=["+truncate+"] devicesPerDay=["+devicesPerDay+"] ");
        new InsertToAggregatedTable(new InsertHourlyAggregated1955(truncate, devicesPerDay)).insert();
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return this.truncateTableBeforeStart;
    }

    @Override
    public int getYear() {
        return 1955;
    }

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.FEBRUARY, 13);
        return cal;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return ( this.deviceCountPerDay==-1 ? 20_000 : this.deviceCountPerDay );
    }

    @Override
    public Set<Integer> getHoursArray() {
        return IntStream.range(0,24).boxed().collect(Collectors.toSet());   //[0,1,2.., 23]
    }

    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return "device_"+deviceIndex+"_"+ SUFFIX;
    }


}
