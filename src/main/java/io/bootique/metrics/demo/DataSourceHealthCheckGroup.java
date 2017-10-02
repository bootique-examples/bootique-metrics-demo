package io.bootique.metrics.demo;

import io.bootique.jdbc.DataSourceFactory;
import io.bootique.metrics.health.HealthCheck;
import io.bootique.metrics.health.HealthCheckGroup;

import java.util.HashMap;
import java.util.Map;

public class DataSourceHealthCheckGroup implements HealthCheckGroup {

    private final DataSourceFactory dataSourceFactory;

    public DataSourceHealthCheckGroup(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    @Override
    public Map<String, HealthCheck> getHealthChecks() {
        Map<String, HealthCheck> checks = new HashMap<>();
        dataSourceFactory.allNames().forEach(n -> checks.put(n, new DataSourceHealthCheck(dataSourceFactory, n)));

        return checks;
    }
}
