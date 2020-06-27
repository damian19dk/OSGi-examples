package com.generator;

import com.manager.api.definition.LoggingService;
import com.manager.api.model.EventType;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

public class LoggingGenerator implements BundleActivator {

    private BundleContext ctx;
    private ServiceReference<?> loggingServiceReference;
    private LoggingService loggingService;

    private ScheduledExecutorService loggingExecutor;

    private void generateLogs(int number) {
        for (int i = 0; i < number; i++) {
            loggingService.logEvent(EventType.RANDOM, UUID.randomUUID().toString());
        }
    }

    @Override
    public void start(BundleContext bundleContext) {
        this.ctx = bundleContext;
        this.loggingServiceReference = bundleContext.getServiceReference(LoggingService.class.getName());
        this.loggingService = (LoggingService) (ctx.getService(loggingServiceReference));

        this.loggingExecutor = Executors.newSingleThreadScheduledExecutor();
        loggingExecutor.scheduleAtFixedRate(
                () -> this.generateLogs(100),
                0,
                1,
                TimeUnit.SECONDS
        );
    }

    @Override
    public void stop(BundleContext bundleContext) {
        if (nonNull(loggingServiceReference)) {
            this.loggingExecutor.shutdown();
            ctx.ungetService(loggingServiceReference);
        }
    }
}
