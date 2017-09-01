package net.mensemedia.adaptto2017.myexport.domain;
import java.util.Optional;
import java.util.function.Predicate;

public class ProcessingContext {
    private final Optional<String> path;

    public ProcessingContext(final Optional<String> path, final Predicate<String> pathFilter) {
        this.path = path;
        this.pathFilter = pathFilter;
    }

    public Optional<String> getPath() {
        return path;
    }


    private final Predicate<String> pathFilter;

    public Predicate<String> getPathFilter() {
        return pathFilter;
    }
}
