package golan.izik.query;

import com.atnt.neo.insert.generator.CassandraShared;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryMapColumn {

    public static void main(String[] args) {
        try (Cluster cluster = CassandraShared.createCluster("localhost")) {
            final Session session = cluster.connect("activity");
            final ResultSet resultSet = session.execute("SELECT * FROM object_event_by_object WHERE env_uuid=AAAAAAAA-AAAA-AAAA-0000-000000000001 and bucket_timestamp='2018-01-01 00:00:00.000' and class_id='cls_2' ALLOW FILTERING;");

            for (Row row : resultSet) {
                final Date timestamp = row.getTimestamp("timestamp");
                final Map<String, String> eventData = row.getMap("event_data", String.class, String.class);
                System.out.println(timestamp + " => " + eventData.entrySet().stream().map(e -> "{"+e.getKey()+"="+e.getValue()+"}")
                        .collect(Collectors.joining(", ")));
            }
            //bucket_timestamp, object_id, timestamp, event_id, event_uuid, event_data
        }
    }
}
