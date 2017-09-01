package net.mensemedia.adaptto2017.snippets;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ObjectArrayArguments;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(JUnitPlatform.class)
public class FactorialRecursiveImplTest extends FactorialTestBase {
    @Override
    protected Factorial getInstance() {
        return new FactorialRecursiveImpl();
    }
}
