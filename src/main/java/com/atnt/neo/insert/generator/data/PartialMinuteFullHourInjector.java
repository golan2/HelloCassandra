package com.atnt.neo.insert.generator.data;

import com.datastax.driver.core.Session;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;


/**
 * Inserts partial hour data for a single device (device_id)
 * Partial Hour Full Minute =>
 *      We insert data for all 60 minutes in the given hour.
 *      For each minute we insert only partial info; only "tpm" transaction per minute (parameter)
 */
public class PartialMinuteFullHourInjector extends AbsSingleDeviceInjector {
    private final int tpm;  //transactions per minute

    PartialMinuteFullHourInjector(Session session, String deviceId, Calendar c, int tpm) {
        super(session, deviceId, c);
        this.tpm = tpm;
    }

    @Override
    protected Set<Integer> getMinutesArray() {
        return generateFullRange();
    }

    @Override
    protected Set<Integer> getSecondsArray() {
        final HashSet<Integer> result = new HashSet<>();
        for (int i = 0; i < tpm%60; i++) {      //%60 is to be on the safe side we should never get TPM higher than 60
            result.add(i);
        }
        return result;
    }
}