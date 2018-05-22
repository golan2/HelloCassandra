package com.atnt.neo.insert.strategy.raw.data;

import com.atnt.neo.insert.strategy.AbsStrategyInsert;

public abstract class AbsStrategyInsertRawData extends AbsStrategyInsert {

    @SuppressWarnings("unused")
    public int getPartSelector(int year, int month, int day, int hour, Integer minute, Integer second) { return -1;}

    @Override
    public boolean includeTimeStamp() {
        return true;
    }

    @Override
    public boolean includeTxnId() {
        return true;
    }
}
