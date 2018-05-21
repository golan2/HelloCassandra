package com.atnt.neo.insert.strategy.usage;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertToCountersTable;
import com.atnt.neo.insert.strategy.AbsStrategyInsertAggregated;

import java.util.Calendar;
import java.util.Collections;
import java.util.Set;


//1964-1965
public class StrategyInsertDailyAggregated1964_65 extends AbsStrategyInsertAggregated {

    public static void main(String[] args) throws InterruptedException {
        new InsertToCountersTable(new StrategyInsertDailyAggregated1964_65()).insert();
    }


    @Override
    public String getTableName() {
        return CassandraShared.RAW_DATA_TABLE;
    }

    @Override
    public int getYear() {
        return 1965;
    }

    @Override
    public Calendar getFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear()-1, Calendar.DECEMBER, 15);
        return cal;
    }

    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return String.format("device_%4d_%2d_%d", year, month, deviceIndex);
    }

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.MARCH, 15);
        return cal;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return 5;
    }

    @Override
    public Set<Integer> getHoursArray() {
        return Collections.singleton(1);    //one hour a day
    }

}
