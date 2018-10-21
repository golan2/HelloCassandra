package com.atnt.neo.insert.strategy.time;

import java.util.Calendar;

public interface TimePeriod {

    Calendar getFirstDay();

    Calendar getLastDay();

    void incrementCalendar(Calendar cal);



}
