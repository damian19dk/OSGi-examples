package com.manager.api;

import com.manager.api.definition.LoggingService;
import com.manager.api.definition.MonitoringService;
import com.manager.api.definition.RetrievalService;
import com.manager.impl.LoggingServiceImpl;
import com.manager.impl.MonitoringServiceImpl;
import com.manager.impl.RetrievalServiceImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.Hashtable;

public class EventManager implements BundleActivator {

    private ServiceRegistration<LoggingService> loggingServiceRegistration;
    private ServiceRegistration<MonitoringService> monitoringServiceRegistration;
    private ServiceRegistration<RetrievalService> retrievalServiceRegistration;

    @Override
    public void start(BundleContext bundleContext) {
        System.out.println("Registering services");
        loggingServiceRegistration = bundleContext.registerService(LoggingService.class, new LoggingServiceImpl(), new Hashtable<String, String>());
        monitoringServiceRegistration = bundleContext.registerService(MonitoringService.class, new MonitoringServiceImpl(), new Hashtable<String, String>());
        retrievalServiceRegistration = bundleContext.registerService(RetrievalService.class, new RetrievalServiceImpl(), new Hashtable<String, String>());
    }

    @Override
    public void stop(BundleContext bundleContext) {
        System.out.println("Unregistering services");
        loggingServiceRegistration.unregister();
        monitoringServiceRegistration.unregister();
        retrievalServiceRegistration.unregister();
    }
}
