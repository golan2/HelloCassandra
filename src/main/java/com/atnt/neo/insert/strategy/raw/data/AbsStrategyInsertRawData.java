package com.atnt.neo.insert.strategy.raw.data;

import com.atnt.neo.insert.strategy.AbsStrategyInsert;

import java.util.Set;

public abstract class AbsStrategyInsertRawData extends AbsStrategyInsert {
    @Override
    public boolean isHourExist() {
        return true;
    }

    public int getPartSelector(int year, int month, int day, int hour, Integer minute, Integer second) { return -1;}
}
