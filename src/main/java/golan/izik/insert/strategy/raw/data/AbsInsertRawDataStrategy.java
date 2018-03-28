package golan.izik.insert.strategy.raw.data;

import golan.izik.insert.strategy.AbsInsertStrategy;

import java.util.Calendar;
import java.util.Set;

public abstract class AbsInsertRawDataStrategy extends AbsInsertStrategy {
    @Override
    public Calendar getLastDay() {
        return null;
    }

    @Override
    public boolean isHourExist() {
        return true;
    }

    public abstract Set<Integer> getMinutesArray();

    public abstract Set<Integer> getSecondsArray();

}
