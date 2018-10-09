package com.atnt.neo.insert.strategy.time;

import com.atnt.neo.insert.strategy.StrategyConfig;

import java.util.Calendar;

public class EveryDaySeveralDaysBack implements TimePeriod {
    private final StrategyConfig config;

    public EveryDaySeveralDaysBack(StrategyConfig config) {
        this.config = config;
    }

    @Override
    public Calendar getFirstDay() {
        final Calendar cal = (Calendar) getLastDay().clone();
        if (config.getDays()!=StrategyConfig.NOT_PROVIDED) {
            cal.add(Calendar.DAY_OF_YEAR, config.getDays()*-1);
        }
        else {
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }
        return cal;
    }

    @Override
    public Calendar getLastDay() {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        if (config.getYear()!=StrategyConfig.NOT_PROVIDED ) {
            cal.set(Calendar.YEAR, config.getYear());
        }

        if (config.getMonth()!=StrategyConfig.NOT_PROVIDED) {
            cal.set(Calendar.MONTH, config.getMonth());
        }
        return cal;
    }

    @Override
    public void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, 1);
    }
}
