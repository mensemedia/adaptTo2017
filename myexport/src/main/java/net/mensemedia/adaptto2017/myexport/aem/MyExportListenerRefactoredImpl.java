package net.mensemedia.adaptto2017.myexport.aem;
import com.day.cq.commons.jcr.JcrConstants;
import net.mensemedia.adaptto2017.myexport.domain.*;
import net.mensemedia.adaptto2017.myexport.exporter.ExporterMemImpl;
import net.mensemedia.adaptto2017.myexport.storage.DaoJcrSessionImpl;
import net.mensemedia.adaptto2017.commons.jcr.JcrSessionTemplate;
import net.mensemedia.adaptto2017.commons.jcr.JcrSessionTemplateImpl;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Component
public class MyExportListenerRefactoredImpl implements EventListener {
    private static final Logger LOG = LoggerFactory.getLogger(MyExportListenerRefactoredImpl.class);

    @Reference
    private SlingRepository repository;

    private final BiConsumer<String, Exception> errorConsumer = LOG::error;
    private final Consumer<String> warningConsumer = LOG::warn;

    private Supplier<Dao> daoFactory = () -> {
        final SimpleCredentials credentials = new SimpleCredentials("me", "secret".toCharArray());
        final JcrSessionTemplate jcrSessionTemplate = new JcrSessionTemplateImpl(
                repository, credentials, errorConsumer
        );
        return new DaoJcrSessionImpl(jcrSessionTemplate, errorConsumer);
    };

    private Supplier<Exporter> exporterFactory = () -> new ExporterMemImpl(errorConsumer);

    private Supplier<MyServiceImpl> serviceFactory = () -> new MyServiceImpl(
            daoFactory.get(), exporterFactory.get(),
            errorConsumer, warningConsumer);

    @Override
    public void onEvent(final EventIterator events) {
        final MyService service = createService();

        while (events.hasNext()) {
            final Event event = events.nextEvent();
            onSingleEvent(service, event);
        }
    }

    private void onSingleEvent(final MyService service, final Event event) {
        final ProcessingContext ctx = toProcessingContext(event, LOG::error);
        service.process(ctx);
    }

    MyService createService() {
        return serviceFactory.get();
    }

    static ProcessingContext toProcessingContext(final Event event,
                                                 final BiConsumer<String, Exception> errorConsumer) {
        try {
            return new ProcessingContext(
                    Optional.ofNullable(event.getPath()),
                    s -> s.endsWith(JcrConstants.JCR_CONTENT)
            );
        } catch (RepositoryException e) {
            errorConsumer.accept("Error when getting path from event.", e);
            return new ProcessingContext(Optional.empty(), s -> false);
        }
    }
}
