package io.bootique.metrics.demo;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.meta.application.CommandMetadata;
import io.bootique.metrics.health.HealthCheckOutcome;
import io.bootique.metrics.health.HealthCheckRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DataSourceHealthCheckCommand extends CommandWithMetadata {
    private Provider<HealthCheckRegistry> registry;

    private static Logger LOGGER = LoggerFactory.getLogger(DataSourceHealthCheckCommand.class);

    @Inject
    public DataSourceHealthCheckCommand(Provider<HealthCheckRegistry> registry) {
        super(CommandMetadata.builder(DataSourceHealthCheckCommand.class)
                .name("ds-check")
                .description("Checks data sources validity")
                .build());

        this.registry = registry;
    }

    @Override
    public CommandOutcome run(Cli cli) {
        for (Map.Entry<String, HealthCheckOutcome> entry : registry.get().runHealthChecks().entrySet()) {
            if (entry.getValue().isHealthy()) {
                LOGGER.info(entry.getKey() + ": OK");
            } else {
                LOGGER.error(entry.getKey() + ": FAIL");
            }
        }

        //Sleep to see a report from Slf4jReporter.
        //Only for demo purposes!
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return CommandOutcome.succeeded();
    }
}
