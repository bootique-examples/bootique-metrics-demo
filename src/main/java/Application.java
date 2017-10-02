import com.codahale.metrics.MetricRegistry;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.bootique.BQCoreModule;
import io.bootique.Bootique;
import io.bootique.config.ConfigurationFactory;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.jdbc.JdbcModule;
import io.bootique.jdbc.TomcatDataSourceFactory;
import io.bootique.log.BootLogger;
import io.bootique.metrics.MetricsModule;
import io.bootique.metrics.demo.DataSourceHealthCheckCommand;
import io.bootique.metrics.demo.DataSourceHealthCheckGroup;
import io.bootique.metrics.demo.ds.DataSourceFactoryFactory;
import io.bootique.shutdown.ShutdownManager;
import io.bootique.type.TypeRef;

import java.util.Map;

public class Application implements Module {

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
        BQCoreModule.extend(binder).setDefaultCommand(DataSourceHealthCheckCommand.class);

        MetricsModule.extend(binder).addHealthCheckGroup(DataSourceHealthCheckGroup.class);
    }

    @Singleton
    @Provides
    DataSourceHealthCheckGroup provideDataSourceHealthCheckGroup(DataSourceFactory dataSourceFactory) {
        return new DataSourceHealthCheckGroup(dataSourceFactory);
    }

    @Singleton
    @Provides
    public DataSourceFactory createDataSourceFactory(ConfigurationFactory configFactory,
                                                     BootLogger bootLogger,
                                                     MetricRegistry metricRegistry,
                                                     ShutdownManager shutdownManager) {

        Map<String, TomcatDataSourceFactory> configs = configFactory
                .config(new TypeRef<Map<String, TomcatDataSourceFactory>>() {
                }, "jdbc");

        return new DataSourceFactoryFactory(configs).create(shutdownManager, bootLogger, metricRegistry);
    }
}
