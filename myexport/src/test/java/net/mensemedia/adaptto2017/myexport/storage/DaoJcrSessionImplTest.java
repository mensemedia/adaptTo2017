package net.mensemedia.adaptto2017.myexport.storage;
import io.wcm.testing.mock.aem.junit.AemContext;
import net.mensemedia.adaptto2017.commons.functional.Tuple;
import net.mensemedia.adaptto2017.commons.testing.MyAssertsJUnit4;
import net.mensemedia.adaptto2017.myexport.domain.MyExportData;
import net.mensemedia.adaptto2017.myexport.domain.ProcessingContext;
import net.mensemedia.adaptto2017.commons.jcr.JcrSessionTemplate;
import net.mensemedia.adaptto2017.commons.jcr.JcrSessionTemplateImpl;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.testing.mock.sling.MockJcrSlingRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import static org.junit.Assert.*;

public class DaoJcrSessionImplTest {

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
    private DaoJcrSessionImpl dao;
    private MyAssertsJUnit4 asserts;

    @Before
    public void setUp() throws Exception {
        aemContext.registerInjectActivateService(new MockJcrSlingRepository());
        slingRepository = aemContext.getService(SlingRepository.class);
        assertNotNull(slingRepository);

        credentials = new SimpleCredentials("a", "b".toCharArray());

        session = slingRepository.login(credentials);
        assertNotNull(session);

        nodeAllSet = session.getRootNode().addNode("/all-set");
        nodeAllSet.setProperty("cq:template", "my-template");
        nodeAllSet.setProperty("group", "my-group");
        nodeAllSet.setProperty("type", "my-type");

        nodeAllBlank = session.getRootNode().addNode("/all-blank");
        nodeAllBlank.setProperty("cq:template", "");
        nodeAllBlank.setProperty("group", "");
        nodeAllBlank.setProperty("type", "");

        nodeAllMissing = session.getRootNode().addNode("/all-missing");

        session.save();


        errors = new ArrayList<>();
        errorConsumer = (s, e) -> errors.add(Tuple.of(s, e));
        jcrSessionTemplate = new JcrSessionTemplateImpl(
                slingRepository, credentials, errorConsumer);

        dao = new DaoJcrSessionImpl(jcrSessionTemplate, errorConsumer);

        asserts = new MyAssertsJUnit4(errors);
    }

    @Test
    public void test_getData_ok() throws Exception {
        final Optional<MyExportData> data = dao.getData(new ProcessingContext(Optional.of("/all-set"), s -> true));

        asserts.assertNoErrors();
        asserts.assertIsPresent(data);
    }

    @Test
    public void test_getData_path_empty() throws Exception {
        final Optional<MyExportData> data = dao.getData(new ProcessingContext(Optional.empty(), s -> true));

        asserts.assertNoErrors();
        asserts.assertIsNotPresent(data);
    }

    @Test
    public void test_getData_path_noSuchNode() throws Exception {
        final Optional<MyExportData> data = dao.getData(new ProcessingContext(Optional.of("/missing"), s -> true));

        asserts.assertNoErrors();
        asserts.assertIsNotPresent(data);
    }

    @Test
    public void test_toDataAllSet() throws Exception {
        // TODO: refactor
        // the fact that we need to pass a value for JcrSessionTemplate
        // to the constructor indicates that toData does not need JcrSessionTemplate.
        // This usually indicated that the DAO can be split up further,
        // i.e. we should extract the toData into a separate class,
        // e.g. JcrNodeToDataAdapter.
        DaoJcrSessionImpl dao = new DaoJcrSessionImpl(null, errorConsumer);
        Node node = session.getNode("/all-set");

        MyExportData data = dao.toData(node);
        assertAllSet(data);
        asserts.assertNoErrors();
    }

    @Test
    public void test_toDataAllBlank() throws Exception {
        DaoJcrSessionImpl dao = new DaoJcrSessionImpl(null, errorConsumer);
        Node node = session.getNode("/all-blank");

        MyExportData data = dao.toData(node);
        assertEquals(Optional.empty(), data.getTemplate());
        assertEquals(Optional.empty(), data.getGroup());
        assertEquals(Optional.empty(), data.getRangeType());
        asserts.assertNoErrors();
    }

    @Test
    public void test_toDataAllMissing() throws Exception {
        DaoJcrSessionImpl dao = new DaoJcrSessionImpl(null, errorConsumer);
        Node node = session.getNode("/all-missing");

        MyExportData data = dao.toData(node);
        assertEquals(Optional.empty(), data.getTemplate());
        assertEquals(Optional.empty(), data.getGroup());
        assertEquals(Optional.empty(), data.getRangeType());
        asserts.assertNoErrors();
    }



    private void assertAllSet(final MyExportData data) {
        assertEquals(Optional.of("my-template"), data.getTemplate());
        assertEquals(Optional.of("my-group"), data.getGroup());
        assertEquals(Optional.of("my-type"), data.getRangeType());
    }
}
