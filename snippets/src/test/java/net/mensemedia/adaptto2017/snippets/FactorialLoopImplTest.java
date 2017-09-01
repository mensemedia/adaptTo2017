package net.mensemedia.adaptto2017.snippets;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class FactorialLoopImplTest extends FactorialTestBase {
    @Override
    protected Factorial getInstance() {
        return new FactorialLoopImpl();
    }
}
