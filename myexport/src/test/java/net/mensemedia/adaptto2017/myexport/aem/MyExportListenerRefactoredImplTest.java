package net.mensemedia.adaptto2017.myexport.aem;
import io.wcm.testing.mock.aem.junit.AemContext;
import net.mensemedia.adaptto2017.commons.functional.Tuple;
import net.mensemedia.adaptto2017.commons.testing.FieldHack0r;
import net.mensemedia.adaptto2017.commons.testing.MyAssertsJUnit4;
import net.mensemedia.adaptto2017.myexport.domain.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.jcr.RepositoryException;
import javax.jcr.observation.Event;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MyExportListenerRefactoredImplTest {

    List<Tuple<String, Exception>> errors;
    BiConsumer errorConsumer;
    List<MyExportData> exportedItems;
    MyAssertsJUnit4 asserts;

    MyExportListenerRefactoredImpl myExportListener;

    @Rule
    public AemContext aemContext = new AemContext();


    @Before
    public void setUp() throws Exception {
        errors = new ArrayList<>();
        errorConsumer = (s, e) -> errors.add(Tuple.of(s, e));
        asserts = new MyAssertsJUnit4(errors);

        exportedItems = new ArrayList<>();

        aemContext.registerService(
                MyExportListenerRefactoredImpl.class,
                new MyExportListenerRefactoredImpl()
        );

        myExportListener = aemContext.getService(MyExportListenerRefactoredImpl.class);
    }

    @Test
    public void test_onEvent_noEvents() throws Exception {
        List<ProcessingContext> contexts = new ArrayList<>();
        /* FieldHack0r:
        * In a Clean Code implementation we would pass the service factory via the constructor.
        * However, OSGi does not allow for constructor injection.
        * As an alternative we could create the default constructor plus a custom constructor for injection.
        * But then this newly added code would exist only for testing purposes and that's ugly.
        * The same applies to adding a setter.
        *
        * I.e. FieldHack0r is there because OSGi's design restricts our options
        * which in itself proves that TDD shows you weaknesses in the design.
        * */
        FieldHack0r fieldHack0r = new FieldHack0r(myExportListener);
        fieldHack0r.set("serviceFactory", (Supplier) () -> (MyService) contexts::add);

        final List<Event> events = Arrays.asList();
        myExportListener.onEvent(new EventIteratorImpl(events));

        assertEquals("It should not process any ctxs when no event was triggered",
                0, contexts.size());
    }

    @Test
    public void test_onEvent_1Event() throws Exception {
        List<ProcessingContext> contexts = new ArrayList<>();
        FieldHack0r fieldHack0r = new FieldHack0r(myExportListener);
        fieldHack0r.set("serviceFactory", (Supplier) () -> (MyService) contexts::add);

        final List<Event> events = Arrays.asList(createValidEvent("/a/b/"));
        myExportListener.onEvent(new EventIteratorImpl(events));

        assertEquals(
                "It should process the 1 event",
                1, contexts.size()
        );
        assertEquals(
                "It should process the 1 event",
                Optional.of("/a/b/jcr:content"), contexts.get(0).getPath()
        );
    }

    @Test
    public void test_onEvent_events() throws Exception {
        FieldHack0r fieldHack0r = new FieldHack0r(myExportListener);
        Dao dao = ctx -> Optional.of(new MyExportData().setGroup("g").setRangeType("mr").setTemplate("t"));
        Exporter exporter = exportedItems::add;
        fieldHack0r.set("daoFactory", (Supplier) () -> dao);
        fieldHack0r.set("exporterFactory", (Supplier) () -> exporter);

        final List<Event> events = Arrays.asList(createValidEvent("/a/b/"));
        myExportListener.onEvent(new EventIteratorImpl(events));
    }

    @Test
    public void test_createService() throws Exception {
        final MyExportListenerRefactoredImpl myExportListener = aemContext.getService(MyExportListenerRefactoredImpl.class);
        assertNotNull(myExportListener.createService());
    }

    @Test
    public void test_toProcessingContext_ok() throws Exception {
        final Event event = createValidEvent("/a/b/c/");
        final ProcessingContext ctx = MyExportListenerRefactoredImpl.toProcessingContext(event, errorConsumer);
        assertEquals(Optional.of("/a/b/c/jcr:content"), ctx.getPath());
        assertNotNull(ctx.getPathFilter());
        asserts.assertNoErrors();
    }

    @Test
    public void test_toProcessingContext_null() throws Exception {
        final Event event = createEvent(null);
        final ProcessingContext ctx = MyExportListenerRefactoredImpl.toProcessingContext(event, errorConsumer);
        assertEquals(Optional.empty(), ctx.getPath());
        asserts.assertNoErrors();
    }

    @Test
    public void test_toProcessingContext_WrongSuffix() throws Exception {
        final Event event = createEvent("/a/b/c");
        final ProcessingContext ctx = MyExportListenerRefactoredImpl.toProcessingContext(event, errorConsumer);
        assertEquals("It should yield the path... suffix checking happens in service",
                Optional.of("/a/b/c"), ctx.getPath());
        assertNotNull(ctx.getPathFilter());
        asserts.assertNoErrors();
    }

    @Test
    public void test_toProcessingContext_error() throws Exception {
        final Event event = createThrowingEvent("oops");
        final ProcessingContext ctx = MyExportListenerRefactoredImpl.toProcessingContext(event, errorConsumer);

        assertEquals(Optional.empty(), ctx.getPath());

        assertEquals(1, errors.size());
        assertEquals("Error when getting path from event.", errors.get(0).getLeft());
        assertEquals("oops", errors.get(0).getRight().getMessage());
        assertEquals(RepositoryException.class, errors.get(0).getRight().getClass());
    }

    private Event createEvent(final String path) throws RepositoryException {
        final Event event = mock(Event.class);
        when(event.getPath()).thenReturn(path);
        return event;
    }

    private Event createValidEvent(final String pathPrefix) throws RepositoryException {
        return createEvent(pathPrefix + "jcr:content");
    }

    private Event createThrowingEvent(final String message) throws RepositoryException {
        final Event event = mock(Event.class);
        when(event.getPath()).thenThrow(new RepositoryException(message));
        return event;
    }
}
