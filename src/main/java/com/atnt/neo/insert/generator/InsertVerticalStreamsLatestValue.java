package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.streams.vertical.AbsStrategyInsertVerticalStreams;
import com.datastax.driver.core.querybuilder.Insert;

import java.util.Calendar;

public class InsertVerticalStreamsLatestValue extends AbsInsertVerticalStreams {
    public  InsertVerticalStreamsLatestValue(AbsStrategyInsertVerticalStreams strategyInsert) {
        super(strategyInsert);
    }

    @Override
    protected void appendInsertTimeFields(Insert insert, int year, int month, int day, int hour, Calendar cal, Integer minute, Integer second) {

    }
}
