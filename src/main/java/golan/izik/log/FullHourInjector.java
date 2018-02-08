package golan.izik.log;

import com.datastax.driver.core.Session;

import java.util.Calendar;
import java.util.Set;

/**
 * Inserts full hour data for a single device (device_id)
 * Full hour data ==> one data point per second (i.e. 3600 data points)
 */
public class FullHourInjector extends AbsHourInjector {

    FullHourInjector(Session session, String deviceId, Calendar c) {
        super(session, deviceId, c);
    }


    @Override
    protected Set<Integer> getMinutesArray(int hour) {
        return generateFullRange();
    }

    @Override
    protected Set<Integer> getSecondsArray(int minutes) {
        return generateFullRange();
    }
}
