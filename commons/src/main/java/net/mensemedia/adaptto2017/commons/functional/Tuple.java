package net.mensemedia.adaptto2017.commons.functional;


/**
 * Class Tuple.
 */
public final class Tuple<T1, T2> {
    private final T1 left;
    private final T2 right;

    private Tuple(final T1 left, final T2 right) {
        this.left = left;
        this.right = right;
    }

    public static <T1, T2> Tuple of(final T1 left, final T2 right) {
        return new Tuple<>(left, right);
    }

    public T1 getLeft() {
        return left;
    }

    public T2 getRight() {
        return right;
    }
}
