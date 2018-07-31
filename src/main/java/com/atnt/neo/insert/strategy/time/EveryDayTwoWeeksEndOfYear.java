package com.atnt.neo.insert.strategy.time;

@SuppressWarnings("unused")
public class EveryDayTwoWeeksEndOfYear extends EveryDaySeveralDaysEndOfYear {
    public EveryDayTwoWeeksEndOfYear(int year) {
        super(year, 14);
    }
}
