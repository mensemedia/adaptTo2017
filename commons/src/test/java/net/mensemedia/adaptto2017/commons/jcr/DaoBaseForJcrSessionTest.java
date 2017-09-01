package net.mensemedia.adaptto2017.commons.jcr;
import io.wcm.testing.mock.aem.junit.AemContext;
import net.mensemedia.adaptto2017.commons.functional.Tuple;
import net.mensemedia.adaptto2017.commons.jcr.DaoBaseForJcrSession;
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
import java.util.Optional;
import java.util.function.BiConsumer;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DaoBaseForJcrSessionTest {

    @Rule
    public AemContext aemContext = new AemContext();

    SlingRepository slingRepository;
    private SimpleCredentials credentials;
    private Session session;
    DaoBaseForJcrSession base;

    List<Tuple<String, Exception>> errors;
    BiConsumer errorConsumer;
    private MyAssertsJUnit4 asserts;

    @Before
    public void setUp() throws Exception {
        aemContext.registerInjectActivateService(new MockJcrSlingRepository());
        slingRepository = aemContext.getService(SlingRepository.class);
        assertNotNull(slingRepository);

        credentials = new SimpleCredentials("a", "b".toCharArray());

        session = slingRepository.login(credentials);
        assertNotNull(session);

        Node nodeAllSet = session.getRootNode().addNode("/all-set");
        nodeAllSet.setProperty("cq:template", "my-template");
        nodeAllSet.setProperty("group", "my-group");
        nodeAllSet.setProperty("type", "my-type");
        session.save();

        errors = new ArrayList<>();
        errorConsumer = (s, e) -> errors.add(Tuple.of(s, e));
        asserts = new MyAssertsJUnit4(errors);

        base = new DaoBaseForJcrSession(errorConsumer);
    }

    @Test
    public void test_getNode_ok() throws Exception {
        final Optional<Node> node = base.getNode(session, "/all-set");

        asserts.assertNoErrors();
        assertTrue("It should yield a value", node.isPresent());
    }

    @Test
    public void test_getNode_pathNotFound() throws Exception {
        final Optional<Node> node = base.getNode(session, "/missing");

        asserts.assertNoErrors();
        assertFalse("It should not yield a value when node does not exist", node.isPresent());
    }

    @Test
    public void test_getNode_error() throws Exception {
        Session session = mock(Session.class);
        when(session.getNode(anyString())).thenThrow(new RepositoryException("oops"));

        final Optional<Node> node = base.getNode(session, "/missing");

        asserts.assertErrors(Tuple.of("RepositoryException e", new RepositoryException("oops")));
        assertFalse("It should not yield a value when an exception occurred", node.isPresent());
    }

    @Test
    public void test_getNodePropertyAsString_ok() throws Exception {
        final Optional<Node> node = base.getNode(session, "/all-set");
        final Optional<String> prop = base.getNodePropertyAsString(node.get(), "cq:template");

        asserts.assertNoErrors();
        asserts.assertIsPresent(prop);
        assertEquals("my-template", prop.get());
    }

    @Test
    public void test_getNodePropertyAsString_missing() throws Exception {
        final Optional<Node> node = base.getNode(session, "/all-set");
        final Optional<String> prop = base.getNodePropertyAsString(node.get(), "missing");

        asserts.assertNoErrors();
        asserts.assertIsNotPresent(prop);
    }

    @Test
    public void test_getNodePropertyAsString_error() throws Exception {
        final Node node = mock(Node.class);
        when(node.hasProperty(anyString())).thenThrow(new RepositoryException("oops"));
        final Optional<String> prop = base.getNodePropertyAsString(node, "error");

        asserts.assertErrors(Tuple.of("RepositoryException e", new RepositoryException("oops")));
        asserts.assertIsNotPresent(prop);
    }
}
