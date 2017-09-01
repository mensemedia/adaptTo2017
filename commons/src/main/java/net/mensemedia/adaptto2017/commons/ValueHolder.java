package net.mensemedia.adaptto2017.commons;
import java.util.Optional;

public class ValueHolder<T> {
    private T value;
    private boolean wasSet = false;

    public void set(final T value) {
        this.value = value;
        wasSet = true;
    }

    public boolean wasSet() {
        return wasSet;
    }

    public Optional<T> get() {
        return Optional.ofNullable(value);
    }
}
