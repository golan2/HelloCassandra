package com.atnt.neo.insert.strategy.counters.clazz;

import com.atnt.neo.insert.generator.InsertToCountersClassMinuteAggregatedTable;
import com.atnt.neo.insert.strategy.StrategyConfig;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralDaysEndOfYear;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

public class StrategyInsertClassMinuteAggregated1945 extends AbsStrategyInsertClassMinuteAggregated {
    private StrategyInsertClassMinuteAggregated1945(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertToCountersClassMinuteAggregatedTable(new StrategyInsertClassMinuteAggregated1945(args)).insert();
    }

    @Override
    protected int getDefaultYear() {
        return 1945;
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
            return 6;
        }
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
    }
}
