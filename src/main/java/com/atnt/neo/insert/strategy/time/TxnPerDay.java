package com.atnt.neo.insert.strategy.time;

import java.util.Set;

public interface TxnPerDay {

    Set<Integer> getHoursArray();

    Set<Integer> getMinutesArray();

    Set<Integer> getSecondsArray();

}
