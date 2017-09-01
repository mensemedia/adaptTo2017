package net.mensemedia.adaptto2017.commons.jcr;
import org.apache.sling.jcr.api.SlingRepository;

import javax.jcr.Credentials;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class JcrSessionTemplateImpl implements JcrSessionTemplate {
    private SlingRepository repository;
    private final Credentials credentials;
    private final BiConsumer<String, Exception> errorConsumer;

    public JcrSessionTemplateImpl(final SlingRepository repository,
                                  final Credentials credentials,
                                  final BiConsumer<String, Exception> errorConsumer) {
        this.repository = repository;
        this.credentials = credentials;
        this.errorConsumer = errorConsumer;
    }

    @Override
    public void execute(final Consumer<Session> consumer) {
        Session session = null;
        try {
            session = repository.login(credentials);

            // magic:
            consumer.accept(session);
        } catch (RepositoryException e) {
            errorConsumer.accept("Error during event processing.", e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    };
}
