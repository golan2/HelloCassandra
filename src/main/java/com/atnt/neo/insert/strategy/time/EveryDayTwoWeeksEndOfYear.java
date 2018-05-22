package com.atnt.neo.insert.strategy.time;

public class EveryDayTwoWeeksEndOfYear extends EveryDaySeveralDaysEndOfYear {
    public EveryDayTwoWeeksEndOfYear(int year) {
        super(year, 14);
    }
}
