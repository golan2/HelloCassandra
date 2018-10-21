package com.atnt.neo.insert.strategy.counters.clazz;

import com.atnt.neo.insert.generator.InsertToCountersByClassTable;
import com.atnt.neo.insert.strategy.StrategyConfig;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralDaysEndOfYear;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

public class StrategyInsertCountersByClass1946 extends AbsStrategyInsertCountersByClass {
    private StrategyInsertCountersByClass1946(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertToCountersByClassTable(new StrategyInsertCountersByClass1946(args)).insert();
    }

    @Override
    protected int getDefaultYear() {
        return 1946;
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySeveralDaysEndOfYear(getYear(), getDays());
    }

    private int getDays() {
        final Integer days = getConfig().getDays();
        if (days != StrategyConfig.NOT_PROVIDED) {
            return days;
        }
        else {
            return 45;
        }
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
    }
}
