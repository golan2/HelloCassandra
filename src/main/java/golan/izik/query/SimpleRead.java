package golan.izik.query;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class SimpleRead {

    public static void main(String[] args) {

        final long before = System.nanoTime();

        final ResultSet resultSet = CassandraConnection.getSession().execute("SELECT * FROM project_dashbords;");

        final long middle = System.nanoTime();
        while (resultSet.iterator().hasNext()) {
            Row row = resultSet.iterator().next();
            System.out.printf("%s, %s, %s \n", row.getString(0), row.getString(1), row.getString(2));
        }

        final long after = System.nanoTime();


        System.out.println("executeTime=["+((middle-before)/1_000_000_000)+"] fetchTime=["+((after-middle)/1_000_000_000)+"] ");


        CassandraConnection.close();
    }
}
