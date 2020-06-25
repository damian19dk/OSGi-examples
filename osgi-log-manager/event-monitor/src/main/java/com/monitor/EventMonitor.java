package com.monitor;

import com.manager.api.definition.MonitoringService;
import com.manager.api.model.EventType;
import com.manager.api.model.MonitoringListener;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import static java.util.Objects.nonNull;

public class EventMonitor implements BundleActivator, ServiceListener {

    private BundleContext ctx;
    private ServiceReference serviceReference;

    @Override
    public void serviceChanged(ServiceEvent serviceEvent) {
        switch (serviceEvent.getType()) {
            case (ServiceEvent.REGISTERED):
                serviceReference = serviceEvent.getServiceReference();
                Object service = ctx.getService(serviceReference);

                final MonitoringService monitoringService = (MonitoringService) service;
                final MonitoringListener monitoringListener = new MonitoringListener(EventType.USER, EventType.RANDOM);
                monitoringService.addMonitoringListener(monitoringListener);
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
            ctx.addServiceListener(this, "(objectclass=" + MonitoringService.class.getName() + ")");
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop(BundleContext bundleContext) {
        if (nonNull(serviceReference)) {
            ctx.ungetService(serviceReference);
        }
    }
}
