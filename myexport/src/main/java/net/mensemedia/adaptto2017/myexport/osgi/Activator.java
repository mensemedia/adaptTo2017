package net.mensemedia.adaptto2017.myexport.osgi;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
    @Override
    public void start(final BundleContext context) throws Exception {
        System.out.println(
                String.format("Starting Bundle %s...", context.getBundle().getSymbolicName())
        );
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        System.out.println(
                String.format("Stopping Bundle %s.", context.getBundle().getSymbolicName())
        );
    }
}
