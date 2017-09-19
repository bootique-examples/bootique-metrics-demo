package io.bootique.metrics.demo.ds;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import io.bootique.jdbc.LazyDataSourceFactory;
import io.bootique.jdbc.TomcatDataSourceFactory;
import org.apache.tomcat.jdbc.pool.ConnectionPool;
import org.apache.tomcat.jdbc.pool.DataSource;

import java.util.Map;

public class DataSourceFactory extends LazyDataSourceFactory {

    private MetricRegistry metricRegistry;

    public DataSourceFactory(Map<String, TomcatDataSourceFactory> configs,
                             MetricRegistry metricRegistry) {
        super(configs);
        this.metricRegistry = metricRegistry;
    }

    @Override
    protected DataSource createDataSource(String name) {
        DataSource dataSource = super.createDataSource(name);
        collectPoolMetrics(name, dataSource.getPool());
        return dataSource;
    }

    /**
     * Registers {@link Gauge} metrics returning {@code pool} properties.
     * <p>
     * NOTE: If you need a data source with metrics then plug in the module {@code bootique-jdbc-instrumented}
     * into your app. This demo applies metrics in the same way as the module.
     *
     * @param name data source name
     * @param pool connection pool
     * @see <a href="https://github.com/bootique/bootique-jdbc/tree/master/bootique-jdbc-instrumented">bootique-jdbc-instrumented</a>
     */
    void collectPoolMetrics(String name, ConnectionPool pool) {
        metricRegistry.register(MetricRegistry.name(getClass(), name, "active"), (Gauge<Integer>) () -> pool.getActive());

        metricRegistry.register(MetricRegistry.name(getClass(), name, "idle"), (Gauge<Integer>) () -> pool.getIdle());

        metricRegistry.register(MetricRegistry.name(getClass(), name, "waiting"), (Gauge<Integer>) () -> pool.getWaitCount());

        metricRegistry.register(MetricRegistry.name(getClass(), name, "size"), (Gauge<Integer>) () -> pool.getSize());
    }
}
