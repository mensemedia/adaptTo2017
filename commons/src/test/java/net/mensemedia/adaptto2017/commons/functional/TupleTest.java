package net.mensemedia.adaptto2017.commons.functional;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TupleTest {
    @Test
    public void test_ok() throws Exception {
        assertEquals("a", Tuple.of("a", 1).getLeft());
        assertEquals(1, Tuple.of("a", 1).getRight());
    }

    @Test
    public void test_nulls() throws Exception {
        assertEquals(null, Tuple.of(null, null).getLeft());
        assertEquals(null, Tuple.of(null, null).getRight());
    }

}
