package com.atnt.neo.insert.strategy.streams;

import com.atnt.neo.insert.strategy.AbsStrategyInsert;

import java.util.HashMap;
import java.util.Map;

public abstract class AbStrategyInsertStreams extends AbsStrategyInsert {

    protected AbStrategyInsertStreams(String[] args) {
        super(args);
    }

    public abstract Map<String, Double> createDoubleStreamMap(int deviceIndex, int year, int month, int day, int hour);

    @SuppressWarnings("SameParameterValue")
    protected static Map<String, Double> generateDoubleStreamMap(int howManyStreams, int deviceIndex, int year, int month, int day) {
        final HashMap<String, Double> result = new HashMap<>();
        for (int i = 0; i < howManyStreams ; i++) {
            result.put("stream_"+i, (double) (year * 365 + month * 30 + day + deviceIndex + i));
        }
        return result;
    }

    public abstract Map<String, String> createGeoLocationStreamMap(int deviceIndex, int year, int month, int day, int hour);

    protected static Map<String, String> generateGeoStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        double offset = (year-1970) * 365 + month * 30 + day + hour + deviceIndex;        //(2018-1970)*2018 + 12*30 + 31 + 70000  == 167,255
        double longitude = -120.000+offset/1000;          // scale offset to get values ~between (-120) and(-77)
        double latitude = 32.00+offset/3000;              // scale offset to get values ~between 32.00 and 48.00

        final HashMap<String, String> result = new HashMap<>();
        result.put("location", String.format("%.6f|%.6f|null",longitude,latitude));

        return result;
    }

}
