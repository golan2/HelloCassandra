package com.atnt.neo.insert.strategy;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StrategyUtil {
    public static final int NO_VALUE = -1;

    public static Set<Integer> generate24hours() {
        return IntStream.range(0,24).boxed().collect(Collectors.toSet());
    }

    public static Set<Integer> singleValue() {
        return Collections.singleton(0);
    }

    public static Set<Integer> generateXhours(int x) {
        return generateXminutes(x);
    }

    private static Set<Integer> generateXminutes(int x) {
        return IntStream.range(0,x).boxed().collect(Collectors.toSet());       // //[0,1,2]
    }

    public static  Set<Integer> generateEveryTwoMinutes() {
        return IntStream.range(0,60).filter(x->x%2==0).boxed().collect(Collectors.toSet());     //every 2 minutes
    }

    public static Set<Integer> generateEveryMinute() {
        return IntStream.range(0,60).boxed().collect(Collectors.toSet());
    }

    public static Set<Integer> generateEverySecond() {
        return generateEveryMinute();
    }

    public static Set<Integer> generateNotApplicable() {
        return Collections.singleton(NO_VALUE);
    }
}
