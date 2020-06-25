package com.generator;

import com.manager.api.definition.LoggingService;
import com.manager.api.model.EventType;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import java.util.UUID;

import static java.util.Objects.nonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class LoggingGenerator implements BundleActivator, ServiceListener {

    private BundleContext ctx;
    private ServiceReference<?> loggingServiceReference;
    private LoggingService loggingService;

    public void generateLogs(int number) {
        for (int i = 0; i < number; i++) {
            loggingService.logEvent(EventType.RANDOM, this.generateLogIn10Miliseconds());
        }
    }

    private String generateLogIn10Miliseconds() {
        try {
            MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return UUID.randomUUID().toString();
    }

    @Override
    public void serviceChanged(ServiceEvent serviceEvent) {
        int type = serviceEvent.getType();
        switch (type) {
            case (ServiceEvent.REGISTERED):
                loggingServiceReference = serviceEvent.getServiceReference();
                this.loggingService = (LoggingService) (ctx.getService(loggingServiceReference));
                this.generateLogs(100);
                break;
            case (ServiceEvent.UNREGISTERING):
                ctx.ungetService(serviceEvent.getServiceReference());
                break;
            default:
                break;
        }
    }

    @Override
    public void start(BundleContext bundleContext) {
        this.ctx = bundleContext;
        try {
            ctx.addServiceListener(this, "(objectclass=" + LoggingService.class.getName() + ")");
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop(BundleContext bundleContext) {
        if (nonNull(loggingServiceReference)) {
            ctx.ungetService(loggingServiceReference);
        }
    }
}
