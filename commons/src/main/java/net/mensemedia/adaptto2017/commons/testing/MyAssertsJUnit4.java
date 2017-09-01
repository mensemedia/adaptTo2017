package net.mensemedia.adaptto2017.commons.testing;
import net.mensemedia.adaptto2017.commons.functional.Tuple;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class MyAssertsJUnit4 {
    private final List<Tuple<String, Exception>> errors;

    public MyAssertsJUnit4(final List<Tuple<String, Exception>> errors) {
        this.errors = errors;
    }

    public void assertErrors(final Tuple<String, Exception>... expectedErrors) {
        assertEquals(expectedErrors.length, errors.size());

        for (int i = 0; i < expectedErrors.length; i++) {
            final Tuple<String, Exception> expectedExceptionTuple = expectedErrors[i];
            final Tuple<String, Exception> stringExceptionTuple = errors.get(i);

            assertEquals(expectedExceptionTuple.getLeft(), stringExceptionTuple.getLeft());
            assertEquals(expectedExceptionTuple.getRight().getClass(), stringExceptionTuple.getRight().getClass());
            assertEquals(expectedExceptionTuple.getRight().getMessage(), stringExceptionTuple.getRight().getMessage());
        }
    }

    public void assertNoErrors(final String message) {
        assertEquals(message, 0, errors.size());
    }

    public void assertNoErrors() {
        assertNoErrors("It should not yield errors");
    }

    public void assertIsPresent(final Optional optional) {
        assertTrue(optional.isPresent());
    }

    public void assertIsNotPresent(final Optional optional) {
        assertFalse(optional.isPresent());
    }

}
