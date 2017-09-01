package net.mensemedia.adaptto2017.snippets;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnitPlatform.class)
public abstract class FactorialTestBase {

    protected abstract Factorial getInstance();

    @ParameterizedTest(name = "n \"{1}\" should yield {0}. ")
    @MethodSource(names = "factorialProvider")
    void test_factorial(final int expected, final int input) {
        Factorial factorial = getInstance();
        assertEquals(expected, factorial.fac(input));
    }

    static Stream<Arguments> factorialProvider() {
        return Stream.of(
                ObjectArrayArguments.create(1,      1),
                ObjectArrayArguments.create(2,      2),
                ObjectArrayArguments.create(6,      3),
                ObjectArrayArguments.create(24,     4),
                ObjectArrayArguments.create(120,    5),
                ObjectArrayArguments.create(720,    6),
                ObjectArrayArguments.create(5040,   7),
                ObjectArrayArguments.create(40320,  8),
                ObjectArrayArguments.create(362880, 9)
        );
    }
}
