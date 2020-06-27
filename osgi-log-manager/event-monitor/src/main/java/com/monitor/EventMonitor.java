package com.monitor;

import com.manager.api.definition.MonitoringService;
import com.manager.api.definition.RetrievalService;
import com.manager.api.model.EventType;
import com.manager.api.model.MonitoringListener;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

public class EventMonitor implements BundleActivator {

    private BundleContext ctx;
    private ServiceReference monitoringServiceReference;
    private ServiceReference retrievalServiceReference;

    private MonitoringService monitoringService;
    private RetrievalService retrievalService;
    private ScheduledExecutorService retrievalExecutor;

    @Override
    public void start(BundleContext bundleContext) {
        this.ctx = bundleContext;
        this.monitoringServiceReference = bundleContext.getServiceReference(MonitoringService.class.getName());
        this.monitoringService = (MonitoringService) bundleContext.getService(monitoringServiceReference);

        final MonitoringListener monitoringListener = new MonitoringListener(EventType.USER, EventType.RANDOM);
        this.monitoringService.addMonitoringListener(monitoringListener);


        this.retrievalServiceReference = bundleContext.getServiceReference(RetrievalService.class.getName());
        this.retrievalService = (RetrievalService) bundleContext.getService(retrievalServiceReference);

        retrievalExecutor = Executors.newSingleThreadScheduledExecutor();
        retrievalExecutor.scheduleAtFixedRate(
                this::retrieveEventsIn2Minutes,
                0,
                2,
                TimeUnit.MINUTES
        );
    }

    @Override
    public void stop(BundleContext bundleContext) {
        if (nonNull(monitoringServiceReference)) {
            ctx.ungetService(monitoringServiceReference);
        }
        else if(nonNull(retrievalServiceReference)) {
            retrievalExecutor.shutdown();
            ctx.ungetService(retrievalServiceReference);
        }
    }

    private void retrieveEventsIn2Minutes() {
        this.retrievalService.retrieve(EventType.RANDOM, LocalDateTime.now().minusMinutes(2), LocalDateTime.now());
    }
}
