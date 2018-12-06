package io.bootique.metrics.demo;

import io.bootique.jdbc.DataSourceFactory;
import io.bootique.metrics.health.HealthCheck;
import io.bootique.metrics.health.HealthCheckOutcome;

import java.sql.Connection;

public class DataSourceHealthCheck implements HealthCheck {

    private final DataSourceFactory dataSourceFactory;
    private final String dataSourceName;

    public DataSourceHealthCheck(DataSourceFactory dataSourceFactory, String dataSourceName) {
        this.dataSourceFactory = dataSourceFactory;
        this.dataSourceName = dataSourceName;
    }

    @Override
    public HealthCheckOutcome check() throws Exception {
        try (Connection c = dataSourceFactory.forName(dataSourceName).getConnection()) {
            return c.isValid(1)
                    ? HealthCheckOutcome.ok()
                    : HealthCheckOutcome.critical("Connection validation failed");
        }
    }
}
