package golan.izik.insert.raw.data;

import com.datastax.driver.core.Session;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;


/**
 * Inserts partial hour data for a single device (device_id)
 * Partial hour data ==> We insert "tpm" transaction per minute for all 60 minutes in hour
 */
public class PartialMinuteFullHourInjector extends AbsHourInjector {
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
