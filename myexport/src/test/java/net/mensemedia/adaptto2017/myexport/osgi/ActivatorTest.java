package net.mensemedia.adaptto2017.myexport.osgi;
import net.mensemedia.adaptto2017.myexport.osgi.Activator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ActivatorTest {     Activator activator;
    PrintStream origSystemOut;
    ByteArrayOutputStream fakeSystemOut;

    @Before
    public void setUp() throws Exception {
        activator = new Activator();

        origSystemOut = System.out;
        fakeSystemOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeSystemOut));
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(origSystemOut);
    }


    @Test
    public void test_start() throws Exception {
        activator.start(mockBundleContext("xyz"));
        assertEquals("Starting Bundle xyz...", getSystemOutAsString());
    }

    @Test
    public void test_stop() throws Exception {
        activator.stop(mockBundleContext("abc"));
        assertEquals("Stopping Bundle abc.", getSystemOutAsString());
    }


    private String getSystemOutAsString() {
        return new String(fakeSystemOut.toByteArray(), Charset.forName("UTF-8")).trim();
    }

    BundleContext mockBundleContext(String symbolicName) {
        Bundle bundle = mock(Bundle.class);
        when(bundle.getSymbolicName()).thenReturn(symbolicName);

        BundleContext bundleContext = mock(BundleContext.class);
        when(bundleContext.getBundle()).thenReturn(bundle);

        return bundleContext;
    }
}
