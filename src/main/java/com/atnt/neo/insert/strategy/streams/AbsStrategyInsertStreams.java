package com.atnt.neo.insert.strategy.streams;

import com.atnt.neo.insert.strategy.AbsStrategyInsert;
import lombok.Data;

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
            result.put("stream_d_"+i, (double) (month * 30 + day + i)*.075*ThreadLocalRandom.current().nextInt(0,100));
        }
        return result;
    }

    public Map<String, String> createStringStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return Collections.emptyMap();
    }


    protected static Map<String, String> generateStringStreamMap(int howManyStreams, int deviceIndex, int year, int month, int day, int hour) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < howManyStreams; i++) {
            result.put("stream_s_" + i, String.format("%d_%d.%d.%d_%d_%d", deviceIndex, year, month, day, hour, i));
        }
        return result;
    }


    @SuppressWarnings("unused")
    public Map<String, GeoLocation> createGeoStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return Collections.emptyMap();
    }

    @SuppressWarnings({"SameParameterValue", "unused"})
    protected static Map<String, GeoLocation> generateGeoStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        double offset = (year-1970) * 365 + month * 30 + day + hour + deviceIndex;        //(2018-1970)*2018 + 12*30 + 31 + 70000  == 167,255
        double longitude = -120.000+offset/1000;          // scale offset to get values ~between (-120) and(-77)
        double latitude = 32.00+offset/3000;              // scale offset to get values ~between 32.00 and 48.00

        final HashMap<String, GeoLocation> result = new HashMap<>();
        result.put("location", new GeoLocation(longitude, latitude, Double.NaN));

        return result;
    }

    public Map<String, Boolean> createBooleanStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return Collections.emptyMap();
    }

    @SuppressWarnings("unused")
    protected static Map<String, Boolean> generateBooleanStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return Collections.singletonMap("stream_b_0", hour%2==0);
    }


//    private double getNextRandomStreamValue() {
//        final double range = getConfig().getStreamUpperBound() - getConfig().getStreamLowerBound();
//        final double rand = ThreadLocalRandom.current().nextDouble();
//        if (isOutlier(rand)) {
//            return Math.round((rand * range) + getConfig().getStreamUpperBound());
//        }
//        else {
//            return Math.round((rand * range) + getConfig().getStreamLowerBound());
//        }
//    }
//
//    private boolean isOutlier(double rand) {
//        return rand*100 >= 100-getConfig().getStreamOutlierPercent();   //if we want 2% outliers then the config value is 2 so only if rand>=0.98   (ie rand*100>=98)
//    }

    @Data
    public static class GeoLocation {
        final double longitude;
        final double latitude;
        final double elevation;

        private GeoLocation(double longitude, double latitude, double elevation) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.elevation = elevation;
        }
    }
}
