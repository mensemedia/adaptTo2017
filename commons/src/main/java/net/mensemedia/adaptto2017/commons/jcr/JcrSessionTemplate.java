package net.mensemedia.adaptto2017.commons.jcr;
import javax.jcr.Session;
import java.util.function.Consumer;

public interface JcrSessionTemplate {
    void execute(Consumer<Session> consumer);
}
