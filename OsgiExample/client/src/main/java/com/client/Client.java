package com.client;

import com.service.definition.Greeter;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

public class Client implements BundleActivator, ServiceListener {

    private BundleContext ctx;
    private ServiceReference serviceReference;

    public void start(BundleContext ctx) {
        this.ctx = ctx;
        try {
            ctx.addServiceListener(
                    this, "(objectclass=" + Greeter.class.getName() + ")");
        } catch (InvalidSyntaxException ise) {
            ise.printStackTrace();
        }
    }

    public void serviceChanged(ServiceEvent serviceEvent) {
        int type = serviceEvent.getType();
        switch (type){
            case(ServiceEvent.REGISTERED):
                System.out.println("Notification of service registered.");
                serviceReference = serviceEvent
                        .getServiceReference();
                Greeter service = (Greeter)(ctx.getService(serviceReference));
                System.out.println( service.sayHiTo("John") );
                break;
            case(ServiceEvent.UNREGISTERING):
                System.out.println("Notification of service unregistered.");
                ctx.ungetService(serviceEvent.getServiceReference());
                break;
            default:
                break;
        }
    }

    public void stop(BundleContext bundleContext) {
        if(serviceReference != null) {
            ctx.ungetService(serviceReference);
        }
    }
}