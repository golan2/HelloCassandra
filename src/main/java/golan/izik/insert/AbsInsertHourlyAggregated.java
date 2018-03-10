package golan.izik.insert;

public abstract class AbsInsertHourlyAggregated extends AbsInsertAggregated {

    @Override
    public String getTableName() {
        return "hourly_aggregator";
    }

    @Override
    public boolean isHourExist() {
        return true;
    }
}
