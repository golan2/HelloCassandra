package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.streams.AbsStrategyInsertStreams;
import com.atnt.neo.insert.strategy.streams.vertical.AbsStrategyInsertVerticalStreams;
import com.datastax.driver.core.querybuilder.Insert;

import java.util.Calendar;

import static com.atnt.neo.insert.generator.CassandraShared.F_VERTICAL_STREAM_TEXT;

public class InsertVerticalStreamsLatestValue extends AbsInsertVerticalStreams {
    public  InsertVerticalStreamsLatestValue(AbsStrategyInsertVerticalStreams strategyInsert) {
        super(strategyInsert);
    }

    @Override
    protected void appendInsertTimeFields(Insert insert, int year, int month, int day, int hour, Calendar cal, Integer minute, Integer second) { }

    @Override
    void insertGeoValue(Insert insert, AbsStrategyInsertStreams.GeoLocation geo) {
        insert.value(F_VERTICAL_STREAM_TEXT, String.format("%.5f|%.5f|%.5f", geo.getLongitude(), geo.getLatitude(), geo.getElevation()));
    }
}
