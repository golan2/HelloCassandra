package com.atnt.neo.insert.generator;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;

import java.util.concurrent.ExecutionException;

public class FutureWrapper {
    private final ResultSetFuture future;
    private final String query;

    FutureWrapper(ResultSetFuture future, String query) {
        this.future = future;
        this.query = query;
    }


    public boolean isDone() {
        return future.isDone();
    }

    public boolean isCancelled() {
        return future.isCancelled();
    }

    public ResultSet get() throws ExecutionException, InterruptedException {
        return future.get();
    }

    public ResultSetFuture getFuture() {
        return future;
    }

    public String getQuery() {
        return query;
    }
}
