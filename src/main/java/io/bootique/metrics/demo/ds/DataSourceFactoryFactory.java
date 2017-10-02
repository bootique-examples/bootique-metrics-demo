package io.bootique.metrics.demo.ds;

import com.codahale.metrics.MetricRegistry;
import io.bootique.jdbc.LazyDataSourceFactoryFactory;
import io.bootique.jdbc.TomcatDataSourceFactory;
import io.bootique.log.BootLogger;
import io.bootique.shutdown.ShutdownManager;

import java.util.Map;

public class DataSourceFactoryFactory {

    private Map<String, TomcatDataSourceFactory> configs;

    public DataSourceFactoryFactory(Map<String, TomcatDataSourceFactory> configs) {
        this.configs = configs;
    }

    public DataSourceFactory create(
            ShutdownManager shutdownManager,
            BootLogger bootLogger,
            MetricRegistry metricRegistry) {

        DataSourceFactory factory = new DataSourceFactory(
                LazyDataSourceFactoryFactory.prunePartialConfigs(configs),
                metricRegistry);

        shutdownManager.addShutdownHook(() -> {
            bootLogger.trace(() -> "shutting down InstrumentedLazyDataSourceFactory...");
            factory.shutdown();
        });

        return factory;
    }
}
