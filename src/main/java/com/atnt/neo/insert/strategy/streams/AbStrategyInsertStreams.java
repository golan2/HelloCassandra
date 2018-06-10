package com.atnt.neo.insert.strategy.streams;

import com.atnt.neo.insert.strategy.AbsStrategyInsert;

import java.util.HashMap;
import java.util.Map;

public abstract class AbStrategyInsertStreams extends AbsStrategyInsert {

    public Map<String, Double> createDoubleStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        final HashMap<String, Double> result = new HashMap<>();
        for (int i = 0; i < 80 ; i++) {
            result.put("stream_"+i, (double) (year * 365 + month * 30 + day + deviceIndex + i));
        }
        return result;
    }

}
