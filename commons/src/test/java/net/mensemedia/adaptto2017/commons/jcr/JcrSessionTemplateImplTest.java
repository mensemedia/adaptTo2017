package net.mensemedia.adaptto2017.commons.jcr;
import io.wcm.testing.mock.aem.junit.AemContext;
import net.mensemedia.adaptto2017.commons.ValueHolder;
import net.mensemedia.adaptto2017.commons.functional.Tuple;
import net.mensemedia.adaptto2017.commons.testing.MyAssertsJUnit4;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.testing.mock.sling.MockJcrSlingRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JcrSessionTemplateImplTest {

    @Rule
    public AemContext aemContext = new AemContext();

    SlingRepository slingRepository;
    private Node nodeAllSet;
    private Node nodeAllBlank;
    private Node nodeAllMissing;
    private Session session;
    List<Tuple<String, Exception>> errors;
    BiConsumer errorConsumer;
    private SimpleCredentials credentials;
    private JcrSessionTemplate jcrSessionTemplate;
    private MyAssertsJUnit4 asserts;

    @Before
    public void setUp() throws Exception {
        aemContext.registerInjectActivateService(new MockJcrSlingRepository());
        slingRepository = aemContext.getService(SlingRepository.class);
        assertNotNull(slingRepository);

        credentials = new SimpleCredentials("a", "b".toCharArray());
        session = slingRepository.login(credentials);
        assertNotNull(session);

        errors = new ArrayList<>();
        errorConsumer = (s, e) -> errors.add(Tuple.of(s, e));

        asserts = new MyAssertsJUnit4(errors);
    }

    @Test
    public void test_execute_ok() throws Exception {
        JcrSessionTemplateImpl template = new JcrSessionTemplateImpl(
                slingRepository, credentials, errorConsumer
        );

        ValueHolder<Session> valueHolder = new ValueHolder<>();
        template.execute(valueHolder::set);

        asserts.assertNoErrors();
        assertTrue(valueHolder.wasSet());
        asserts.assertIsPresent(valueHolder.get());
    }

    @Test
    public void test_execute_session_null() throws Exception {
        slingRepository = mock(SlingRepository.class);
        when(slingRepository.login(credentials)).thenReturn(null);

        JcrSessionTemplateImpl template = new JcrSessionTemplateImpl(
                slingRepository, credentials, errorConsumer
        );

        ValueHolder<Session> valueHolder = new ValueHolder<>();
        template.execute(valueHolder::set);

        asserts.assertNoErrors();
        assertTrue(valueHolder.wasSet());
    }

    @Test
    public void test_execute_exception() throws Exception {
        slingRepository = mock(SlingRepository.class);
        when(slingRepository.login(credentials)).thenThrow(new RepositoryException("oops"));

        JcrSessionTemplateImpl template = new JcrSessionTemplateImpl(
                slingRepository, credentials, errorConsumer
        );

        ValueHolder<Session> valueHolder = new ValueHolder<>();
        template.execute(valueHolder::set);

        asserts.assertErrors(
                Tuple.of("Error during event processing.", new RepositoryException("oops"))
        );
        assertFalse(valueHolder.wasSet());
    }
}
