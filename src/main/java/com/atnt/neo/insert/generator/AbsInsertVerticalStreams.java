package com.atnt.neo.insert.generator;

import com.atnt.neo.insert.strategy.streams.AbsStrategyInsertStreams.GeoLocation;
import com.atnt.neo.insert.strategy.streams.vertical.AbsStrategyInsertVerticalStreams;
import com.datastax.driver.core.UDTValue;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbsInsertVerticalStreams extends AbsInsertToCassandra {
    AbsInsertVerticalStreams(AbsStrategyInsertVerticalStreams strategyInsert) {
        super(strategyInsert);
    }

    @SuppressWarnings("CollectionAddAllCanBeReplacedWithConstructor")
    @Override
    protected Iterable<Insert> createInsertQueries(String txnId, int deviceIndex, int year, int month, int day, int hour) {
        final Calendar     cal     = Calendar.getInstance();
        final Set<Integer> minutes = getStrategy().getTxnPerDay().getMinutesArray();
        final Set<Integer> seconds = getStrategy().getTxnPerDay().getSecondsArray();

        List<Insert> result = new ArrayList<>();

        final Map<String, Double> doubleStreamMap = getStrategy().createDoubleStreamMap(deviceIndex, year, month, day, hour);
        result.addAll(createInsertStreamsQuery(txnId, deviceIndex, year, month, day, hour, cal, minutes, seconds, doubleStreamMap));

        final Map<String, GeoLocation> geoStreamMap = getStrategy().createGeoStreamMap(deviceIndex, year, month, day, hour);
        result.addAll(createInsertStreamsQuery(txnId, deviceIndex, year, month, day, hour, cal, minutes, seconds, geoStreamMap));

        final Map<String, String> stringStreamMap = getStrategy().createStringStreamMap(deviceIndex, year, month, day, hour);
        result.addAll(createInsertStreamsQuery(txnId, deviceIndex, year, month, day, hour, cal, minutes, seconds, stringStreamMap));

        final Map<String, Boolean> booleanStreamMap = getStrategy().createBooleanStreamMap(deviceIndex, year, month, day, hour);
        result.addAll(createInsertStreamsQuery(txnId, deviceIndex, year, month, day, hour, cal, minutes, seconds, booleanStreamMap));

        return result;
    }


    private <T> ArrayList<Insert> createInsertStreamsQuery(String txnId, int deviceIndex, int year, int month, int day, int hour, Calendar cal, Set<Integer> minutes, Set<Integer> seconds, Map<String, T> streams) {
        final ArrayList<Insert> result  = new ArrayList<>(minutes.size() * seconds.size());
        for (Map.Entry<String, T> entry : streams.entrySet()) {
            for (Integer minute : minutes) {
                for (Integer second : seconds) {

                    final Insert insert = QueryBuilder.insertInto(getStrategy().getConfig().getKeyspace(), getStrategy().getTableName());

                    appendInsertContextFields(insert, year, month, day, hour, minute, deviceIndex);

                    appendInsertTimeFields(insert, year, month, day, hour, cal, minute, second);

                    appendInsertDeviceInfo(insert, deviceIndex, year, month, day);

                    appendInsertStreamFields(insert, entry.getKey(), entry.getValue());

                    appendAdditionalFields(txnId, insert, year, month, day, hour, minute, second, deviceIndex);

                    result.add(insert);

                    System.out.println(insert.toString());

                }
            }
        }
        return result;
    }

    private <T> void appendInsertStreamFields(Insert insert, String streamName, T streamValue) {
        insert.value(getStreamNameField(), streamName);

        if (streamValue==null) return;  //in Cassandra we don't insert null; we simply don't set value to this column

        if (streamValue instanceof Boolean) {
            insert.value(getBooleanStreamField(), streamValue);
        }
        else if (streamValue instanceof Double) {
            insert.value(getDoubleStreamField(), streamValue);
        }
        else if (streamValue instanceof String) {
            insert.value(getTextStreamField(), streamValue);
        }
        else if (streamValue instanceof GeoLocation) {
            final UDTValue geo = CassandraShared.createGeoPoint((GeoLocation) streamValue);
            insert.value(getGeoStreamField(), geo);
        }
    }

    private String getBooleanStreamField() {
        return CassandraShared.F_VERTICAL_BOOL_STREAM;
    }

    String getTextStreamField() {
        return CassandraShared.F_VERTICAL_STREAM_TEXT;
    }

    String getDoubleStreamField() {
        return CassandraShared.F_VERTICAL_STREAM_DOUBLE;
    }

    private String getGeoStreamField() {
        return CassandraShared.F_VERTICAL_GEO_STREAM;
    }

    String getStreamNameField() {
        return CassandraShared.F_VERTICAL_STREAM_NAME;
    }

    @Override
    protected abstract void appendInsertTimeFields(Insert insert, int year, int month, int day, int hour, Calendar cal, Integer minute, Integer second);

    @Override
    protected void appendAdditionalFields(String txnId, Insert insert, int year, int month, int day, int hour, int minute, int second, int deviceIndex) {}

    protected AbsStrategyInsertVerticalStreams getStrategy() {
        //noinspection unchecked
        return (AbsStrategyInsertVerticalStreams) super.getStrategy();
    }
}
