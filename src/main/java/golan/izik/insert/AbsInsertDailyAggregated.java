package golan.izik.insert;

public abstract class AbsInsertDailyAggregated extends AbsInsertAggregated {

    @Override
    public String getTableName() {
        return "daily_aggregator";
    }

    @Override
    public boolean isHourExist() {
        return false;
    }
}
