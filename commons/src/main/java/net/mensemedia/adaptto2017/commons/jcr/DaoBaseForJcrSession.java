package net.mensemedia.adaptto2017.commons.jcr;
import javax.jcr.*;
import java.util.Optional;
import java.util.function.BiConsumer;

public class DaoBaseForJcrSession {
    private final BiConsumer<String, Exception> errorConsumer;

    public DaoBaseForJcrSession(final BiConsumer<String, Exception> errorConsumer) {
        this.errorConsumer = errorConsumer;
    }

    public Optional<Node> getNode(final Session session, final String path) {
        try {
            return Optional.ofNullable(session.getNode(path));
        } catch (PathNotFoundException e) {
            return Optional.empty();
        } catch (RepositoryException e) {
            errorConsumer.accept("RepositoryException e", e);
            return Optional.empty();
        }
    }


    public Optional<String> getNodePropertyAsString(final Node node, final String key) {
        try {
            if (node.hasProperty(key)) {
                final Property property = node.getProperty(key);
                return Optional.ofNullable(property.getString());
            }
        } catch (RepositoryException e) {
            errorConsumer.accept("RepositoryException e", e);
        }
        return Optional.empty();
    }
}
