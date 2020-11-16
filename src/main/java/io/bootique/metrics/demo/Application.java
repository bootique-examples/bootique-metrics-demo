package io.bootique.metrics.demo;

import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.bootique.BQCoreModule;
import io.bootique.BaseModule;
import io.bootique.Bootique;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.jdbc.JdbcModule;
import io.bootique.metrics.MetricNaming;
import io.bootique.metrics.health.HealthCheckModule;

public class Application extends BaseModule {

    public static final MetricNaming METRIC_NAMING = MetricNaming.forModule(Application.class);

    public static void main(String[] args) {
        Bootique.app(args).autoLoadModules()
                //JdbcModule is overridden for demo purposes only!
                //NOTE: If you need a data source with metrics then plug in the module bootique-jdbc-instrumented
                //into your app. This demo shows metrics integration in the same way as the module.
                .override(JdbcModule.class).with(Application.class)
                .module(Application.class)
                .exec();
    }

    @Override
    public void configure(Binder binder) {
        BQCoreModule.extend(binder)
                .setDefaultCommand(DataSourceHealthCheckCommand.class);

        JdbcModule.extend(binder)
                .addDataSourceListener(TomcatMetricsInitializer.class);

        HealthCheckModule.extend(binder)
                .addHealthCheckGroup(DataSourceHealthCheckGroup.class);
    }

    @Singleton
    @Provides
    DataSourceHealthCheckGroup provideDataSourceHealthCheckGroup(DataSourceFactory dataSourceFactory) {
        return new DataSourceHealthCheckGroup(dataSourceFactory);
    }
}
