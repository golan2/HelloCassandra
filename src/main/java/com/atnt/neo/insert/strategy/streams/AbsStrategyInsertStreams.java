package com.atnt.neo.insert.strategy.streams;

import com.atnt.neo.insert.strategy.AbsStrategyInsert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbsStrategyInsertStreams extends AbsStrategyInsert {

    protected AbsStrategyInsertStreams(String[] args) {
        super(args);
    }

    public Map<String, Double> createDoubleStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return Collections.emptyMap();
    }

    @SuppressWarnings({"SameParameterValue", "unused"})
    protected static Map<String, Double> generateDoubleStreamMap(int howManyStreams, int deviceIndex, int year, int month, int day) {
        final HashMap<String, Double> result = new HashMap<>();
        for (int i = 0; i < howManyStreams ; i++) {
            if (deviceIndex%10==0) {
                result.put("stream_" + i, null);
            }
            else {
                result.put("stream_"+i, (double) (month * 30 + day + i));
            }
        }
        return result;
    }

    public Map<String, String> createGeoLocationStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return Collections.emptyMap();
    }

    protected static Map<String, String> generateGeoStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        double offset = (year-1970) * 365 + month * 30 + day + hour + deviceIndex;        //(2018-1970)*2018 + 12*30 + 31 + 70000  == 167,255
        double longitude = -120.000+offset/1000;          // scale offset to get values ~between (-120) and(-77)
        double latitude = 32.00+offset/3000;              // scale offset to get values ~between 32.00 and 48.00

        final HashMap<String, String> result = new HashMap<>();
        result.put("location", String.format("%.6f|%.6f|null",longitude,latitude));

        return result;
    }

    public Map<String, Double> createRandomStreamMap() {
        return Collections.emptyMap();
    }

    protected final Map<String, Double> generateRandomStreamMap() {
        final HashMap<String, Double> result = new HashMap<>();
        result.put("stream_0", getNextRandomStreamValue());
        return result;
    }

    private double getNextRandomStreamValue() {
        final double range = getConfig().getStreamUpperBound() - getConfig().getStreamLowerBound();
        final double rand = ThreadLocalRandom.current().nextDouble();
        if (isOutlier(rand)) {
            return Math.round((rand * range) + getConfig().getStreamUpperBound());
        }
        else {
            return Math.round((rand * range) + getConfig().getStreamLowerBound());
        }

    }

    private boolean isOutlier(double rand) {
        return rand*100 >= 100-getConfig().getStreamOutlierPercent();   //if we want 2% outliers then the config value is 2 so only if rand>=0.98   (ie rand*100>=98)
    }



}
